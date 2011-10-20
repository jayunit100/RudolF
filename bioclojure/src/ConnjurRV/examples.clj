(ns ConnjurRV.examples  
  (:use ConnjurRV.readcyana) 
  (:use ConnjurRV.vizcharts  )
  (:use ConnjurRV.structdisplay)  
  (:use ConnjurRV.modelreducer)
  (:use ConnjurRV.statistics)
  (:use ConnjurRV.bmrbstats)
  (:use clojure.contrib.generic.functor))


;;;;; THIS CLASS IS SANDBOX CODE ;;;;;;;;;;;;;;;;;;;

(def my-protein (make-protein-from-files "data/connjur.seq" "data/connjur.prot"))

(def stats-protein
  (merge-bmrb my-protein))

(def example-stats 
  (protein-to-num-atoms-per-residue my-protein))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn shifts-on-structure
  "the shifts probably don't correspond to the structure"
  []
  (display-colored-struct 
   (load-pdb-struct "1TXR") 
   (color-map (protein-to-atomid-shifts my-protein))))

(defn norm-shifts-on-struct
  ""
  []
  (display-colored-struct
   (load-pdb-struct "1TXR")
   (color-map (normalized-shifts (protein-to-atomid-map stats-protein)))))


(defn shifts-on-histogram
  ""
  []
  (make-histogram (vals (protein-to-atomid-shifts my-protein)) "a"))

(defn norm-shifts-on-bar-chart 
 ""
 []
 (make-bar-chart (normalized-shifts (protein-to-atomid-map stats-protein)) "atom-id" "normalized chemical shifts" ))
 
