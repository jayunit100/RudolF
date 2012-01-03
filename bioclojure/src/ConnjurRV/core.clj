(ns ConnjurRV.core
  (:use [clojure.contrib.java-utils      :only (file)])
  (:use [ConnjurRV.readstats             :only (merge-bmrb)])
  (:use [ConnjurRV.readpdb               :only (load-pdb-struct)])
  (:use [ConnjurRV.readcyana             :only (make-protein-from-files)])
  (:use [ConnjurRV.vizstruct             :only (display-colored-struct)])
  (:use [ConnjurRV.vizcharts             :only (make-bar-chart)])
  (:use [ConnjurRV.modelreducer          :only (get-atoms-map get-residues-map)])
  (:use [ConnjurRV.statistics            :only (color-map normalized-shifts)])
  (:use [clojure.contrib.generic.functor :only (fmap)])
  (:gen-class :main true))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;A set of command line options.
;;There are two options "r" (for visualizing relaxation data ) and "c" for (venn NMR)
(def options (set ["r" "c"]))

;;input 1 : a mode (options r or c) 
;;input 2 : a file (pdbFile) 
(defn run [mode pdbFile]
  {
    ;;Two preconditions :the option is valid, the file must exist
    :pre  [(contains? options mode) (.exists (file pdbFile))]
  }
  (print "valid\n"))

(defn example-run
  ""
  []
  (run "r" "data/sample15270.pdb"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn norm-shifts-on-struct
  ""
  [seq-file shift-file pdbid]
  (let [protein (make-protein-from-files seq-file shift-file)
        stats-protein (merge-bmrb protein)
        atomid-shifts (get-atoms-map stats-protein)
        atom-colors (color-map (normalized-shifts atomid-shifts))
        structure (load-pdb-struct pdbid)]
   (display-colored-struct
    structure
    atom-colors)))

(defn plot-atom-shifts
  "plot Map resid shift"
  [seq-file shift-file atomname]
  (let [protein (make-protein-from-files seq-file shift-file)
        func #(try (((% :atoms) atomname) :shift) (catch Exception e 0))
        resid-shifts (fmap func (get-residues-map protein))]
   (make-bar-chart 
    resid-shifts
    "residue number"
    (str atomname " shift"))
   resid-shifts)) ;; returning it for checking (ugly hack)
 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def run-options {"norm-shifts" norm-shifts-on-struct
              "shift-plot" plot-atom-shifts})

(defn -main
  ""
  [& args]
  (let [option (first args)
        r-args (rest args)
        func   (run-options option)]
   (println (str "your command-line args for BioClojure/ConnjurRV v0.0.2: " (apply str args)))
   (if (and func (= 4 (count args)))
    (apply func r-args)
    (do
     (println "error with command-line arguments")
     (println "usage: <program> shift-plot <path to seq file> <path to shift file> <atomname>")
     (println "alternate usage: <program> norm-shifts <path to seq file> <path to shift file> <PDBID>")))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn example
  ""
  []
  (norm-shifts-on-struct "data/connjur.seq" "data/connjur.prot" "1TXR"))

(defn plot-example
  ""
  []
  (plot-atom-shifts "data/connjur.seq" "data/connjur.prot" "CA"))

(defn plot-eg
  ""
  [aname]
  (plot-atom-shifts "data/connjur.seq" "data/connjur.prot" aname))
