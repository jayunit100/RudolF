(ns ConnjurRV.model)

; protein
; residue
; atom
; atom data (shift, shift-diff, relaxation, etc.)


(defn get-residues
  "Protein -> Map rindex Residue"
  [protein]
  (protein :residues))

(defn protein-to-atoms
  "Protein -> [Atom]"
  [prot]
  (let [anon-atoms (map #(:atoms (second %)) (get-residues prot))]
   (for [[name data] (apply concat anon-atoms)]
    (into data [[:atomname name]]))))