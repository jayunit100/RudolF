(ns ConnjurRV.readpdb
  (import org.biojava.bio.structure.StructureTools))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; module interface
;	load-local-struct
;	load-pdb-struct
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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