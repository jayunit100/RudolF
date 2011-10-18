(ns ConnjurRV.dataplot
  (:use clojure.contrib.generic.functor)
  (:use [ConnjurRV.readcyana :only (make-protein-from-files)])
  (:use [ConnjurRV.statmapper :only (protein-to-num-atoms-per-residue)]);)
  (:use [incanter.charts :only (histogram)])
  (:use [incanter.core :only (view)]))

(def example-stats 
  (protein-to-num-atoms-per-residue 
   (make-protein-from-files "data/connjur.seq" "data/connjur.prot")))

(defn hist 
  "test function -- displays histogram.  
   todos:  figure out how to control the x-axis label
	figure out how to change the x-values, or how they're generated"
  []
  (view
   (histogram (vals example-stats))))



