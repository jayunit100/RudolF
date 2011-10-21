
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
  (count-atoms-by-residue 
   (get-residues-map my-protein)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn shifts-on-structure
  "the shifts probably don't correspond to the structure"
  []
  (display-colored-struct 
   (load-pdb-struct "1TXR") 
   (color-map 
    (get-atomid-shifts 
     (get-atoms-map my-protein)))))

(defn norm-shifts-on-struct
  ""
  []
  (display-colored-struct
   (load-pdb-struct "1TXR")
   (color-map (normalized-shifts (get-atoms-map stats-protein)))))


(defn shifts-on-histogram
  ""
  []
  (make-histogram 
   (vals (get-atomid-shifts (get-atoms-map my-protein))) 
   "chemical shift"))

(defn norm-shifts-on-bar-chart 
  ""
  []
  (make-bar-chart 
   (normalized-shifts (get-atoms-map stats-protein)) 
   "atom-id" 
   "normalized chemical shifts" ))


( comment (defn number-of-shifts-avgs-on-double-bar-chart
  ""
  []
  (make-double-bar-chart 
   (number-of-shifts (get-residues-map stats-protein))
   "residue id"
   ["number of shifts" "number of averages"]))
)

(defn all-examples
  ""
  []
  (do
   (shifts-on-structure)
   (norm-shifts-on-struct)
   (shifts-on-histogram)
   (norm-shifts-on-bar-chart)))
   ;;(number-of-shifts-avgs-on-double-bar-chart)))

 
