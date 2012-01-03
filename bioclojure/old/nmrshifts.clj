(ns BioClojure.test.nmrshifts
  (:use [BioClojure.nmrshifts])
  (:use [clojure.test]))


; @Matt: are you following the clojure/'testing in clojure' conventions?
; are you using the test API properly/effectively?
; Refactor to make the tests more meaningful.
(deftest test-compare-atom
  "1. does it return a positive when ss-shift is greater than aa-shift
   2. does it return a negative when aa-shift greater
   3. does it return 0 when they're equal
   4. does it return 'not-in-ss when ss-shift is nil
   5. does it return 'not-in-aa when aa-shift is nil
   6. undefined when both are nil"
  []
  (and ; using test API????
   (> (compare-atom 8.3 4.2) 0)
   (< (compare-atom 345 3443) 0)
   (= (compare-atom 3.3 3.3) 0) ; does floating-point comparison work like in Java?  yikes
   (= (compare-atom nil 107.79) 'not-in-ss)
   (= (compare-atom 89.88 nil) 'not-in-aa)))