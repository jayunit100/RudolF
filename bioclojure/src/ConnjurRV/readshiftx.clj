(ns ConnjurRV.readshiftx
  (:use [clojure.contrib.str-utils2 :only (split-lines)]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; parse-shiftx
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn convert-types
  ""
  [shiftx-map]
  {:resid (Integer/parseInt (shiftx-map :resid))
   :shift (Float/parseFloat (shiftx-map :shift))
   :aatype (shiftx-map :aatype)
   :atomname (shiftx-map :atomname)})


(defn parse-shiftx
  "String -> [ShiftxLine]"
  [string]
  (let [lines (split-lines string)
        header (first lines) ; ignore header ??
        headings [:resid :aatype :atomname :shift]]
   (for [line (rest lines)] ; continue with the second line
    (convert-types 
     (zipmap headings (.split line ","))))))

(def one-to-three
  {"R" "ARG"
   "H" "HIS"
   "K" "LYS"
   "D" "ASP"
   "E" "GLU"
   "S" "SER"
   "T" "THR"
   "N" "ASN"
   "Q" "GLN"
   "C" "CYS"
   "U" "SEC" ;; interesting amino acid
   "G" "GLY"
   "P" "PRO"
   "A" "ALA"
   "V" "VAL"
   "I" "ILE"
   "L" "LEU"
   "M" "MET"
   "F" "PHE"
   "Y" "TYR"
   "W" "TRP"
   "B" "TRP"}) ; just because I feel like (B is not real)

(defn semantic-shiftx
  "[ShiftxLine] -> Protein"
  [sx-lines]
  (let [residues (reduce #(assoc-in %1 [(%2 :resid) :aatype] (one-to-three (%2 :aatype)))
                         {} 
                         sx-lines)]
   {:name nil   ;; ?? are these the right keys??
    :source nil ;;
    :residues (reduce #(assoc-in %1 [(%2 :resid) :atoms (%2 :atomname) :shift] (%2 :shift)) 
                      residues 
                      sx-lines)})) ; what about aatype?
