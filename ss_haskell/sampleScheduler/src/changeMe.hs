
import Control.Applicative


-- example:
-- ghci> uniformGrid [(1,5), (8, 10)]
-- 
uniformGrid :: (Enum t) => [(t, t)] -> [[t]]
uniformGrid bounds = sequence ranges
  where
    ranges = map (\(l, h) -> [l .. h]) bounds