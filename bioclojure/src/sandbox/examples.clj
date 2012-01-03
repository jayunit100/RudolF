(comment
(ns ConnjurRV.examples  
 (use ConnjurRV.vizcharts  
     ConnjurRV.vizstruct  
     ConnjurRV.modelreducer
     ConnjurRV.statistics
     ConnjurRV.readstats
     ConnjurRV.readpdb
     ConnjurRV.readcyana 
     clojure.contrib.generic.functor
     clojure.stacktrace))
)
;;This class imports everything.  It is just a sandbox class.
;;Use at your own risk.  To use namespace to invoke and import all methods
;;Call this function : 
;;user=> (doto 'ConnjurRV.examples require in-ns)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


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
   "normalized chemical shifts"))

(defn shifts-scatter-plot
  ""
  []
  (let [data (norm-shifts-avg-shifts (get-atoms-list stats-protein))]
   (do
    (make-scatter-plot
     data
     "assigned shift"
     "average shift")
    data)))

(defn atom-shifts-on-bar-chart
  "plot Map resid shift"
  [atomname]
  (let [func #(try (((% :atoms) atomname) :shift) (catch Exception e 0))
        resid-shifts (fmap func (get-residues-map my-protein))]
   (make-bar-chart 
    resid-shifts
    "residue number"
    (str atomname " shift"))))

(defn norm-shifts-on-bar-chart-by-atom
  ""
  [atomname]
  (let [func #(try (/ (((% :atoms) atomname) :shift)
                      (((% :atoms) atomname) :avg))
                   (catch Exception e 0))
        resid-shifts (fmap func (get-residues-map stats-protein))]
   (make-bar-chart 
    resid-shifts
    "residue number"
    (str "normalized " atomname " shift"))))

(defn avg-shifts-on-bar-chart
  ""
  [atomname]
  (let [func #(try (((% :atoms) atomname) :avg) (catch Exception e 0))
        resid-shifts (fmap func (get-residues-map stats-protein))]
   (make-bar-chart 
    resid-shifts
    "residue number"
    (str "average residue-specific " atomname " shift"))))   


(defn number-of-shifts-avgs-on-double-bar-chart
  ""
  []
;  (make-double-bar-chart 
;   (number-of-shifts (get-residues-map stats-protein))
;   "residue id"
;   ["number of shifts" "number of averages"]))
  )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn norm-shifts-all-aatypes
  ""
  [atomname]
  (let [get-shift #(try (((% :atoms) atomname) :shift) (catch Exception e 0))  ; get the shift of that atomtype for each residue (or 0 if nil)
        get-std-shift #(try (% :avg) (catch Exception e 0))
        atype-shifts (map (fn [[_ aadata]] (aadata atomname)) stats-data) ; all the standard shifts for the atomtype
        resid-shifts (fmap get-shift (get-residues-map my-protein))
        my-data (filter #(not (or (= 0 (first %)) (= 0 (second %))))
                 (apply concat (for [[_ shift] resid-shifts] ; ignore resid
                        (map (fn [std-shift] 
                                 [shift (get-std-shift std-shift)]) 
                             atype-shifts))))] ; pull out the chemical shifts as a (Map resid shift) -- for the atomtype
    (do
     (make-scatter-plot
      my-data
      "assigned shift"
      "standard shift possibility")
     [resid-shifts my-data])))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn all-examples
  ""
  []
  (do
   (shifts-on-structure)
   (norm-shifts-on-struct)
   (shifts-on-histogram)
   (norm-shifts-on-bar-chart)
   (shifts-scatter-plot)
   (atom-shifts-on-bar-chart "CA")
   (norm-shifts-on-bar-chart-by-atom "CA")
   (atom-shifts-on-bar-chart "HA")
   (norm-shifts-on-bar-chart-by-atom "HA")
   (avg-shifts-on-bar-chart "CA")
   (avg-shifts-on-bar-chart "HA")
   (number-of-shifts-avgs-on-double-bar-chart)))

(defn bar-charts
  "this example generates plots which show assigned shifts, normalized shifts, and average shifts of specific nuclei per residue"
  []
  (do
   (atom-shifts-on-bar-chart "CA")
   (norm-shifts-on-bar-chart-by-atom "CA")
   (atom-shifts-on-bar-chart "HA")
   (norm-shifts-on-bar-chart-by-atom "HA")
   (avg-shifts-on-bar-chart "CA")
   (avg-shifts-on-bar-chart "HA")))

 
