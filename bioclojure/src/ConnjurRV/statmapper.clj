(ns ConnjurRV.statmapper
  (:use clojure.contrib.generic.functor)
  (:use [ConnjurRV.readcyana :only (make-protein-from-files)]))


(defn protein-to-num-atoms-per-residue
  "Protein -> Map index Integer
  "
  [prot]
  (fmap (fn [res] (count (res :atoms))) prot))


(def example-stats 
  (protein-to-num-atoms-per-residue 
   (make-protein-from-files "data/connjur.seq" "data/connjur.prot")))