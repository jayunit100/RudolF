module Quadrature (
	allQuadUnits,
	justReals,
	singleRandom
)  where

import Model
import Control.Applicative
import qualified Random as R




allQuadUnits = ctxtFreeQuad allquadunits

justReals = ctxtFreeQuad justreals


--------------------------------------------------

-- the state needs to be maintained over the various invocations for each point
singleRandom :: Int -> [GridPoint] -> [(GridPoint, QuadUnit)]
singleRandom sd pts = fmap (fmap (\r -> quads !! r)) gps_rands		-- pretty ugly (fmap fmap)
  where
    gps_rands = zip pts randnums		-- pair a gridpoint with a random number
    randnums = map (flip mod (length quads) . abs) $ R.randoms (R.mkStdGen sd)		-- generate random numbers which can be used as indexes into 'quads'
    quads = allquadunits (head pts)		-- bad: pulls off first point to get the length, to determine how many quadrature components need to be generated (??REFACTOR??)

--------------------------------------------------

ctxtFreeQuad :: (GridPoint -> [QuadUnit]) -> [GridPoint] -> [(GridPoint, QuadUnit)]
ctxtFreeQuad qfunc gpoints = concatMap someFunc gpoints -- someFunc takes a gridpt, and produces a list of tuples
  where
    someFunc gp = map ((,) gp) $ qfunc gp

----------------

allquadunits :: GridPoint -> [QuadUnit]
allquadunits pt = sequence $ take (length pt) $ repeat [R, I]

justreals :: GridPoint -> [QuadUnit]
justreals pt = [take (length pt) $ repeat R]

--------------------------------------------------
-- unused:
--twoquads2d = ctxtFreeQuad twoquads2dp

--twoquads2dp :: GridPoint -> [QuadUnit]
--twoquads2dp _ = [[R, I], [I, R]]

