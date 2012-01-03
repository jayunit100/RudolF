(ns ConnjurRV.test.readcyana
  (:use clojure.test)
  (:use ConnjurRV.readcyana))

;; testing info:
; http://richhickey.github.com/clojure/clojure.test-api.html
; points:
;	1) use (is (...))
;	2) documentation: (is (...) "some string")
;	3) (is (thrown? ...)) for exceptions
;	4) (with-test ...) is weird
;	5) (defn test-ns-hook ...)  :  run tests in a certain order
;	6) (use-fixtures :each f)
;	7) (use-fixtures :once f)
;	8) fixtures take a test function as an argument

(def shift-data "   901 4.747 0.0 HA 82 
   864 32.119 0.0 CB 78 
   1057 122.839 0.0 N 95 
   807 25.72 0.0 CD1 74 
   660 3.215 0.0 HB3 61 
   191 0.146 0.0 HD22 19 
   174 0.718 0.0 HD23 18 
   1361 58.524 0.0 CA 121 
   741 3.998 0.0 HA 69 
   342 175.96 0.0 C 34 
   1004 2.985 0.0 HB3 92 
   822 4.769 0.0 HA 76 
   776 42.88 0.0 CB 72 
   27 28.658 0.0 CB 3 
   619 2.609 0.0 HB3 57 
   1170 33.628 0.0 CB 106 ")

(def seq-data "GLN
GLY
MET
GLU
VAL
SER
ALA
ASN
GLU
LEU
GLU
ALA
ALA
SER
SER
ARG")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest parse-shifts-length
  (is (= 16 (count (parse-shifts shift-data))) "number of lines recognized in shift data"))

(deftest parse-shifts-dict-size
  (is (every? #(= 5 (count %)) (parse-shifts shift-data)) "each line leads to a map with 5 pairs"))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest parse-sequence-length
  (is (= 16 (count (parse-sequence seq-data))) "number of lines recognized in sequence data"))

(deftest parse-sequence-aa-length
  (is (every? #(= 3 (count %)) (parse-sequence seq-data)) "need three-letter names"))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest protein-from-sequence-is-map
  (is (map? (sequence-to-protein (parse-sequence seq-data))) "???"))

(deftest protein-from-sequence-has-3-keys
  (is (= 3 (count (sequence-to-protein (parse-sequence seq-data)))) "???"))

(deftest protein-from-sequence-has-16-residues
  (let [residues (:residues (sequence-to-protein (parse-sequence seq-data)))]
   (is (= 16 (count residues)) 
       (str "have " (count residues) " residues"))))

(deftest protein-from-sequence-has-keys-1-16
  (let [residues (:residues (sequence-to-protein (parse-sequence seq-data)))]
   (is (every? #(not (nil? %)) 
               (map residues (range 1 17))) 
       (str "has keys: " (vals residues)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;




