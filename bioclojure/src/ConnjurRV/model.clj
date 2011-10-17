(ns ???
  (:use ???))

; protein
; residue
; atom
; atom data (shift, shift-diff, relaxation, etc.)


(defn getResidues
  ""
  [protein]
  (protein :residues))

(defn getAtoms
  ""
  [residue]
  (residue :atoms))