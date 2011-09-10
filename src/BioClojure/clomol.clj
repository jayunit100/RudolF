(ns BioClojure.clomol
  (:use [clojure.contrib.string :only (replace-by)])
  (:use clojure.contrib.generic.functor)
  (import org.biojava.bio.structure.StructureTools)
)

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
;; what is the return value intended to be?
;; 
;; check out the 'doto' special form:  http://clojure.org/java_interop#Java%20Interop-The%20Dot%20special%20form-%28doto%20instance-expr%20%28instanceMethodName-symbol%20args*%29*%29
;; this could be rewritten as:
;;  (doto (new org.biojava.bio.structure.gui.BiojavaJmol)]
;;   (.setStructure (getStructure "1WRP"))
;;   (.evalString "select *; spacefill 200; wireframe off; backbone 0.4; color chain"))
(defn ex1 
  "Launch a viewer that views trp represor. And then colors it blue, with spacefill. "
  []
  (let [panel (new org.biojava.bio.structure.gui.BiojavaJmol)] 
       (.setStructure panel (getStructure "1WRP")) 
       (.evalString panel "select *; spacefill 200; wireframe off; backbone 0.4; color chain")
       (.evalString panel "select atomno=1; color RED") ;;<--- color this one atom red.
))


;; this is (?supposed?) to be a map of amino acid numbers -> rgb colors , to use venn nmr
;; refactored from no-args function to a name binding
(defn atomColors "" [acount] 
	(
		zipmap (range 1 acount) (repeat (rand)) 
	)
) ;; is this supposed to be a map?

;;ounch a viewer that views trp represor. And then colors it blue, with spacefill. "
(defn ex2  []
  (let [ panel (new org.biojava.bio.structure.gui.BiojavaJmol) 
	 atomToFloat atomColors
	 ]
       (.setStructure panel (getStructure "1WRP"))
       (.evalString panel "select *; spacefill 200; wireframe off; backbone 0.4; color chain")
       ;;this methodo creates a map  of iindices to selct statements .... how to select the key and put value in the right half ?
	(fmap #(str "select atomno=" % ";" ) (atomColors (StructureTools/getNrAtoms (getStructure "1WRP") )))
)
)
