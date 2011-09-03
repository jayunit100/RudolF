(ns BioClojure.clomol)

(use 'BioClojure.vennnmr)

;;Here is a starting point - it will launch biojava jmol gui. 
;;INPUT a biojava Structure object (org.biojava.bio.structure.Structure)
(defn launchGui [s]
 (.setStructure (new org.biojava.bio.structure.gui.BiojavaJmol) s)
)
;;Get a structure From the pdb 
;;INPUT a pdb id
;;OUPUT is a org.biojava.bio.structure.Structure object.
(defn getStructure [pdbid]
 (.getStructure (new org.biojava.bio.structure.align.util.AtomCache "/tmp" true) pdbid))

;;Launch a viewer that views trp represor
(defn ex1 []
 (launchGui(getStructure "1WRP"))
)



