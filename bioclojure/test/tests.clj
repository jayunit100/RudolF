(ns tests
    (:use clojure.test))

;;Callable functions, make sure there are no namespace errors
(defn callable? 
  [s] 
  (let [obj (try (eval (symbol s)) (catch Exception e))]
  (and obj (fn? obj))))
  

(defn myfixture [block] 
    (do 
        (use 'ConnjurRV.bmrbstats)
        (println (resolve (symbol "parse-bmrb-stats")))
        (block)
        (println "   ")))


;;Fixtures are a clojure construct for wrapping tests. 
(use-fixtures :each myfixture) 

;;A negative control.
(deftest testFailure [] 
		(is (= true (callable? "ppppppppppppppppp") )) )

;;test that we can see matts code 
(deftest testNmrReader [] 
		(is (= true (callable? "parse-bmrb-stats") )) )
