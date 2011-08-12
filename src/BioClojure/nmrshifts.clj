(ns BioClojure.nmrshifts
  (:use clojure.contrib.math))

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
  "get differences of input shift, average shift for various Carbon nucleus types as a sorted list of pairs"
  [ shift ]
  (let [diffs (for [[atom avgshift] approx-c-shifts] 	; computer difference between shift and average shift for each nucleus type
                (list atom (abs (- shift avgshift))))]
    (sort-by #(second %) diffs))) 	; return a sorted list (by shift difference)