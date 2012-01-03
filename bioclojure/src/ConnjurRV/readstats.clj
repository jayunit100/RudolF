(ns ConnjurRV.readstats
  (:use [clojure.contrib.str-utils2 :only (split-lines trim)])
  (:use clojure.contrib.generic.functor))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; module interface
;	merge-bmrb
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; this code is used to load in the bmrb data from a file -- it is currently unused

(comment 
(defn parse-bmrb-stats
  "type :: String -> [Statsline]
   Statsline: map where keys are /Res, Name, Atom, Count, Min. Max., Avg., StdDev/"
  [string]
  (let [lines (split-lines string)
        headings (.split (first lines) " +")
        atomlines (rest lines)]
   (for [atomline atomlines]
    (zipmap headings (.split atomline " +")))))
)

(comment
(defn transform-stats
  "type :: [Statsline] -> Stats
   where Stats :: Map aatype (Map atomname (Map statname statvalue))

   NOTE:  ignores most of the data in the stats, and just pulls out the average chemical shift and std dev
   "
  [shift-stats]
  (let [f (fn [base linemap] 
              (assoc-in base 
                        [(linemap "Res") (linemap "Name")] 
                        {:avg (Float/parseFloat (linemap "Avg.")) 
                         :stddev (Float/parseFloat (linemap "StdDev"))}))] ;; refactor the return value ....
   (reduce f {} shift-stats))) 
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; this is a snapshot of the bmrb data

; test: there are 20 keys, each of which is a three letter abbr for an amino acid
;	each value is a map with keys of atom names, values are maps with keys :avg and :stddev
(def stats-data {"SER" {"N" {:avg 116.27, :stddev 3.52}, "CB" {:avg 63.8, :stddev 1.48}, "CA" {:avg 58.72, :stddev 2.08}, "C" {:avg 174.62, :stddev 1.73}, "HG" {:avg 5.35, :stddev 1.02}, "HB3" {:avg 3.85, :stddev 0.27}, "HB2" {:avg 3.88, :stddev 0.25}, "HA" {:avg 4.48, :stddev 0.41}, "H" {:avg 8.28, :stddev 0.58}}, "VAL" {"C" {:avg 175.61, :stddev 1.89}, "H" {:avg 8.29, :stddev 0.67}, "CG1" {:avg 21.48, :stddev 1.35}, "N" {:avg 121.14, :stddev 4.54}, "CG2" {:avg 21.26, :stddev 1.57}, "HG1" {:avg 0.83, :stddev 0.26}, "HG2" {:avg 0.8, :stddev 0.28}, "HA" {:avg 4.19, :stddev 0.58}, "HB" {:avg 1.98, :stddev 0.32}, "CA" {:avg 62.48, :stddev 2.86}, "CB" {:avg 32.74, :stddev 1.79}}, "ILE" {"HG12" {:avg 1.28, :stddev 0.4}, "HG13" {:avg 1.19, :stddev 0.41}, "C" {:avg 175.84, :stddev 1.92}, "H" {:avg 8.28, :stddev 0.69}, "CG1" {:avg 27.71, :stddev 1.69}, "N" {:avg 121.46, :stddev 4.28}, "CG2" {:avg 17.52, :stddev 1.37}, "CD1" {:avg 13.43, :stddev 1.68}, "HG2" {:avg 0.77, :stddev 0.27}, "HD1" {:avg 0.68, :stddev 0.29}, "HA" {:avg 4.18, :stddev 0.56}, "HB" {:avg 1.78, :stddev 0.29}, "CA" {:avg 61.61, :stddev 2.67}, "CB" {:avg 38.63, :stddev 2.03}}, "LYS" {"CD" {:avg 28.95, :stddev 1.1}, "CE" {:avg 41.88, :stddev 0.82}, "C" {:avg 176.64, :stddev 2.01}, "CG" {:avg 24.89, :stddev 1.14}, "H" {:avg 8.19, :stddev 0.6}, "NZ" {:avg 33.81, :stddev 2.81}, "N" {:avg 121.05, :stddev 3.78}, "HZ" {:avg 7.42, :stddev 0.67}, "HG2" {:avg 1.37, :stddev 0.26}, "HG3" {:avg 1.35, :stddev 0.28}, "HE2" {:avg 2.92, :stddev 0.19}, "HD2" {:avg 1.61, :stddev 0.22}, "HE3" {:avg 2.91, :stddev 0.2}, "HD3" {:avg 1.6, :stddev 0.22}, "HB2" {:avg 1.78, :stddev 0.25}, "HA" {:avg 4.27, :stddev 0.44}, "HB3" {:avg 1.74, :stddev 0.27}, "CA" {:avg 56.95, :stddev 2.2}, "CB" {:avg 32.79, :stddev 1.78}}, "GLN" {"CD" {:avg 179.68, :stddev 1.3}, "C" {:avg 176.3, :stddev 1.95}, "CG" {:avg 33.75, :stddev 1.12}, "H" {:avg 8.22, :stddev 0.59}, "N" {:avg 119.89, :stddev 3.6}, "HG2" {:avg 2.32, :stddev 0.27}, "HG3" {:avg 2.29, :stddev 0.29}, "HB2" {:avg 2.05, :stddev 0.25}, "HA" {:avg 4.27, :stddev 0.44}, "HB3" {:avg 2.02, :stddev 0.28}, "NE2" {:avg 111.86, :stddev 1.72}, "HE21" {:avg 7.24, :stddev 0.45}, "HE22" {:avg 7.02, :stddev 0.44}, "CA" {:avg 56.57, :stddev 2.14}, "CB" {:avg 29.2, :stddev 1.82}}, "PHE" {"C" {:avg 175.41, :stddev 1.99}, "CG" {:avg 138.3, :stddev 2.05}, "H" {:avg 8.36, :stddev 0.72}, "N" {:avg 120.47, :stddev 4.17}, "CE1" {:avg 130.7, :stddev 1.32}, "CE2" {:avg 130.75, :stddev 1.14}, "CD1" {:avg 131.57, :stddev 1.18}, "CD2" {:avg 131.63, :stddev 1.11}, "HZ" {:avg 7.0, :stddev 0.42}, "HE1" {:avg 7.08, :stddev 0.31}, "HD1" {:avg 7.06, :stddev 0.3}, "HE2" {:avg 7.08, :stddev 0.32}, "HD2" {:avg 7.06, :stddev 0.31}, "CZ" {:avg 129.21, :stddev 1.45}, "HB2" {:avg 3.0, :stddev 0.37}, "HA" {:avg 4.63, :stddev 0.57}, "HB3" {:avg 2.93, :stddev 0.4}, "CA" {:avg 58.08, :stddev 2.56}, "CB" {:avg 39.98, :stddev 2.1}}, "TYR" {"HH" {:avg 9.3, :stddev 1.37}, "C" {:avg 175.38, :stddev 1.99}, "CG" {:avg 129.74, :stddev 5.13}, "H" {:avg 8.32, :stddev 0.73}, "N" {:avg 120.53, :stddev 4.22}, "CE1" {:avg 117.93, :stddev 1.18}, "CE2" {:avg 117.9, :stddev 1.2}, "CD1" {:avg 132.77, :stddev 1.19}, "CD2" {:avg 132.72, :stddev 1.32}, "HE1" {:avg 6.7, :stddev 0.23}, "HD1" {:avg 6.93, :stddev 0.3}, "HE2" {:avg 6.71, :stddev 0.23}, "HD2" {:avg 6.93, :stddev 0.3}, "CZ" {:avg 156.66, :stddev 2.04}, "HB2" {:avg 2.91, :stddev 0.37}, "HA" {:avg 4.63, :stddev 0.57}, "HB3" {:avg 2.84, :stddev 0.4}, "CA" {:avg 58.12, :stddev 2.51}, "CB" {:avg 39.33, :stddev 2.16}}, "PRO" {"CD" {:avg 50.32, :stddev 0.99}, "C" {:avg 176.74, :stddev 1.52}, "CG" {:avg 27.17, :stddev 1.09}, "N" {:avg 134.1, :stddev 6.68}, "HG2" {:avg 1.93, :stddev 0.31}, "HG3" {:avg 1.9, :stddev 0.33}, "HD2" {:avg 3.65, :stddev 0.35}, "HD3" {:avg 3.61, :stddev 0.39}, "HB2" {:avg 2.08, :stddev 0.35}, "HA" {:avg 4.4, :stddev 0.33}, "HB3" {:avg 2.0, :stddev 0.36}, "CA" {:avg 63.33, :stddev 1.53}, "CB" {:avg 31.85, :stddev 1.18}}, "GLU" {"CD" {:avg 182.38, :stddev 1.79}, "C" {:avg 176.86, :stddev 1.95}, "CG" {:avg 36.1, :stddev 1.2}, "H" {:avg 8.33, :stddev 0.6}, "N" {:avg 120.67, :stddev 3.5}, "HG2" {:avg 2.27, :stddev 0.21}, "HG3" {:avg 2.25, :stddev 0.22}, "HE2" {:avg 2.73, :stddev 0.0}, "HB2" {:avg 2.02, :stddev 0.21}, "HA" {:avg 4.25, :stddev 0.42}, "HB3" {:avg 1.99, :stddev 0.23}, "OE1" {:avg 184.19, :stddev 0.0}, "CA" {:avg 57.33, :stddev 2.09}, "CB" {:avg 30.01, :stddev 1.72}}, "TRP" {"HZ2" {:avg 7.28, :stddev 0.33}, "HZ3" {:avg 6.86, :stddev 0.4}, "C" {:avg 176.11, :stddev 2.01}, "CG" {:avg 110.53, :stddev 1.87}, "H" {:avg 8.29, :stddev 0.78}, "CH2" {:avg 123.85, :stddev 1.43}, "N" {:avg 121.67, :stddev 4.16}, "CE2" {:avg 138.57, :stddev 6.7}, "CD1" {:avg 126.5, :stddev 1.85}, "CE3" {:avg 120.42, :stddev 1.8}, "CD2" {:avg 127.45, :stddev 1.45}, "HH2" {:avg 6.98, :stddev 0.38}, "HE1" {:avg 10.08, :stddev 0.65}, "HD1" {:avg 7.14, :stddev 0.36}, "HE3" {:avg 7.31, :stddev 0.41}, "HB2" {:avg 3.19, :stddev 0.35}, "HA" {:avg 4.68, :stddev 0.52}, "HB3" {:avg 3.12, :stddev 0.36}, "NE1" {:avg 129.33, :stddev 2.08}, "CZ2" {:avg 114.25, :stddev 1.12}, "CZ3" {:avg 121.34, :stddev 1.55}, "CA" {:avg 57.66, :stddev 2.58}, "CB" {:avg 30.0, :stddev 2.01}}, "HIS" {"C" {:avg 175.24, :stddev 1.97}, "CG" {:avg 131.45, :stddev 3.25}, "H" {:avg 8.25, :stddev 0.68}, "N" {:avg 119.67, :stddev 4.01}, "CE1" {:avg 137.65, :stddev 2.15}, "CD2" {:avg 120.4, :stddev 3.36}, "HE1" {:avg 7.95, :stddev 0.49}, "HD1" {:avg 8.71, :stddev 2.64}, "HE2" {:avg 9.64, :stddev 2.6}, "HD2" {:avg 7.01, :stddev 0.43}, "HB2" {:avg 3.1, :stddev 0.36}, "HA" {:avg 4.61, :stddev 0.44}, "HB3" {:avg 3.04, :stddev 0.38}, "ND1" {:avg 195.02, :stddev 18.51}, "NE2" {:avg 182.48, :stddev 14.41}, "CA" {:avg 56.5, :stddev 2.31}, "CB" {:avg 30.25, :stddev 2.07}}, "GLY" {"N" {:avg 109.67, :stddev 3.88}, "CA" {:avg 45.35, :stddev 1.34}, "C" {:avg 173.87, :stddev 1.89}, "HA3" {:avg 3.9, :stddev 0.37}, "HA2" {:avg 3.98, :stddev 0.37}, "H" {:avg 8.33, :stddev 0.65}}, "ALA" {"N" {:avg 123.24, :stddev 3.54}, "CB" {:avg 19.01, :stddev 1.84}, "CA" {:avg 53.13, :stddev 1.98}, "C" {:avg 177.72, :stddev 2.14}, "HB" {:avg 1.35, :stddev 0.26}, "HA" {:avg 4.26, :stddev 0.44}, "H" {:avg 8.2, :stddev 0.6}}, "ARG" {"HH11" {:avg 6.91, :stddev 0.46}, "HH22" {:avg 6.76, :stddev 0.36}, "CD" {:avg 43.16, :stddev 0.88}, "HH12" {:avg 6.81, :stddev 0.32}, "C" {:avg 176.4, :stddev 2.03}, "CG" {:avg 27.21, :stddev 1.2}, "H" {:avg 8.24, :stddev 0.61}, "N" {:avg 120.8, :stddev 3.68}, "HG2" {:avg 1.57, :stddev 0.27}, "HG3" {:avg 1.54, :stddev 0.29}, "HD2" {:avg 3.12, :stddev 0.24}, "CZ" {:avg 159.98, :stddev 2.99}, "NE" {:avg 84.64, :stddev 1.7}, "NH1" {:avg 73.62, :stddev 4.35}, "HD3" {:avg 3.1, :stddev 0.26}, "NH2" {:avg 73.26, :stddev 3.32}, "HB2" {:avg 1.79, :stddev 0.27}, "HA" {:avg 4.3, :stddev 0.46}, "HB3" {:avg 1.76, :stddev 0.28}, "HE" {:avg 7.39, :stddev 0.64}, "CA" {:avg 56.77, :stddev 2.31}, "CB" {:avg 30.7, :stddev 1.83}, "HH21" {:avg 6.82, :stddev 0.48}}, "MET" {"CE" {:avg 17.07, :stddev 1.57}, "C" {:avg 176.19, :stddev 2.09}, "CG" {:avg 32.01, :stddev 1.23}, "H" {:avg 8.26, :stddev 0.6}, "N" {:avg 120.1, :stddev 3.57}, "HG2" {:avg 2.43, :stddev 0.35}, "HG3" {:avg 2.39, :stddev 0.38}, "HB2" {:avg 2.03, :stddev 0.34}, "HA" {:avg 4.41, :stddev 0.47}, "HB3" {:avg 1.99, :stddev 0.34}, "HE" {:avg 1.89, :stddev 0.47}, "CA" {:avg 56.11, :stddev 2.22}, "CB" {:avg 33.0, :stddev 2.24}}, "LEU" {"C" {:avg 176.98, :stddev 1.98}, "CG" {:avg 26.78, :stddev 1.11}, "H" {:avg 8.23, :stddev 0.65}, "N" {:avg 121.86, :stddev 3.9}, "CD1" {:avg 24.72, :stddev 1.59}, "CD2" {:avg 24.04, :stddev 1.7}, "HD1" {:avg 0.75, :stddev 0.28}, "HD2" {:avg 0.73, :stddev 0.29}, "HB2" {:avg 1.61, :stddev 0.34}, "HA" {:avg 4.32, :stddev 0.47}, "HB3" {:avg 1.52, :stddev 0.36}, "CA" {:avg 55.63, :stddev 2.12}, "CB" {:avg 42.32, :stddev 1.89}, "HG" {:avg 1.51, :stddev 0.33}}, "ASN" {"C" {:avg 175.26, :stddev 1.78}, "CG" {:avg 176.75, :stddev 1.37}, "H" {:avg 8.34, :stddev 0.63}, "N" {:avg 118.93, :stddev 4.02}, "HB2" {:avg 2.81, :stddev 0.31}, "HA" {:avg 4.67, :stddev 0.36}, "HB3" {:avg 2.76, :stddev 0.33}, "HD21" {:avg 7.35, :stddev 0.48}, "HD22" {:avg 7.14, :stddev 0.49}, "ND2" {:avg 112.75, :stddev 2.23}, "CA" {:avg 53.54, :stddev 1.88}, "CB" {:avg 38.69, :stddev 1.68}}, "CYS" {"N" {:avg 120.15, :stddev 4.64}, "CB" {:avg 32.54, :stddev 6.06}, "CA" {:avg 58.28, :stddev 3.35}, "C" {:avg 174.92, :stddev 2.07}, "HG" {:avg 1.91, :stddev 1.31}, "HB3" {:avg 2.89, :stddev 0.47}, "HB2" {:avg 2.95, :stddev 0.44}, "HA" {:avg 4.66, :stddev 0.55}, "H" {:avg 8.39, :stddev 0.67}}, "THR" {"C" {:avg 174.53, :stddev 1.75}, "H" {:avg 8.24, :stddev 0.62}, "N" {:avg 115.42, :stddev 4.75}, "CG2" {:avg 21.55, :stddev 1.12}, "HG1" {:avg 4.91, :stddev 1.58}, "HG2" {:avg 1.14, :stddev 0.23}, "HA" {:avg 4.46, :stddev 0.48}, "HB" {:avg 4.17, :stddev 0.33}, "CA" {:avg 62.22, :stddev 2.59}, "CB" {:avg 69.73, :stddev 1.65}}, "ASP" {"C" {:avg 176.39, :stddev 1.75}, "CG" {:avg 179.16, :stddev 1.81}, "H" {:avg 8.31, :stddev 0.58}, "N" {:avg 120.64, :stddev 3.87}, "HD2" {:avg 5.25, :stddev 0.58}, "HB2" {:avg 2.72, :stddev 0.26}, "HA" {:avg 4.59, :stddev 0.32}, "HB3" {:avg 2.66, :stddev 0.28}, "OD1" {:avg 179.66, :stddev 0.91}, "CA" {:avg 54.69, :stddev 2.03}, "CB" {:avg 40.88, :stddev 1.62}}})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Input: a map of atom data , and an atomname
;; Output: returns the avg / stdev for the atom in this residue.
;; Map atomname (Map statskey statsval) -> atomname -> Either 'no-stats-for-atom Number"
(defn get-stats
  "Map atomname (Map statskey statsval) -> atomname -> Either 'no-stats-for-atom Number"
  [stats atomname]
  (let [atomstats (stats atomname)]
   (if atomstats 
       (atomstats :avg)
       'no-stats-for-atom)))

(defn place-stats
  "Map atomname (Map key val) -> Map atomname (Map statskey statsval) -> Map atomname (Map key val)
   output includes new key of :avg"
  [atoms stats]
    ;; for each atom
    ;; find the standard data in stats
    ;; put that into the atom's map
  (into {} (for [[atomname atommap] atoms]
                [atomname (assoc-in atommap [:avg] (get-stats stats atomname))]))) ;; but what does this do if (stats atomname) is nil????

;;  input : A Proteinmap data structure .
;;	output : A Proteinmap with all of the input data ... but in addition, has a new key of :avg in each atom-data-map
;;	the value of :avg is either 'no-stats-for-atom or a number.
(defn merge-bmrb
  "Proteinmap -> Proteinmap
   protein's atoms now have avg shift stats"
  [prot]
    ;; for each residue in the protein
    ;; find the matching "standard residue" in the average stats
    ;;      use the aatype of the residue to look up the stats
  (let [shift-adder 
        (fn [res]
         (assoc-in res [:atoms] (place-stats (res :atoms) (stats-data (res :aatype))) ))
        new-residues (fmap shift-adder (prot :residues))]
   (into prot [[:residues new-residues]]))) ;; overwrite :residues in prot
