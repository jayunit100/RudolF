(ns ConnjurRV.statistics
  (:use clojure.contrib.generic.functor))


(defn color-map
  "Map a Num -> Map a Color"
  [data]
  (fmap (fn [n] [0 0 n]) data))


(defn normalized-shifts
  "Map atomid (Map atomkey atomval) -> Map atomid norm-shift"
  [data]
  (let [division (fn [x y] (if (symbol? y) 1.99 (/ x y)))]
   (fmap (fn [dict] (division (dict :shift) (dict :avg))) data)))