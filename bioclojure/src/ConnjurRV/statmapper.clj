(ns ConnjurRV.statmapper
  (:use clojure.contrib.generic.functor)
  (:use [ConnjurRV.model :only (get-residues protein-to-atoms)]))

(defn protein-to-num-atoms-per-residue
  "Protein -> Map index Integer
  "
  [prot]
  (fmap (fn [res] (count (res :atoms))) 
        (get-residues prot)))

(defn protein-to-atomid-shifts
  "Protein -> Map atomid shift"
  [prot]
  (let [pairs (for [dict (protein-to-atoms prot)]
                   [(dict :id) (dict :shift)])]
   (into {} pairs)))
  

