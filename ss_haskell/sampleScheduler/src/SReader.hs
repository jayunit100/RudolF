module SReader (
	parseJson
) where


import Model
import Control.Applicative
import qualified Text.RJson as R
import qualified Data.Map as M


--parseJson :: String -> Either String Schedule
parseJson2 str = thing $ R.parseJsonString str
  where
    thing x = fmap readJson x
--    thing (Left e) = Left e
--    thing (Right s) = readJson s
parseJson str = do
	json <- R.parseJsonString str
	parsed <- readJson json
	return parsed

readJson :: R.JsonData -> Either String Schedule
readJson text = do
	mpoints <- jpoints text
	return $ newSchedule mpoints

jpoints :: R.JsonData -> Either String [Point]
jpoints (R.JDObject mymap) = do
	(R.JDArray jpts) <- mlookup "points" mymap
	doStuff $ map jpoint jpts
jpoints x = Left ("bad JSON structure: wanted object, got " ++ show x)

doStuff :: [Either String Point] -> Either String [Point]
doStuff = foldr comb (Right [])
  where
    comb (Left err) _ = Left err
    comb _ (Left err) = Left err
    comb (Right pt) (Right pts) = Right $ pt : pts

jpoint :: R.JsonData -> Either String Point
jpoint (R.JDObject mymap) = do
	(R.JDArray coords) <- mlookup "gridPoint" mymap
	(R.JDArray qunit) <- mlookup "quadratureUnit" mymap
	return $ makePoint (cs coords) (qu qunit)
  where
    cs = map (\(R.JDNumber n) -> floor n)
    qu = map (\(R.JDString s) -> read s)

mlookup :: (Show k, Ord k) => k -> M.Map k a -> Either String a
mlookup k m = 
  let res = M.lookup k m
  in case res of 
    (Just r) -> return r
    Nothing -> fail $ concat ["couldn't find key<", show k, ">"]

ex1 = "{\"points\": [{\"gridPoint\": [22], \"quadratureUnit\": [\"R\"]}]}"

