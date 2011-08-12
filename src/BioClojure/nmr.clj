(ns BioClojure.nmr)


(def one-to-three
	{ "R" "ARG",
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
	"W" "TRP"
	})


(defn convert-sequence-123
  "Converts a string into a list of 3-letter amino acid names.  Needs refactoring:  assumes capital letters; if letter not found, mapping function returns nil; accepts string instead of list"
  [ string ]
  (let [convert (fn [c] (one-to-three (str c)))]
    (map convert string)))


;; repl example:
;; user=> (use `BioClojure.nmr)
;; user=> (convert-sequence-123 "ABCDEFGH")
;;	("ALA" nil "CYS" "ASP" "GLU" "PHE" "GLY" "HIS")
