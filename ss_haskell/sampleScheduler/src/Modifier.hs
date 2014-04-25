module Modifier (
	blurred
) where


import Model
import qualified Random as R


-- 'bump' every point in every dimension a random number of units (from -width to +width)
--	currently bumps only the first dimension
--  	points can end up outside any implicit 'bounds' -- since schedules have no concept of bounds 
-- 	duplicates can occur (by gridpoints) -- thus there is a function for adding a list of points into a schedule
--		this should maybe be a function provided by the 'Model' module
--	transients are basically ignored -- ([1,1] RR, 18) is treated the same as ([1,1] RR, 1) -- all transients are 'bumped' together
blurred2 :: Integer -> Int -> Schedule -> Schedule
blurred2 width seed sched = newSchedule bumpedPts
  where
    bumpedPts = map bumpPt $ zip randomNums $ getPoints sched 	-- there may now be multiple points with the same coordinates
    bumpPt (bp, pt) = makePoint (bump (gridPoint pt) bp) (quadUnit pt)
    bump (c:cs) bp = (c + bp) : cs
		-- this should be refactored to modify each of the coordinates with a separate bump function
		-- this should also check to make sure that ?? none of the points move outside of the allowed area ??
    randomNums = map (((-) width) . abs . (flip mod (2 * width + 1))) $ R.randoms (R.mkStdGen seed)


blurred :: Integer -> Int -> Schedule -> Schedule
blurred w s sched = newSchedule bumpedPts
  where
    bumpedPts = do
      pt <- zip (getPoints sched) randomNums
      return $ bump pt
    bump (pt, r) = makePoint (map (+r) $ gridPoint pt) (quadUnit pt)
    randomNums = map (((-) w) . abs . (flip mod (2 * w + 1))) $ R.randoms (R.mkStdGen s)
