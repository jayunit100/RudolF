(ns ConnjurRV.statistics
  (:use clojure.contrib.generic.functor))


(defn color-map
  "Map a Num -> Map a Color"
  [data]
  (fmap (fn [n] [0 0 n]) data))