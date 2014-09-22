import Data.List (intersperse)


data Box = Yes Int | No [Int] deriving (Show)


type Row = [Box]


type Sudoku = [Row]


sud1 :: [[Maybe Int]]
sud1 = [
  [Just 4, Nothing, Nothing, Nothing, Nothing, Nothing, Just 8, Nothing, Just 5],
  [Nothing, Just 3, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing],
  [Nothing, Nothing, Nothing, Just 7, Nothing, Nothing, Nothing, Nothing, Nothing],
  [Nothing, Just 2, Nothing, Nothing, Nothing, Nothing, Nothing, Just 6, Nothing],
  [Nothing, Nothing, Nothing, Nothing, Just 8, Nothing, Just 4, Nothing, Nothing],
  [Nothing, Nothing, Nothing, Nothing, Just 1, Nothing, Nothing, Nothing, Nothing],
  [Nothing, Nothing, Nothing, Just 6, Nothing, Just 3, Nothing, Just 7, Nothing],
  [Just 5, Nothing, Nothing, Just 2, Nothing, Nothing, Nothing, Nothing, Nothing],
  [Just 1, Nothing, Just 4, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing]]


maybesToSudoku :: [[Maybe Int]] -> Sudoku
maybesToSudoku = map f
  where
    f :: [Maybe Int] -> [Box]
    f = map g
    g :: Maybe Int -> Box
    g (Just x) = Yes x
    g Nothing = No [1..9] 


showBoard :: Sudoku -> String
showBoard sd = concat $ intersperse "\n" $ map stuff sd
  where
    stuff :: Row -> String
    stuff row = concat $ intersperse " " $ map f row
    f :: Box -> String
    f (Yes x) = show x
    f (No _) = "."


doIt = putStrLn . showBoard


isFinished :: Sudoku -> Bool
isFinished sd = not $ any q sd
  where
    q = any r
    r (No _) = False
    r _ = True 


nextGuess :: Sudoku -> Sudoku



{-

checkBoard :: Sudoku -> Either () ()


getColumns :: Sudoku -> [[Box]]


isRowOkay :: [Box] -> Bool
isRowOkay xs = 


getRows :: Sudoku -> [[Box]]


getSquares :: Sudoku -> [[[Box]]]

-}
-- strategy:
--   given a sudoku board,
--     1. find the first unassigned box.  if none, done
--     2. assign it one of the values
--     3. propagate the constraints (by removing possibilities)
--     4. check the board for rule violations
--     5. go to 1.