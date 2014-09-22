import Control.Monad (guard)
import Data.List (nub)


allDifferent :: Eq a => [a] -> Bool
allDifferent xs = length xs == (length $ nub xs)


allSame :: Eq a => [a] -> Bool
allSame xs = (length $ nub xs) == 1


-- a b c
-- d e f
-- g h i
square :: [[[Integer]]]
square = do 
  let ns = [1..9]
  a <- ns
  b <- ns
  c <- ns
--  guard (allDifferent [a, b, c] && (sum [a,b,c] == 15))
  d <- ns
  e <- ns
  f <- ns
--  guard (allDifferent [a, b, c, d, e, f])
--  guard (allSame [sum [a, b, c], sum [d, e, f]])
  g <- ns
  h <- ns
  i <- ns
  guard (allDifferent [a, b, c, d, e, f, g, h, i])
  guard (allSame [a + b + c, d + e + f, g + h + i, 
                  a + d + g, b + e + h, c + f + i,
                  a + e + i, c + e + g])
  return [[a,b,c], [d,e,f], [g,h,i]]
  
  
showIt :: Show a => [[a]] -> String
showIt xs = foldl (\b n -> b ++ "\n" ++ n) "" $ map show xs