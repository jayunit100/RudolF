(ns ConnjurRV.statistics
  (:use clojure.contrib.generic.functor))


(defn color-map
  "Map a Num -> Map a Color"
  [data]
  (fmap (fn [n] [0 0 n]) data))


(defn normalized-shifts
  "Map atomid (Map atomkey atomval) -> Map atomid norm-shift"
  [data]
  (let [safe-division (fn [x y] 
                          (if (symbol? y) 
                              y 
                              (/ x y)))]
   (into {} 
         (filter #(not (symbol? (second %))) 
                 (fmap (fn [dict] (safe-division (dict :shift) (dict :avg))) data)))))


(defn number-of-shifts
  "Map rindex Residue -> Map rindex (shifts, avgs)"
  [data]
  (let [f (fn [res]
              (let [atomdict (res :atoms)
                    atoms (count atomdict)
                    avgs (count (filter #(not (symbol? (:avg (second %)))) atomdict))]
                    [atoms avgs]))]
   (fmap f data))) 