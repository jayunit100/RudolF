
(use 'ConnjurRV.vizcharts  
     'ConnjurRV.vizstruct  
     'ConnjurRV.modelreducer
     'ConnjurRV.statistics
     'ConnjurRV.readstats
     'ConnjurRV.readpdb
     'ConnjurRV.readcyana 
     'clojure.contrib.generic.functor
     'clojure.stacktrace)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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
  (make-histogram (vals (protein-to-atomid-shifts my-protein)) "chemical shift"))

(defn norm-shifts-on-bar-chart 
 ""
 []
 (make-bar-chart (normalized-shifts (protein-to-atomid-map stats-protein)) "atom-id" "normalized chemical shifts" ))


(defn number-of-shifts-avgs-on-double-bar-chart
  ""
  []
  (make-double-bar-chart 
   (number-of-shifts (get-residues stats-protein))
   "residue id"
   ["number of shifts" "number of averages"]))

(defn all-examples
  ""
  []
  (do
   (shifts-on-structure)
   (norm-shifts-on-struct)
   (shifts-on-histogram)
   (norm-shifts-on-bar-chart)
   (number-of-shifts-avgs-on-double-bar-chart)))

 
