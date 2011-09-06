(ns BioClojure.clomol
  (:use [clojure.contrib.string :only (replace-by)])
  (:use clojure.contrib.generic.functor))

(defn launchGui 
  "Here is a starting point - it will launch biojava jmol gui. 
   INPUT: a biojava Structure object (org.biojava.bio.structure.Structure)
   OUTPUT:  ???"
  [s]
  (.setStructure (new org.biojava.bio.structure.gui.BiojavaJmol) s))

(defn getStructure 
  "Get a structure From the pdb 
    INPUT: a pdb id
    OUPUT: is a org.biojava.bio.structure.Structure object."
  [pdbid]
  (.getStructure (new org.biojava.bio.structure.align.util.AtomCache "/tmp" true) pdbid))

(defn colorAA 
  "Creates a string that colors an amino acid"
  [index]
  (str "select ", index, "; color green")) 

;;This is a good example of how to use object API's in clojure, via the let special form  -- 'let' is not a function
;; what is the return value intended to be?  panel?  what is the return value of .evalString?
;; can this no-arg procedure also be refactored as a (def ...) binding?
(defn ex1 
  "Launch a viewer that views trp represor. And then colors it blue, with spacefill. "
  []
  (let [panel (new org.biojava.bio.structure.gui.BiojavaJmol)] 
       (.setStructure panel (getStructure "1WRP")) 
       (.evalString panel "select *; spacefill 200; wireframe off; backbone 0.4; color chain")))


;; this is (?supposed?) to be a map of amino acid numbers -> rgb colors , to use venn nmr
;; refactored from no-args function to a name binding
(def aaColors 
  ([1 2 3]) ) ;; is this supposed to be a map?  should it be [1 2 3] -- without the parentheses?

;;incomplete method to map color statements 
;;over all residues.
(defn ex2 []
  (let [panel (new org.biojava.bio.structure.gui.BiojavaJmol)]
       (.setStructure panel (getStructure "1WRP"))
       (map colorAA aaColors)))

