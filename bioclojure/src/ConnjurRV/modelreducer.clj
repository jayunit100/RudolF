(ns ConnjurRV.modelreducer
  (:use clojure.contrib.generic.functor))

; protein
; residue
; atom
; atom data (shift, shift-diff, relaxation, etc.)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; for getting data into data model


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; for getting data out of data model

(defn get-residues
  "Protein -> Map rindex Residue"
  [protein]
  (protein :residues))

(defn protein-to-atoms
  "Protein -> [Map atomkey atomval]"
  [prot]
  (let [anon-atoms (map #(:atoms (second %)) (get-residues prot))]
   (for [[name data] (apply concat anon-atoms)]
    (into data [[:atomname name]]))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; for getting data into specific data structures

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

(defn protein-to-atomid-map
  "Protein -> Map atomid (Map atomkey atomval)"
  [prot]
  (let [pairs (for [dict (protein-to-atoms prot)]
                   [(dict :id) {:shift (dict :shift)
                                :avg (dict :avg)}])]
   (into {} pairs)))
