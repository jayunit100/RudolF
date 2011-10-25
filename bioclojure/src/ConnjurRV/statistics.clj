(ns ConnjurRV.statistics
  (:use clojure.contrib.generic.functor))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; module interface
;	color-map
;	normalized-shifts
;	number-of-shifts
;	count-atoms-by-residue
;	get-atomid-shifts
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn color-map
  "Map a Num -> Map a Color"
  [data]
  (fmap (fn [n] [0 n 0]) data))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; for getting data into specific data structures

(defn normalized-shifts
  "Map atomid (Map atomkey atomval) -> Map atomid norm-shift"
  [atom-map]
  (let [safe-division (fn [x y] 
                          (if (symbol? y) 
                              y 
                              (/ x y)))]
   (into {} 
         (filter #(not (symbol? (second %))) 
                 (fmap (fn [dict] (safe-division (dict :shift) (dict :avg))) atom-map)))))

(defn norm-shifts-avg-shifts
  "[Map atomkey atomval] -> [(shift, avg-shift)]"
  [atom-list]
  (let [get-pair (fn [dict] [(dict :shift) (dict :avg)])]
   (filter #(and (number? (first %)) (number? (second %))) ; filter out pairs with nil
           (map get-pair atom-list))))


(defn number-of-shifts
  "Map rindex Residue -> Map rindex (shifts, avgs)"
  [data]
  (let [f (fn [res]
              (let [atomdict (res :atoms)
                    atoms (count atomdict)
                    avgs (count (filter #(not (symbol? (:avg (second %)))) atomdict))]
                    [atoms avgs]))]
   (fmap f data)))

(defn count-atoms-by-residue
  "Map rindex Residue -> Map rindex Integer"
  [res-map]
  (fmap #(count (% :atoms))
        res-map))

(defn get-atomid-shifts
  "Map atomid (Map atomkey atomval) -> Map atomid shift"
  [atom-map]
  (fmap :shift atom-map))
