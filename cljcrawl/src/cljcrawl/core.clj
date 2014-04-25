(ns cljcrawl.core)
(require '[clj-http.client :as client])
(use 'clojure.contrib.json)

;;This script exemplifies the basics of 
;;web requests and json parsing, in clojure.
;;Example output : 
;;user=> (getJsonFieldNames "https://graph.facebook.com/peerindex")
;;  (:can_post :company_overview :link :mission :name :likes :products :username :talking_about_count :founded :id :website :picture :category)


;;goes to a url and gets contents.
(defn getUrl [url] 
  (client/get url {:accept :json}))

;;goes to a url, gets contents, and returns the body only. 
(defn getBod [url]
  ((read-json (:body (client/get url)))))

;;goes to a url, gets contents, pasrses them as a json into a map, and gets the json keys only.
(defn getJsonFieldNames [url]
  (keys (read-json (:body (client/get url)))))

;; (time1 #(client/get "http://www.sportsci.com/topics2/AVI_files/APASgait0003.avi")) 
;; returns the time in seconds to run a function..
;; to repeatedly benchmark a function : 
;; (repeatedly 3 #(myFunction))
(defn time1 [f] ""
    (let [starttime (System/nanoTime)] 
          (f) 
          (/ (- (System/nanoTime) starttime) 1e9)))

