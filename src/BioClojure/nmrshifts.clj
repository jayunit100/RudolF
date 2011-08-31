(ns BioClojure.nmrshifts
  (:use clojure.contrib.math)
  (:use clojure.set))

(def approx-c-shifts
  {"CA" 58,
   "THR-CB" 68,
   "CB" 32,
   "SER-CB" 63,
   "GLY-CA" 44,
   "ALA-CB" 18,
   "TYR-CB" 39,
   "ILE-CD" 13,
   "CG" 25})

(defn get-shift-differences
  "get differences of input shift, average shift for various Carbon nucleus
  types as a sorted list of pairs"
  [shift]
  ;; computer difference between shift and average shift for each nucleus type
  (let [diffs (for [[atom avgshift] approx-c-shifts] 
                (list atom (abs (- shift avgshift))))]
    ;; return a sorted list (by shift difference)
    (sort-by second diffs)))



(def avg-aa-shifts ; ignoring helix/sheet/coil for now -- would be great to push this data into a DB
  {"ALA" {"H" 8.20, "HA" 4.26, "HB" 1.35, "C" 177.69, "CA" 53.13, "CB" 19.12, "N" 123.32},
;   "ARG" {}, ;; TODO -- fill these in
   "ASN" {"H" 8.36, "HA" 4.67, "HB2" 2.81, "HB3" 2.75, "HD21" 7.35, "HD22" 7.13, "C" 175.20, 
     "CA" 53.52, "CB" 38.71, "CG" 176.56, "N" 118.94, "ND2" 112.73},
   "ASP" {"H" 8.31, "HA" 4.60, "HB2" 2.73, "HB3" 2.67, "HD2" 5.78, "C" 176.38, "CA" 54.68,
     "CB" 40.92, "CG" 178.13, "N" 120.67, "OD1" 179.66},
   "CYS" {"H" 8.39, "HA" 4.70, "HB2" 3.28, "HB3" 3.18, "HG" 10.67, "C" 174.79, "CA" 58.19,
     "CB" 32.80, "N" 120.84},
   "GLN" {},
   "GLU" {},
   "GLY" {"H" 8.33, "HA2" 3.97, "HA3" 3.89, "C" 173.88, "CA" 45.37, "N" 109.89},
   "HIS" {},
   "ILE" {},
   "LEU" {},
   "LYS" {},
   "MET" {},
   "PHE" {},
   "PRO" {},
   "SER" {},
   "THR" {},
   "TRP" {},
   "TYR" {},
   "VAL" {"H" 8.29, "HA" 4.18, "HB" 1.97, "HG1" 0.82, "HG2" 0.80, "C" 175.60, "CA" 62.48,
     "CB" 32.77, "CG1" 21.52, "CG2" 21.33, "N" 121.36}})



(defn compare-atom
  "Return difference if both ss- and aa-shift defined.  
   Otherwise return which of the two is not defined.  
   Result is meaningless if both are not defined."
  [ss-shift aa-shift]
  (cond
   (= nil ss-shift) 'not-in-ss
   (= nil aa-shift) 'not-in-aa
   true (- ss-shift aa-shift)))


(defn compare-ss-to-aa
  "for each atom in spin system:
     compare to amino acid's atom (of same name)
   return:  map -- key is atom, value is comparison result"
  [ss aa-atoms]
  (let [all-atoms (union (set (keys aa-atoms)) 
			 (set (keys ss)))]
   (into {} (for [atom all-atoms]
                 [atom (compare-atom (ss atom) (aa-atoms atom))]))))


; (compare-ss-to-aas {"H" 3.32 "CA" 55})
(defn compare-ss-to-aas
  "for each amino acid:
     compare spin system to amino acid
   return:  map -- key is amino acid, value is comparison"
  [ss]
  (into {} (for [[aa atoms] avg-aa-shifts]
                [aa (compare-ss-to-aa ss atoms)])))

(defn best-match
  ""
  [comparison]
  ;; need to do absolute value, and need to filter out non-numeric values
  ;; Lee: I believe I did this, but keeping your comment here just in case
  (->> comparison
       ;; Filter out ones that aren't float-able
       (filter #(try (float (val %)) (catch Exception _ nil)))
       ;; Sort by smallest absolute value number 
       (sort-by (comp abs val))
       ;; First is the best match
       first))

(defn worst-match
  ""
  [comparison]
  (let [key-fn (fn [[_ v]] v)]
   (first 
     (reverse 
       (sort-by key-fn comparison)))))

(defn rank-comparison
  ""
  [comparisons algorithm]
  (sort-by (fn [[_ v]] (algorithm v)) comparisons))


