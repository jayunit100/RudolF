(ns tests
    (:use clojure.test) (:use ConnjurRV.bmrbstats)) ;;<-- not that use methods in ns declaration don't use colon.
    

;;Callable functions, make sure there are no namespace errors
;;This function returns true or nil.
(defn callable? 
  [s] 
  (let [obj (try (eval (symbol s)) (catch Exception e))]
  (and obj (fn? obj))))

  
;;A Fixture is a scaffold for each test.  these statements are executed before/after the tests.
(defn myfixture [block] 
    (do 
		    (use 'ConnjurRV.bmrbstats)
        (println (resolve (symbol "parse-bmrb-stats")))
        (block)
        (println "   ")))

;;Fixtures are a clojure construct for wrapping tests. 
(use-fixtures :each myfixture) 

;;A negative control.  Tests that nonexistent methods do not exist)
(deftest testFailure [] 
		(is (= nil (callable? "pppppsdfsdfpppppppppppp") )) )

;;Tests that we can see matts code 
(deftest testNmrReader [] 
		(is (= true  (callable? "parse-bmrb-stats") )) )