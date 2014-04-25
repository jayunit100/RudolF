;;Tests for ConnjurRV 
(ns ConnjurRV.test.app
    (:use clojure.test) 
    (:use ConnjurRV.readstats) 
    (:use ConnjurRV.readcyana)) 
    
;;Utility method, that makes sure there are no namespace errors
;;It is used to test that the methods are named correctly and namespaces are working normally.
(defn callable? 
  [s] 
  (let [obj (try (eval (symbol s)) (catch Exception e))]
  (and obj (fn? obj))))
  
;;A Fixture is a scaffold for each test.  these statements are executed before/after the tests.
(defn myfixture [block] 
    (do 
		    (use 'ConnjurRV.readstats)
        (println (resolve (symbol "parse-bmrb-stats")))
        (block)
        (println "   ")))

;;Fixtures are a clojure construct for wrapping tests. 
(use-fixtures :each myfixture) 

;;A negative control.  Tests that nonexistent methods do not exist)
;;(deftest testFailure [] 
;;		(is (= nil (callable? "pppppsdfsdfpppppppppppp") )) )

;;Tests that we can see matts code 
;;There should be a call to one method from each class.
;;Returns nill if false
(deftest testClasses [] 
    (is 
      (if (callable? "merge-bmrb") true false)))

;;Tests that there are 20 residues in the bmrb stats data .
(deftest testBmrbStatsData [] 
		(is (= 20 (count stats-data))))

;;Make a protein to test the merge method.
(def proteinMap1 (make-protein-from-files "data/connjur.seq" "data/connjur.prot"))

;;Make sure the protein is a map data structure.
(deftest testProteinMap
  (map? proteinMap1))

;;The coup de gras : adding bmrb data to a structures atoms.
;;This should return a map with 3 keys.
(deftest testBmrbMerge
  (is (= 3 (count (keys (merge-bmrb proteinMap1))))))