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
                        {:avg (linemap "Avg.") :stddev (linemap "StdDev")}))] ;; refactor the return value ....
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
        shift (shift-map :shift)]
   (assoc-in prot [resid :atoms atomname] {:shift shift})))

(defn merge-shifts
  "input: protein, list of maps of header to value"
  [prot shift-list]
  (reduce place-shift prot shift-list))

(defn place-stats
  "input: 1) atoms, keys are atom names, values are maps
          2) average shifts map (specific to that residue) -- keys: atom names, values: maps of info about that atom
   output: augmented atoms"
  [atoms stats]
    ;; for each atom
    ;; find the standard data in stats
    ;; put that into the atom's map
  (into {} (for [[atomname atommap] atoms]
                [atomname (assoc-in atommap [:avg] ((stats atomname) :avg))]))) ;; but what does this do if (stats atomname) is nil????

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


;;(venn-nmr "resources/venn_nmr/sequence.txt" "resources/venn_nmr/assigned-shifts.prot" "resources/venn_nmr/bmrbstats.txt")

(def mseq   (slurp "resources/venn_nmr/sequence.txt"))
(def mshift (slurp "resources/venn_nmr/assigned-shifts2.prot"))
(def mstat  (slurp "resources/venn_nmr/bmrbstats.txt"))

;; for informal testing
(def shifts (parse-shifts "     7  57.135   0.000 CA      1
     1   4.155   0.000 HA      1
     8  29.815   0.000 CB      1
     2   1.908   0.000 HB2     1
     3   2.003   0.000 HB3     1
     9  36.326   0.000 CG      1
     4   2.222   0.000 HG2     1
     5   2.222   0.000 HG3     1
     6 176.410   0.000 C       1
    19 118.889   0.000 N       2
    10   8.376   0.000 H       2
    17  53.005   0.000 CA      2
    11   4.752   0.000 HA      2
    18  38.025   0.000 CB      2
    12   2.675   0.000 HB2     2
    13   2.932   0.000 HB3     2
    20 110.708   0.000 ND2     2
    14   6.792   0.000 HD21    2
    15   7.423   0.000 HD22    2
    16 175.445   0.000 C       2
   123 116.209   0.000 N      12
   116   8.466   0.000 H      12
   121  56.000   0.000 CA     12
   117   4.179   0.000 HA     12
   122  40.455   0.000 CB     12
   118   2.630   0.000 HB2    12
   119   2.469   0.000 HB3    12
   120 175.224   0.000 C      12"))

(def myseq (parse-sequence "MET
ASN
CYS
VAL
CYS
GLY
SER
GLY
LYS
THR
TYR
ASP
ASP
CYS
CYS
GLY
PRO
LEU"))

(def stats (parse-bmrb-stats "Res    Name     Atom     Count        Min.        Max.       Avg.      StdDev
ALA     H        H        30843        3.53        11.48       8.20       0.60       
ALA     HA       H        23429        0.87         6.51       4.26       0.44       
ALA     HB       H        22202       -0.88         3.12       1.35       0.26       
ALA     C        C        19475      164.48       187.20     177.72       2.14       
ALA     CA       C        26260       44.22        65.52      53.13       1.98       
ALA     CB       C        24766        0.00        38.70      19.01       1.84       
ALA     N        N        28437       77.10       142.81     123.24       3.54       
ARG     H        H        21153        3.57        12.69       8.24       0.61       
ARG     HA       H        16560        1.34         6.52       4.30       0.46       
ARG     HB2      H        14978       -0.86         3.44       1.79       0.27       
ARG     HB3      H        14071       -0.86         3.32       1.76       0.28       
ARG     HG2      H        13472       -0.72         3.51       1.57       0.27       
ARG     HG3      H        12287       -0.74         3.51       1.54       0.29       
ARG     HD2      H        13185        0.96         4.69       3.12       0.24       
ARG     HD3      H        11833        0.73         4.56       3.10       0.26       
ARG     HE       H        4149         2.99        11.88       7.39       0.64       
ARG     HH11     H        379          5.88         9.82       6.91       0.46       
ARG     HH12     H        274          6.01         8.76       6.81       0.32       
ARG     HH21     H        342          5.90        11.35       6.82       0.48       
ARG     HH22     H        268          5.97        10.18       6.76       0.36       
ARG     C        C        12724      167.44       184.51     176.40       2.03       
ARG     CA       C        17600       43.27        67.98      56.77       2.31       
ARG     CB       C        16224       20.95        42.50      30.70       1.83       
ARG     CG       C        10535       18.22        40.94      27.21       1.20       
ARG     CD       C        10667       35.05        50.88      43.16       0.88       
ARG     CZ       C        219        156.20       177.70     159.98       2.99       
ARG     N        N        18883      102.78       137.60     120.80       3.68       
ARG     NE       N        2261        67.00        99.81      84.64       1.70       
ARG     NH1      N        64          67.60        87.07      73.62       4.35       
ARG     NH2      N        55          70.10        85.28      73.26       3.32       
ASP     H        H        24462        4.06        12.68       8.31       0.58       
ASP     HA       H        18689        2.33         6.33       4.59       0.32       
ASP     HB2      H        17394       -0.39         4.58       2.72       0.26       
ASP     HB3      H        16624       -0.23         4.58       2.66       0.28       
ASP     HD2      H        4            4.65         6.03       5.25       0.58       
ASP     C        C        15510      166.80       182.70     176.39       1.75       
ASP     CA       C        21054       41.88        67.17      54.69       2.03       
ASP     CB       C        19786       27.48        51.09      40.88       1.62       
ASP     CG       C        290        170.72       186.50     179.16       1.81       
ASP     N        N        22760      101.90       143.52     120.64       3.87       
ASP     OD1      O        20         177.59       180.97     179.66       0.91"))

(def trans-stats (transform-stats stats))

(def myprot (seq-to-protein myseq))
(def merged (merge-shifts myprot shifts))


