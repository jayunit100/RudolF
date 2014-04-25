module GridPoints (
	uniformGrid,
	allLowerBounds,
	firstPoint,
	lastPoint,
	halton
) where

import Model
import qualified Data.List as L
import qualified Control.Applicative as A
import qualified Data.Set as S


-- randomGrid2d :: (Integer, Integer) -> (Integer, Integer) -> Integer -> [GridPoint]
-- randomGrid2d (xl, xh) (yl, yh) totalpoints = random number generator ...

uniformGrid :: (Enum t) => [(t, t)] -> [[t]]
uniformGrid bounds = sequence ranges
  where
    ranges = map (\(l, h) -> [l .. h]) bounds

allLowerBounds :: (Integral t) => [(t, t)] -> [[t]]
allLowerBounds bounds = filter onLowerEdge gridpoints
  where
    gridpoints = uniformGrid bounds
    onLowerEdge pt = any (\((l, _), c) -> l == c) (zip bounds pt)    -- can this be refactored (to use zipWith or as a ZipList?)
                                                                     --           or maybe use the 'Any' monoid (LYAH Ch 11)

firstPoint :: (Integral t) => [(t, t)] -> [[t]]
firstPoint bounds = [map fst bounds]

lastPoint :: (Integral t) => [(t, t)] -> [[t]]
lastPoint bounds = [map snd bounds]


-- Int in type signature
-- if there are more than n dimensions in the bounds (n is currently 10 -- the number of primes) -- bad things will happen
-- does it hit no more than the maximum, no less than the minimum?  it looks like it, but there should be unit tests
-- if you tell it to take lots of points, and there aren't that many available .... infinite loop (say, 500 from [(1,5)])
halton :: (Integral t) => [(t, t)] -> Int -> [[t]]
halton bounds num = S.toList $ foldWhile (\s -> S.size s < num) S.insert S.empty scaledPoints
  where
    scaledPoints = map scalePoint points                                                 -- scale the numbers so they fall between the bounds (inclusive!)
    scalePoint cs = A.getZipList $ (\sf l c -> (floor $ c * sf) + l) A.<$> A.ZipList scalingFactors A.<*> (A.ZipList $ map fst bounds) A.<*> A.ZipList cs
    scalingFactors = map (\(l, h) -> fromIntegral (h - l + 1)) bounds                    -- is the (+1) correct?  it seems that 'floor' always knocks it down a notch, so ... yes?
    haltonNums = map (\p -> map (haltonNumber p) [0..]) $ take (length bounds) primes    -- associate a prime with each dimension;  generate halton numbers as infinite lists
    points = L.transpose haltonNums                                                      -- combine the numbers from the dimension lists to form points
    primes = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29]
    
haltonNumber :: Integer -> Integer -> Double
haltonNumber b i = go 0 (1 / db) i                   -- b is usually a prime; i is the index into the series
  where
    go :: Double -> Double -> Integer -> Double
    go r _ 0 = r                                     -- can it be less than 0?
    go r f i = go newr (f / db) newi
      where
        newr = r + f * (fromInteger $ mod i b)
        newi = floor $ fromInteger i / db
    db = fromInteger b

foldWhile :: (b -> Bool) -> (a -> b -> b) -> b -> [a] -> b
foldWhile pred comb base things = go base things
  where
    go b [] = b
    go b (t:ts) 
      | pred b = go (comb t b) ts
      | otherwise = b


--concentricShell :: (Integral t, Floating t1) => [(t,t)] -> t1 -> t1 -> [[t]]
concentricShell bounds spacing maxdev = filter close $ uniformGrid bounds
  where
    close point = mydist point <= maxdev                  -- if the point is close to one of the shells (maxdev is a distance)
    mydist pt = abs (ratio - (fromInteger $ round ratio)) * spacing     -- measuring closeness:  difference between 1) distance from the origin divided by spacing and 2) the nearest integer, multiplied by spacing
      where
        ratio = distance / spacing
        distance = sqrt $ sum $ map ((**2) . fromIntegral) pt

