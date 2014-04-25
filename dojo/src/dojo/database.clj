(ns dojo.database
  (:import (java.io InputStream InputStreamReader BufferedReader)
           (java.net URL HttpURLConnection)))

(use 'clojure.contrib.sql)              

;;hino, dojo table names.
(let [db-host "marcie"
      db-port 3306
      db-name "dojo"]
 (def db {:classname "com.mysql.jdbc.Driver"
          :subprotocol "mysql"
          :subname (str "//" db-host ":" db-port "/" db-name)
          :user "colbert"
          :password "colbertlikesmath"})
  (with-connection db
   (with-query-results rs ["select * from dojo"]
     (dorun (map #(println (:language :iso_code %)) 
                 rs)))))
	

    ; rs will be a sequence of maps,
    ; one for each record in the result set.