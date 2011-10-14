(ns connjurRVTest
    (:use clojure.test))

(defn myfixture [block] 
    (do 
        (println "before test")
        (block)
        (println "after test")))

(use-fixtures :each myfixture)

(deftest mytest []
    (is (= 2 (+ 1 1))))

