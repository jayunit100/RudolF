(ns ConnjurRV.readcyana
  (:use [clojure.contrib.str-utils2 :only (split-lines trim)])
  (:use clojure.contrib.generic.functor))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn parse-shifts
  "type :: String -> [Cyanaline]
   where Cyanaline: map where keys are /id, shift, error, atom, resid/

   test:  "
  [string]
  (let [headers [:id :shift :error :atom :resid]]
   (map #(zipmap headers (.split (trim %) " +")) ; on whitespace??
         (split-lines string))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn parse-sequence
  "type :: String -> Sequence
   where Sequence :: [aatype]"
  [string]
  (split-lines (.trim string)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn make-empty-residue 
  "type :: aatype -> Residue"
  [aatype]
  {:aatype aatype :atoms {}})

(defn sequence-to-protein
  "type :: Sequence -> Protein
   where Protein :: Map index aatype"
  [seq]
  {:residues (fmap make-empty-residue 
                   (zipmap (map #(+ % 1) (range)) ; need indices to be 1-indexed
                           seq))
   :name nil
   :source nil})

(defn place-shift
  "Protein -> Cyanaline -> Protein
   adds an atom and associated data to a protein"
  [prot shift-map]
  (let [resid (shift-map :resid)
        atomname (shift-map :atom)
        shift (shift-map :shift)
        atomid (shift-map :id)]
   (assoc-in prot 
             [:residues (Integer/parseInt resid) :atoms atomname] 
             {:shift (Float/parseFloat shift) :id (Integer/parseInt atomid)})))

(defn merge-shifts
  "type :: Protein -> [Cyanaline] -> Protein
   add chemical shift data to a protein (which has only residues)"
  [prot shift-list]
  (reduce place-shift prot shift-list))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn make-protein
  "Sequence -> [Cyanaline] -> Protein"
  [seq clines]
  (merge-shifts (sequence-to-protein seq) clines))

(defn make-protein-from-files
  "Filepath -> Filepath -> Protein"
  [seqpath shiftpath]
  (make-protein (parse-sequence (slurp seqpath))
                (parse-shifts (slurp shiftpath))))
