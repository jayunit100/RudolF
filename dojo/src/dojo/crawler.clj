(ns dojo.crawler
  (:import (java.io InputStream InputStreamReader BufferedReader)
           (java.net URL HttpURLConnection)))

(defn get-url [url]
  (let [conn (.openConnection (URL. url))]
    (.setRequestMethod conn "GET")
    (.connect conn)                           
    (with-open [stream (BufferedReader.
                       (InputStreamReader. (.getInputStream conn)))] 
      (.length 
       (reduce #(.append %1 %2) 
               (StringBuffer.) 
               (line-seq stream)))))) ;; gets length of url

(defn get-urls [urls] 
  (let [agents (doall (map #(agent %) urls))]       ;; binds "agents" to a list of agents for each url 
    (doseq [agent agents] (send-off agent get-url)) ;; Sends off each agent in the list "agents" to get url
    (apply await-for 5000 agents)                   ;; takes all threads in agents and makes them wait 5 seconds. 
    (doall (map #(deref %) agents))))               ;; dereferences all the agents in the list agents

(defn time3 [f & args]
  (let [start (. System (nanoTime)) ;;reports a function time in ms
        j (apply f args)
        end (. System (nanoTime))]
   (/ (- end start) 1000000.0)))

(prn "now doing in parallel ") ;;Prints the time it takes to get the urls in parallel
(prn (time (get-urls '("http://lethain.com" "http://willarson.com" "http://lethain.com" "http://willarson.com" "http://lethain.com" "http://willarson.com"))))
(prn "now doing in series ")

(prn (+ 
 (time3 #(get-urls '("http://lethain.com" ))) 
 (time3 #(get-urls '("http://willarson.com")))
 (time3 #(get-urls '("http://lethain.com" )))
 (time3 #(get-urls '("http://willarson.com")))
 (time3 #(get-urls '("http://lethain.com" )))
 (time3 #(get-urls '("http://willarson.com")))))