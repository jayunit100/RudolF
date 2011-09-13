(ns BioClojure.clomol
  (:use [clojure.contrib.string :only (replace-by)])
  (:use clojure.contrib.generic.functor)
  (import org.biojava.bio.structure.StructureTools)
  (import java.lang.Math)
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
       (.evalString panel "select atomno=1; color RED"))) ;;<--- color this one atom red.
 
(defn atomColorPct 
  "Returns a map of atom #s to floats" 
  [acount] 
  (zipmap (range 1 acount) 
          (repeatedly rand)))

(defn atomColorValue 
  "Uses the atomColorPct method to scale rbg colors. 
  returns a map of atoms to colors" 
  [acount]
  (let [atomColors (atomColorPct acount)
        mykeys (range 1 acount)
        myvalues (map (fn [x] [(Math/floor (* 255 (atomColors x))) 111 111]) mykeys)] ;; this probably needs refactoring .... -- Matt ... this code is just generating keys, and then generating a value from each of those keys, and then putting those key/value pairs into a map
   (zipmap mykeys myvalues)))


(defn ex2  
  "launch a viewer that views trp represor. And then colors it blue, with spacefill. "
  []
  (let [ panel (new org.biojava.bio.structure.gui.BiojavaJmol) 
	 atomToFloat (atomColorPct 4)] ;; changed to make it compile -- 4 is any arbitrary number, 'atomColorPct' is an arbitrary function
       (.setStructure panel (getStructure "1WRP"))
       (.evalString panel "select *; spacefill 200; wireframe off; backbone 0.4; color chain")
       ;; this method creates a map of indices to select statements .... how to select the key and put value in the right half ?
       (doseq [[k v] (atomColorValue (StructureTools/getNrAtoms (getStructure "1WRP")))] (.evalString panel (str "select atomno=" k " ; color " v  " ;" ) )) ))
	;;(fmap #(str "select atomno=" %?KEY? "; color " % "" ) (atomColorValue (StructureTools/getNrAtoms (getStructure "1WRP")))) ))
