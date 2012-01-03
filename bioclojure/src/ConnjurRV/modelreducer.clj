(ns ConnjurRV.modelreducer
  (:use clojure.contrib.generic.functor))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; module interface
;	get-residues-map
;	get-residues-list
;	get-atoms-map
;	get-atoms-list


; protein
; residue
; atom
; atom data (shift, shift-diff, relaxation, etc.)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; for getting data into data model


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; for getting data out of data model

; test: keys are positive integers
;	number of entries equal to number of residues
;	vals are maps
(defn get-residues-map
  "Protein -> Map rindex Residue"
  [protein]
  (protein :residues))

; test: length equal to number of residues
;	each element is a map
;	there are keys :aatype and :rindex, at least (not nil)
(defn get-residues-list
  "Protein -> [Residueinfo]"
  [protein]
  (map (fn [[rindex rinfo]] (assoc rinfo :rindex rindex))
       (get-residues-map protein)))

; test: collection of maps
;	something about the size?
(defn get-atoms-list
  "Protein -> [Map atomkey atomval]"
  [prot]
  (let [anon-atoms (map #(:atoms (second %)) (get-residues-map prot))]
   (for [[name data] (apply concat anon-atoms)]
    (into data [[:atomname name]]))))

; doesn't remove :id from the dicts ....
; test: Map with keys positive integers
;	???
(defn get-atoms-map
  "Protein -> Map atomid (Map atomkey atomval)"
  [prot]
  (let [atom-infos (get-atoms-list prot)
        make-pair (fn [dict] [(dict :id) dict])]
   (into {} (map make-pair atom-infos))))

