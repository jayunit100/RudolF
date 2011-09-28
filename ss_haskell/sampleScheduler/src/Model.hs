module Model (
	Schedule, 
	getPoints,		

	Quadrature(R, I), 

	Point, 
	gridPoint, 
	quadUnit,
	QuadUnit,
	GridPoint,
	makePoint,

	makeSchedule, 
	newSchedule,
	addPoints,

) where

import qualified Data.Map as M
import Data.List (genericLength, foldl', sort)
import Data.Monoid (mempty, mappend, Monoid)
import Data.Function (on)


--------------------------------------------------

data Quadrature =  R | I  deriving (Show, Eq, Ord, Enum, Bounded, Read)

type GridPoint = [Integer]

type QuadUnit = [Quadrature]

data Point = Point { gridPoint :: GridPoint, 
			quadUnit :: QuadUnit }  deriving  (Show, Eq, Ord)

data Schedule = Schedule {getPoints :: [Point]}  deriving (Show)

-- a schedule is basically a multi-set of points, so equality doesn't depend on order
-- however, it's important that none of the client code knows how a schedule is implemented
instance Eq Schedule where
  (==) = on (==) (sort . getPoints)         -- is this too fancy????
--  (Schedule lpts) == (Schedule rpts) = sort rpts == sort lpts

--------------------------------------------------

pointDims :: (Integral t) => Point -> t
pointDims (Point gp _) = genericLength gp

makePoint :: GridPoint -> QuadUnit -> Point
makePoint gp ph
	| length gp == length ph = Point gp ph
	| otherwise = error ("gridpoint and quadUnit dimensions don't match -- gp" ++ show (length gp) ++ ", quadUnit " ++ show (length ph))

--------------------------------------------------

addPoints :: Schedule -> [Point] -> Schedule
addPoints = foldl' addPoint

-- point to be added must have same dimensionality as schedule
addPoint :: Schedule -> Point -> Schedule
addPoint (Schedule []) newpt = Schedule [newpt]
addPoint (Schedule opts@(pt:pts)) newpt
	| pointDims newpt /= pointDims pt = error ("bad number of dimensions in point -- wanted " ++ show (pointDims pt) ++ ", got " ++ show (pointDims newpt))
	| otherwise = Schedule (newpt : opts)

--------------------------------------------------
instance Monoid Schedule where
  mempty = Schedule []
  mappend l r = foldl' addPoint l (getPoints r)

-- create a schedule from a list of points
newSchedule :: [Point] -> Schedule
newSchedule = addPoints mempty

-- assumptions:
--	all GridPoints have the same dimensionality (as each other)
--	the quadrature generator produces quads of the same dimensionality (as the GridPoints)
--	there is at least one grid point (this is used to define the dimensionality)
makeSchedule :: [GridPoint] ->  ([GridPoint] -> [(GridPoint, QuadUnit)])  -> Schedule
makeSchedule gps quadgen = newSchedule points
	where
		points = map (uncurry makePoint) quad_gps
		quad_gps = quadgen gps



