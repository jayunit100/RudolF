{-# LANGUAGE FlexibleInstances, TypeSynonymInstances #-}

module Formatter (
	varian,
	bruker,
	custom,
	json,
	toolkit,
	separateTransients
) where


import Model
import Grouper
import qualified Data.Map as M
import qualified Data.List as L
import qualified Data.Set as S
import qualified Text.RJson as R
import qualified GHC.Exts as E
--import qualified Data.Ord as O


-- ignore the number of transients; one coordinates/quadunit per line
-- 0-indexed
varian :: Schedule -> String
varian sched = concat $ L.intersperse "\n" formattedPts
  where
    formattedPts = map (sprint . decrementCoordinates) $ removeDuplicates $ getPoints sched
    decrementCoordinates pt = makePoint (map (flip (-) 1) $ gridPoint pt) $ quadUnit pt


-- ignore quadrature, transients; print out unique coordinates; one number per line
-- 1-indexed
bruker :: Schedule -> String
bruker sched = concat $ L.intersperse "\n" $ map show coordinates
  where
    coordinates = concat $ removeDuplicates gridPoints                       -- put all integers in a single list (each integer corresponds to a line)
    gridPoints = map gridPoint $ getPoints sched                                -- unwrap grid points from Schedule, Map, tuple, Point contexts


removeDuplicates :: (Ord a) => [a] -> [a]
removeDuplicates = S.toList  .  S.fromList


-- ignore transients; all quadunits of a coordinates in one line
-- 1-indexed
toolkit :: Schedule -> String
toolkit sched = concat $ L.intersperse "\n" $ map lineForm ptlines
  where
    lineForm (gp, qus) = concat $ L.intersperse " " (sprint gp : (map sprint qus))    -- put a space between each coordinate, QuadUnit, all together on one line
    ptlines = map (fmap (map fst)) pointTransients                                    -- get rid of the transients, keeping just the GridPoint and the QuadratureUnits
    pointTransients = (getGrouper combTransCombQuad) $ getPoints sched                -- group the points into [(GridPoint, [(QuadratureUnit, Transients)])]


-- one coordinates/quadunit per line
-- 1-indexed
custom :: Schedule -> String
custom = sprint


-- one transient per line; like varian format except that points may be repeated (to indicate multiple transients)
-- 1-indexed
separateTransients :: Schedule -> String
separateTransients sched = concat $ L.intersperse "\n" tLines -- transLine
  where
    tLines = do
      pt <- L.sort $ getPoints sched                                                            -- unwrap points from Schedule, Map context
      return $ formatPoint pt                                                     
    formatPoint pt = concat $ L.intersperse " " [sprint $ gridPoint pt, sprint $ quadUnit pt]     -- put a space between each coordinate, quadunit


-- 1-indexed
json :: Schedule -> String
json = show . toJson

-------------------------------------------------

toJson :: Schedule -> R.JsonData
toJson sched = R.JDObject $ M.fromList [("points", pointsToJson $ getPoints sched)]
  where
    pointsToJson = R.JDArray . map ptToJson

ptToJson :: Point -> R.JsonData
ptToJson pt = R.JDObject $ M.fromList [("gridPoint", gridPointToJson $ gridPoint pt),
                                       ("quadratureUnit", quadUnitToJson $ quadUnit pt)]
  where
    gridPointToJson = R.JDArray . map (R.JDNumber . fromInteger)
    quadUnitToJson = R.JDArray . map (R.JDString . show)

--------------------------------------------------

class SchedulePrint a where
  sprint :: a -> String

instance SchedulePrint Quadrature where
  sprint = show

instance SchedulePrint GridPoint where
  sprint = concat  .  L.intersperse " "  .  fmap show

instance SchedulePrint QuadUnit where
  sprint = concat . fmap sprint

instance SchedulePrint Point where
  sprint pt = concat [sprint $ gridPoint pt, " ", sprint $ quadUnit pt]

instance SchedulePrint Schedule where
  sprint sched = foldr comb "" groupedPts
    where
      groupedPts = (getGrouper combTransSepQuad) $ getPoints sched
      comb (pt, t) base = concat [sprint pt, " ", show t, "\n", base]


