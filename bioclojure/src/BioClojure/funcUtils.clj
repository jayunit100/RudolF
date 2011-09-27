(ns BioClojure.funcUtils
  (:use clojure.contrib.math))

(defn lift ;; this function is very similar to partially applying 'map'
  [f]
  (fn [l]
    (map f l)))

(defn plustwo ;; `plustwo` consumes a single number
  [x]
  (+ x 2))


(def lifted_function (lift plustwo)) ;; the lifted version consumes a list of numbers


(def example
  (lifted_function (list 1 2 3 4 5 6)))  ;; here it is in action

(def example2
  ((lift (fn [x] (expt x 2))) ;; 'lift' an anonymous function to create a function over a list
   (range 1 11))) ;; the list that is passed to the lifted function
