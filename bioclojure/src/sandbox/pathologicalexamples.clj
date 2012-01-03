(use 'ConnjurRV.readcyana)
(use 'clojure.test)


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


(deftest parse-sequence-aa-length-no-check
  (is (every? #(= 3 (count %)) (parse-sequence "1\n2\n3")) "need three-letter names"))

(deftest parse-shifts-resids
  (is (every? number? (map :resid (parse-shifts shift-data))) "resids must be numbers"))

(deftest merge-shifts-num-res
  (let [count-residues (fn [prot] (count (:residues prot)))
        protein (sequence-to-protein (parse-sequence seq-data))
        shifts (parse-shifts shift-data)]
   (is (= (count-residues protein)
          (count-residues (merge-shifts protein shifts)))
       "number of residues the same")))

(deftest make-protein-size
  (let [seq (parse-sequence seq-data)
        count-residues (fn [prot] (count (:residues prot)))
        shifts (parse-shifts shift-data)]
   (is (= (count-residues (make-protein seq shifts))
          (count shifts))
       "make-protein correct size (number of residues)")))

(deftest parse-shifts-atomids
  (is (every? number? (map :id (parse-shifts shift-data))) "atomids must be numbers"))

(deftest parse-shifts-shifts
  (is (every? number? (map :shift (parse-shifts shift-data))) "shifts must be numbers"))

(deftest parse-shifts-errors
  (is (every? number? (map :error (parse-shifts shift-data))) "errors must be numbers"))
