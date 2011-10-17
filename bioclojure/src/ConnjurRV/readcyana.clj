(ns BioClojure.connjurRV
  (:use [clojure.contrib.str-utils2 :only (split-lines trim)])
  (:use clojure.contrib.generic.functor))


(defn parse-shift-file
  "type :: Filepath (as a String) -> [Cyanaline]"
  [filename]
  (parse-string (slurp filename)))

(defn parse-shifts
  "type :: String -> [Cyanaline]
   
   Cyanaline: map where keys are /id, shift, error, atom, resid/"
  [string]
  (let [headers [:id :shift :error :atom :resid]]
   (map #(zipmap headers (.split (trim %) " +")) 
         (split-lines string))))


(defn parse-sequence
  "type:  String -> Map index aatype

   input: cyana-formatted sequence string -- residues separted by newlines
   output: map -- key is index, value is amino acid type
     length is same as number of lines in input string"
  [string]
  (zipmap (map #(str (+ % 1)) (range)) ; need indices to be 1-indexed
          ;; should refactor to remove 'str' .... requires other changes, too
          (split-lines (.trim string))))


;; refactor this to seq-to-protein (need to add chain)
(defn seq-to-residues
  "Map index aatype -> Map index residue
   
   input: map of index to aatype
   output: map of index to residue"
  [seqmap]
  (fmap (fn [aatype] {:aatype aatype :atoms {}}) 
        seqmap)))