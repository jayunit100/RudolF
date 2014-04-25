(ns ConnjurRV.readcyana
  (:use [clojure.contrib.str-utils2 :only (split-lines trim)])
  (:use clojure.contrib.generic.functor))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; module interface
;	parse-shifts
;	parse-sequence
;	merge-shifts
;	make-protein
;	make-protein-from-files
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; test:  input is contents of cyana shift file
;	output is list of length equal to number of lines in file
;	and each element is a 5-key map
;	value of :id should be unique across the list
;	:resid is positive
;	:atom is an atomname
;	:shift and :error are numeric
(defn parse-shifts
  "type :: String -> [Cyanaline]
   where Cyanaline: map with keys /id, shift, error, atom, resid/"
  [text]
  {:pre [(string? text)]}
  (let [headers [:id :shift :error :atom :resid]]
   (map #(zipmap headers (.split (trim %) " +")) ; on whitespace??
         (split-lines text))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; test: input is contents of cyana sequence file
;	output is list, length equal to number of lines
;	each element is one of the 20 3-letter abbreviations for an amino-acid type
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

; test: input: Sequence (output of parse-sequence)
;	output: Protein with as many residues as length of sequence
(defn sequence-to-protein
  "type :: Sequence -> Protein"
  [seq]
  ^{:type 'Protein}
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

; test: as many atoms as cyanalines are added
;	output is protein identical to input except for addition of atoms + atomdata
;		no additional residues
;		no midding residues
(defn merge-shifts
  "type :: Protein -> [Cyanaline] -> Protein
   add chemical shift data to a protein (which has only residues)"
  [prot shift-list]
  (reduce place-shift prot shift-list))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; test: input is result of parse-sequence, parse-shifts
;	output is a protein, which is a map with keys :residues
;	residues is a map with integer keys (all positive), values are residues
;	each residue is a map with keys :atoms and :aatype
;	the number of residues is equal to the length of the Sequence
;	the number of atoms is equal to the length of the second argument
(defn make-protein
  "Sequence -> [Cyanaline] -> Protein"
  [seq clines]
  {:post [(= (type %) 'Protein)]}
  (merge-shifts (sequence-to-protein seq) clines))

; test: input is path to sequence file, path to shift file
;	output is a protein
;	see 'make-protein' for more testable conditions
(defn make-protein-from-files
  "Filepath -> Filepath -> Protein"
  [seqpath shiftpath]
  {:post [(= (type %) 'Protein)]}
  (make-protein (parse-sequence (slurp seqpath))
                (parse-shifts (slurp shiftpath))))
