(ns ConnjurRV.vizstruct
  (import org.biojava.bio.structure.StructureTools))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; module interface
;	display-struct
;	display-colored-struct
;	??? color-atom ??? maybe this is private
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn display-struct
  "Biojava.Structure -> PopupDisplay
   1) display structure in a panel
   2) return panel"
  [structure]
  (doto  
   (new org.biojava.bio.structure.gui.BiojavaJmol) 
   (.setStructure structure)))

 
;; what if the panel doesn't have an atom with the given atomid??
;; 
(defn color-atom
  "gui.BiojavaJmol -> atomid -> color -> ()
   set color of an atom in a Jmol panel"
  [panel atomid color]
  (.evalString 
   panel 
   (str  "select atomno="
         atomid
         " ; color "
         color
         " ;")))


(defn display-colored-struct
  "Biojava.Structure -> Map atomid Color -> PopupDisplay
   1) display structure
   2) set atoms' colors
   3) return panel"
  [structure colormap]
  (let [panel (display-struct structure)]
   (.evalString panel "select *; spacefill 200; wireframe off; backbone 0.4; color chain")
   (doseq [[atomid color] colormap] 
    (color-atom panel atomid color))
   panel))
