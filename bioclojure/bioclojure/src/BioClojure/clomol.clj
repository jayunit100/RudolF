(ns BioClojure.clomol
  (:use [clojure.contrib.string :only (replace-by)])
  (:use clojure.contrib.generic.functor)
  (:use [BioClojure.vennnmr :only (venn-nmr)])
  (import org.biojava.bio.structure.StructureTools)
  (import java.lang.Math))

;;    String filename =  "/path/to/PDBFile.pdb" ;
;;        PDBFileReader pdbreader = new PDBFileReader();
;;        pdbreader.setParseSecStruc(true);
;;        pdbreader.setAlignSeqRes(true);
;;        pdbreader.setParseCAOnly(false);
;;        pdbreader.setAutoFetch(true);
;;        Structure struc = pdbreader.getStructure(filename);
(defn getStructureF 
  "Get a structure, local" 
  [filename]
  (.getStructure (new org.biojava.bio.structure.io.PDBFileReader) filename))

(defn launchGui 
  "Matt - maybe move this to vennnmr.clj or app.clj ?  
   Here is a starting point - it will launch biojava jmol gui. 
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

;; could/should this could be rewritten as:
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
        myvalues (map (fn [x] [(Math/floor (* 255 (atomColors x))) 111 111]) mykeys)] 
                ;; this probably needs refactoring .... -- Matt 
		;; ... this code is just generating keys, and then generating a value from each of those keys, 
                ;;          and then putting those key/value pairs into a map
   (zipmap mykeys myvalues)))


(defn ex2  
  "launch a viewer that views trp represor. And then colors it blue, with spacefill. "
  []
  (let [ panel (new org.biojava.bio.structure.gui.BiojavaJmol) 
	 atomToFloat (atomColorPct 4)] ;; changed to make it compile -- 4 is any arbitrary number, 'atomColorPct' is an arbitrary function
       (.setStructure panel (getStructure "1WRP"))
       (.evalString panel "select *; spacefill 200; wireframe off; backbone 0.4; color chain")
       ;; this method creates a map of indices to select statements .... how to select the key and put value in the right half ?
       (doseq [[k v] (atomColorValue (StructureTools/getNrAtoms (getStructure "1WRP")))] 
              (.evalString panel (str "select atomno=" k " ; color " v  " ;" ) )) )) ; @Matt -- read about 'doseq'

(defn getColor
  ""
  [std-shift-map atomid]
  [atomid (std-shift-map atomid)])

(defn ex3  
  "launch a viewer that views 16790, then colors it by shift deviation from norms"
  [std-shift-map]
  (let [panel (new org.biojava.bio.structure.gui.BiojavaJmol)]
       (.setStructure panel (getStructure "2X8N"))
       (.evalString panel "select *; spacefill 200; wireframe off; backbone 0.4; color chain")
	;; get the number of atoms
	;; create the integers from 1 to that number -- these are the keys
	;; pull the values out of the vennnmr data structure -- the values are the std shifts
	;; color all of these atoms in the structure
       (doseq [[k v] std-shift-map] 
              (.evalString panel (str  "select atomno=",  k,  " ; color ",  v,  " ;"))))) ; @Matt -- read about 'doseq'

(defn new-gui-example  
  "launch a viewer that views 16790, then colors it by shift deviation from norms"
  [atoms]
  (let [panel (new org.biojava.bio.structure.gui.BiojavaJmol)]
       (.setStructure panel (getStructureF "resources/venn_nmr/structure.pdb"))
       (.evalString panel "select *; spacefill 200; wireframe off; backbone 0.4; color chain")
       (doseq [x atoms] 
              (.evalString panel (str  "select resno="
                                       (:resid x)
                                       " and atomname=\""
                                       (:aname x)
                                       "\" ; color "
                                       (:color x)
                                       " ;"))))) 

(defn color-maker
  "input: map of atomid to normalized shift"
  [atoms-to-shifts]
  (fmap (fn [x] [(Math/floor (* 128 x)) 111 111]) atoms-to-shifts))

(defn color-maker2
  "input: ???"
  [num]
  (let [value (Math/floor (* 128 num))]
   [value 111 111]))

(defn translate-shift
  "compare shift to avg-shift, returning an arbitrary number of no avg-shift"
  [shift avg-shift]
  (if (symbol? avg-shift)   ;; if there was no shift in the bmrb stats to compare to
      1.99			    ;; then return an arbitrary number
      (/ shift avg-shift))) ;; but if there was, compare (by division)

(defn my-vals
  "like vals, but returns '() instead of nil if no vals in map"
  [my-map]
  (if (= (count my-map) 0)
      '()
      (vals my-map)))

(defn stats-to-atomid-map
  "thing to transform stats data from venn-nmr to what we need here"
  [stats-map]
  ;; for each value (residue map)
  ;;   for each atom (:atoms)
  ;;     [atomid norm-shift]
  (into {} (map (fn [atom-map] 
                    [(atom-map :id) (translate-shift (atom-map :shift) (atom-map :avg))]) 
                (flatten (map my-vals (map :atoms (vals stats-map)))))))

(defn stats-to-new-map
  "thing to transform stats data from venn-nmr to what we need here"
  [stats-map]
  (apply concat  ;; using apply because concat is broken (concat accepts multiple parameters instead of an iterable) 
   (for [[resid res] stats-map]
    (for [[aname atom-map] (res :atoms)]
     {:resid resid 
      :aname aname 
      :color (color-maker2 (translate-shift (atom-map :shift) (atom-map :avg)))}))))

(defn example-4
  "??colors?? atoms according to normalization against std shifts??"
  []
  (ex3 (color-maker (stats-to-atomid-map (venn-nmr "resources/venn_nmr/sequence.txt" "resources/venn_nmr/assigned-shifts.txt" "resources/venn_nmr/bmrbstats.txt")))))

(defn example-5
  ""
  []
  (new-gui-example (stats-to-new-map (venn-nmr "resources/venn_nmr/sequence.txt" "resources/venn_nmr/assigned-shifts.txt" "resources/venn_nmr/bmrbstats.txt"))))

(def stats-stuff (stats-to-new-map (venn-nmr "resources/venn_nmr/sequence.txt" 
                                             "resources/venn_nmr/assigned-shifts.txt" 
                                             "resources/venn_nmr/bmrbstats.txt")))

;;
;;(def nums-eg (stats-to-atomid-map (venn-nmr "resources/venn_nmr/sequence.txt" 
;;                                            "resources/venn_nmr/assigned-shifts.txt" 
;;                                            "resources/venn_nmr/bmrbstats.txt")))

;;(def color-eg (color-maker nums-eg))

;;(def venn-struc (venn-nmr "resources/venn_nmr/sequence.txt" "resources/venn_nmr/assigned-shifts.txt" "resources/venn_nmr/bmrbstats.txt"))
