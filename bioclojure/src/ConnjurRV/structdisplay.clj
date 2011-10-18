(ns ConnjurRV.structdisplay
  (import org.biojava.bio.structure.StructureTools))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; structure utilities

(defn display-struct
  "Biojava.Structure -> PopupDisplay"
  [structure]
  (doto  
   (new org.biojava.bio.structure.gui.BiojavaJmol) 
   (.setStructure structure)))

(defn load-local-struct
  "Filepath -> Biojava.Structure"
  [filename]
  (.getStructure (new org.biojava.bio.structure.io.PDBFileReader) filename))


(defn load-pdb-struct 
  "PDBID -> org.biojava.bio.structure.Structure"
  [pdbid]
  (.getStructure 
   (new org.biojava.bio.structure.align.util.AtomCache "/tmp" true) 
   pdbid))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; functions for displaying a structure and coloring the atoms

; what if the panel doesn't have an atom with the given atomid??
(defn color-atom
  "gui.BiojavaJmol -> atomid -> color -> ()"
  [panel atomid color]
  (.evalString 
   panel 
   (str  "select atomno="
         atomid
         " ; color "
         color
         " ;")))

(defn display-colored-struct
  "Biojava.Structure -> Map atomid Color -> PopupDisplay"
  [structure colormap]
  ;; display the structure
  ;; change the colors
  ;; make sure the panel is returned
  (let [panel (display-struct structure)]
   (.evalString panel "select *; spacefill 200; wireframe off; backbone 0.4; color chain")
   (doseq [[atomid color] colormap] 
    (color-atom panel atomid color))
   panel))
