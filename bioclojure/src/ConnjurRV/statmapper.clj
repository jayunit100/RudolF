(ns ConnjurRV.statmapper
  (:use clojure.contrib.generic.functor))

(defn protein-to-num-atoms-per-residue
  "Protein -> Map index Integer
  "
  [prot]
  (fmap (fn [res] (count (res :atoms))) prot))

  		

