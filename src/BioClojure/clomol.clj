(ns BioClojure.clomol
  (:use [clojure.contrib.string :only (replace-by)])
  (:use clojure.contrib.generic.functor)
  )

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

;;Creates a string that colors an amino acid
(defn colorAA [index]
 (.replaceAll "select index; color green" "index" (str index)) ) 

;;Launch a viewer that views trp represor
;;And then colors it blue, with spacefill. 
;;This is a good example of how to use object API's in clojure, via the let function.
(defn ex1 []
  (let [panel (new org.biojava.bio.structure.gui.BiojavaJmol)] 
       (.setStructure panel (getStructure "1WRP")) 
       (.evalString panel "select *; spacefill 200; wireframe off; backbone 0.4; color chain")))


;;returns a map of amino acid numbers -> rgb colors , to use venn nmr
(defn aaColors [] 
  ([1 2 3]) )

;;incomplete method to map color statements 
;;over all residues.
(defn ex2 []
  (let [panel (new org.biojava.bio.structure.gui.BiojavaJmol)]
       (.setStructure panel (getStructure "1WRP"))
       (map #(colorAA %) (aaColors)) ))
