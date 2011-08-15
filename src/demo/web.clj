(ns demo.web (:use [ring.adapter.jetty] [BioClojure.core] ))

(defn app [req]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (str ["Welcome Mini-ClotifMiner : The Flagship App of the BioClojure Framework " (rand-motif-str (rand-int 15)) ] )   
 }
)

(defn -main []
  (let [port (Integer/parseInt (System/getenv "PORT"))]
    (run-jetty app {:port port})))
