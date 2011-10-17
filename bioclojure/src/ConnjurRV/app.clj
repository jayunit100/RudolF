(ns BioClojure.connjurRV)
(require 'clojure.contrib.java-utils)

;;A map of command line options.
;;There are two options "r" (for visualizint relaxation data ) and "c" for (venn NMR)
(def options
  {"r"
   "c"})

;;input 1 : a mode (options r or c) 
;;input 2 : a file (pdbFile) 
(defn run [mode pdbFile]
  {
    ;;Two preconditions :the option is valid, the file must exist
    :pre  [(contains? options mode) 
	   (.exists (clojure.contrib.java-utils/file pdbFile))]
  }
  (print "valid\n"))

(run "r" "a.pdb")
