(ns BioClojure.vennnmr
  (:use [clojure.contrib.str-utils2 :only (split-lines trim)])
  (:use clojure.contrib.generic.functor))

;; @Matt:  suggested refactorings:
;; refactor and post data model -- make sure all code uses it
;; clarify, post general procedure for merging these data (sequence, shifts, shift-stats)
;; clojure-idiomatize code


(defn parse-bmrb-stats
  "output: list of maps
     each map: keys are /Res, Name, Atom, Count, Min. Max., Avg., StdDev/"
  [string]
  (let [lines (split-lines string)
        headings (.split (first lines) " +")
        atomlines (rest lines)]
   (for [atomline atomlines]
    (zipmap headings (.split atomline " +")))))

(defn transform-stats
  "input: list of maps based on bmrb shift statistics file
   output: map of keys -- amino acid type names, values maps of keys -- atom names, values maps of -- stats about that atom in that amino acid (avg shift, std dev, etc.)"
  [shift-stats]
  (let [f (fn [base linemap] 
              (assoc-in base 
                        [(linemap "Res") (linemap "Name")] 
                        {:avg (Float/parseFloat (linemap "Avg.")) :stddev (Float/parseFloat (linemap "StdDev"))}))] ;; refactor the return value ....
   (reduce f {} shift-stats))) 

(defn parse-sequence
  "input: cyana-formatted sequence string -- residues separted by newlines
   output: map -- key is index, value is amino acid type
     length is same as number of lines in input string"
  [string]
  (zipmap (map #(str (+ % 1)) (range)) ; need indices to be 1-indexed
          ;; should refactor to remove 'str' .... requires other changes, too
          (split-lines (.trim string))))

(defn parse-shifts
  "output: list of maps
     each map:  keys are /id, shift, error, atom, resid/"
  [string]
  (let [headers [:id :shift :error :atom :resid]]
   (map #(zipmap headers (.split (trim %) " +")) 
         (split-lines string))))

(defn seq-to-protein
  "input: map of index to aatype
   output: map of index to residue"
  [seqmap]
  (let [func (fn [aatype] {:aatype aatype :atoms {}})]
   (fmap func seqmap)))

(defn place-shift
  "input: a protein (map) and a cyana line map
   output:  a protein with an added atom"
  [prot shift-map]
  (let [resid (shift-map :resid)
        atomname (shift-map :atom)
        shift (shift-map :shift)
        atomid (shift-map :id)]
   (assoc-in prot [resid :atoms atomname] {:shift (Float/parseFloat shift) :id (Integer/parseInt atomid)})))

(defn merge-shifts
  "input: protein, list of maps of header to value"
  [prot shift-list]
  (reduce place-shift prot shift-list))

(defn get-stats
  "input:  ???
   output: "
  [stats atomname]
  (let [atomstats (stats atomname)]
   (if atomstats 
       (atomstats :avg)
       'no-stats-for-atom)))

(defn place-stats
  "input: 1) atoms, keys are atom names, values are maps
          2) average shifts map (specific to that residue) -- keys: atom names, values: maps of info about that atom
   output: augmented atoms"
  [atoms stats]
    ;; for each atom
    ;; find the standard data in stats
    ;; put that into the atom's map
  (into {} (for [[atomname atommap] atoms]
                [atomname (assoc-in atommap [:avg] (get-stats stats atomname))]))) ;; but what does this do if (stats atomname) is nil????

(defn merge-bmrb
  "input: bmrb stats, protein with chemical shifts of assigned atoms
   output: protein with bmrb stats for each atom that initially had an assignment"
  [prot avg-shifts]
    ;; for each residue in the protein
    ;; find the matching "standard residue" in the average stats
    ;;      use the aatype of the residue to look up the stats
  (let [shift-adder 
        (fn [res] ;;avg-shifts)] 
         (assoc-in res [:atoms] (place-stats (res :atoms) (avg-shifts (res :aatype))) ))]
   (fmap shift-adder prot)))

(defn venn-nmr-help
  [seqstr shiftstr bmrbstr]
  (let [seq (parse-sequence seqstr)               ;; parse the sequence string (comments need refactoring)
        shifts (parse-shifts shiftstr)            ;; parse the assigned shifts string
        avg-shifts (transform-stats (parse-bmrb-stats bmrbstr))    ;; parse the bmrb stats string, transform to semantic model
        prot (seq-to-protein seq)                 ;; create a protein object from the sequence
        prot-shifts (merge-shifts prot shifts)]   ;; put the assigned chemical shifts into the protein
   (merge-bmrb prot-shifts avg-shifts)))          ;; put the bmrb standard shifts into the protein (based on matching aatype/atomtype)

(defn venn-nmr
  [seqfile shiftfile bmrbfile]
  (venn-nmr-help (slurp seqfile)
                 (slurp shiftfile)
                 (slurp bmrbfile)))


;;(def mseq   (slurp "resources/venn_nmr/sequence.txt"))
;;(def mshift (slurp "resources/venn_nmr/assigned-shifts.txt"))
;;(def mstat  (slurp "resources/venn_nmr/bmrbstats.txt"))
;;(def goodstats (transform-stats (parse-bmrb-stats (slurp "resources/venn_nmr/bmrbstats.txt"))))
;;(venn-nmr-help mseq mshift mstat)
;;(venn-nmr "resources/venn_nmr/sequence.txt" "resources/venn_nmr/assigned-shifts.txt" "resources/venn_nmr/bmrbstats.txt")

