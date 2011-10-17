(ns ConnjurRV.readcyana
  (:use [clojure.contrib.str-utils2 :only (split-lines trim)])
  (:use clojure.contrib.generic.functor))


(defn parse-shifts
  "type :: String -> [Cyanaline]
   
   Cyanaline: map where keys are /id, shift, error, atom, resid/"
  [string]
  (let [headers [:id :shift :error :atom :resid]]
   (map #(zipmap headers (.split (trim %) " +")) 
         (split-lines string))))


(defn parse-shift-file
  "type :: Filepath (as a String) -> [Cyanaline]"
  [filename]
  (parse-shifts (slurp filename)))




(defn make-empty-residue 
  ""
  [aatype]
  {:aatype aatype :atoms {}})

(defn parse-sequence
  "type :: String -> Protein
   where Protein :: Map index aatype

   input: cyana-formatted sequence string -- residues separted by newlines
   output: map -- key is index, value is amino acid type
     length is same as number of lines in input string"
  [string]
  (fmap make-empty-residue 
        (zipmap (map #(+ % 1) (range)) ; need indices to be 1-indexed
                (split-lines (.trim string)))))


(defn parse-sequence-file
  "type :: Filepath (as a String) -> Protein"
  [filename]
   (parse-sequence
    (slurp filename)))

