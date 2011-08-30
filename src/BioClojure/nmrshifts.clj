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
    (sort-by #(second %) diffs)))



(def avg-aa-shifts
  {"ALA" {"H" 8.20, "HA" 4.26, "HB" 1.35, "C" 177.69, "CA" 53.13, "CB" 19.12, "N" 123.32},
;   "ARG" {}, ;; TODO -- fill these in
;   "ASN" {},
;   "ASP" {},
;   "CYS" {},
;   "GLN" {},
;   "GLU" {},
   "GLY" {"H" 8.33, "HA2" 3.97, "HA3" 3.89, "C" 173.88, "CA" 45.37, "N" 109.89},
   "HIS" {},
   "ILE" {}})



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


