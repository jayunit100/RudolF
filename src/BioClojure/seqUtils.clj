(ns BioClojure.seqUtils
  (:use [clojure.pprint])
  (:import (org.apache.commons.lang StringUtils)))


(defn transform-entry
  "Transform a meta string into a map of metadata about a DNA entry."
  [entry]
  (let [meta-props (seq (-> (subs entry 1) ;; take off the leading ">"
                            (.split " ")))
        name (first meta-props)
        props (drop 1 meta-props)]
    (assoc (into {} (map #(vec (.split % "=")) props)) "name" name)))

(defn read-fasta-file
  "Read a FASTA file, transforming it into a map with :meta and :dna keys."
  [filename]
  (let [text (.trim (slurp filename))
        entries (-> text (.replaceAll "\r" "") (.split "\n\n") seq)]
    (map #(let [[m dna] (.split % "\n")]
            {:meta (transform-entry m) :dna dna})
         entries)))

(defn read-motif-file
  "Read a motif file, transforming it into a map with :meta and :dnamotif keys."
  [filename]
  (.split (.trim (slurp filename)) "\n"))

;;Create a random nucleotide, from a c g t.  Used to generate motifs randomly.
;;DO NOT Call this method by itself, it will go for infinity.
(defn rand-nuc "Returns a random nucleotide" []
  (repeatedly #(get ["a" "c" "g" "t"] (rand-int 4))))

;;Create a random nucleotide, from a c g t.  Used to generate motifs randomly.
;;DO NOT Call this method by itself, it will go for infinity.
(defn rand-aa "Returns a random amino" []
  (repeatedly #(get ["A" "Q" "W" "R" "T" "Y" "I" "P" "S"
                     "D" "F" "H" "K" "L" "C" "V" "N" "M"] (rand-int 18))))

;;Creates a DNA motif of random length
;;user=> (rand-motif-str 4)
;;("a" "c" "c" "c")
(defn rand-motif-str "Returns a random motif" [l]
  (take l (rand-nuc)))

;;Example usage
;;user=> (load-file "src/BioClojure/dnaUtils.clj")
;;user=> (use `BioClojure.dnaUtils)
;;user=> (rand-aa-str 3)
(defn rand-aa-str "Returns a random protein" [l]
  (take l (rand-aa)))


;;Define the 1-3 letter mapping.
(def one-to-three
  {"R" "ARG",
   "H" "HIS",
   "K" "LYS",
   "D" "ASP",
   "E" "GLU",
   "S" "SER",
   "T" "THR",
   "N" "ASN",
   "Q" "GLN",
   "C" "CYS",
   "U" "SEC", ;; interesting amino acid
   "G" "GLY",
   "P" "PRO",
   "A" "ALA",
   "V" "VAL",
   "I" "ILE",
   "L" "LEU",
   "M" "MET",
   "F" "PHE",
   "Y" "TYR",
   "W" "TRP"})

;;This demonstrates two important distinction in clojure
;;(1) Strings are "Sequences" : When we send "ABCDEFGH" as an argument to a "map" function,
;;     each character is individually sent to the map's function (in this case, "convert").
;;(2) In order to find a key (which is a string in this case) in the Map,
;;     the char must be converted into a string
;;(3) Clojure is dynamically, strongly typed -- source:  http://www.citerus.se/post/220609-from-java-to-clojure
(defn aa-to-3-letter
  "Converts a string into a list of 3-letter amino acid names.  Needs refactoring:  assumes capital letters; if letter not found, mapping function returns nil; accepts string instead of list"
  [string]
  (let [convert (fn [c] (one-to-three (str c)))]
    (map convert string)))


(defn read-and-print
  "Read a FASTA file and print it."
  [filename]
  (pprint (read-fasta-file filename)))

;; From the project directory:
;; lein repl
;; (use 'BioClojure.core)
;; (read-and-print "data/sample.fa")

;; 1 Identify motifs (M1 - Mx) present in DNA and their order
;; Confirm initiator motif and terminator motif and positions
;; Confirm proper insertion into two restriction sites.
;; Flag any incorrect assemblies and sequences with N positions
;; Report total number of minimotifs in clone
;; Generate a graph of overall structure, show linker regions that
;; glue motifs together and alignm
;; of motifs to sequences with labels.
;; 7. Report any 1 nucleotide mutations in DNA

