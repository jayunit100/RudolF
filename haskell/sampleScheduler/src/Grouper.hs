module Grouper (
	Grouper,
	getGrouper,
	getGridPoint,
	getUngrouper,
	sepTransSepQuad,
	combTransSepQuad,
	combTransCombQuad
) where

import Model
import GHC.Exts (groupWith)
import Data.List (genericLength, genericReplicate)

data Grouper a = Grouper { getGrouper :: ([Point] -> [a]),
			getGridPoint :: a -> GridPoint,
			getUngrouper :: ([a] -> [Point]) }

sepTransSepQuad :: Grouper Point
sepTransSepQuad = Grouper g ggp ug
  where
    g = id
    ggp = gridPoint
    ug = id


combTransCombQuad :: Grouper (GridPoint, [(QuadUnit, Integer)])
combTransCombQuad = Grouper g ggp ug
  where
    g pts = map morpher $ groupWith (gridPoint . fst) ptsByPoint
      where
        morpher mpts@((pt,t):ps) = (gridPoint pt, map (\(pt1, t1) -> (quadUnit pt1, t1)) mpts)           -- can this be cleaned up?
        ptsByPoint = map (\x -> (head x, genericLength x)) $ groupWith id pts
    ggp = fst
    ug gpts = do
	(gp, qu_ts) <- gpts
	(qu, t) <- qu_ts
        [1 .. t]
	return $ makePoint gp qu

combTransSepQuad :: Grouper (Point, Integer)
combTransSepQuad = Grouper g ggp ug
  where
    g = map (\pts -> (head pts, genericLength pts)) . groupWith id
    ggp = gridPoint . fst
    ug = concatMap (\(pt, t) -> genericReplicate t pt)


-- I think this doesn't make any sense
--sepTransCombQuad :: Grouper (GridPoint, [QuadUnit]) 
--sepTransCombQuad = Grouper g gp ug
--  where
--    g pts = map morpher $ L.groupBy (F.on (==) (gridPoint . fst)) $ L.sortBy (O.comparing (gridPoint . fst)) pts
--    morpher mpts = (gridPoint $ fst $ head mpts, map (\(pt, t) -> L.genericReplicate t $ quadUnit pt) mpts)
--    gp = fst
--    ug gpts = do
--	(gp, qus) <- gpts
--	qu <- qus
--	return (Point gp qu, 1) -- is this being unnecessarily inefficient