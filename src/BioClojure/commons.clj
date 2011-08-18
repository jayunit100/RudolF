(ns BioClojure.util
  (:import (org.apache.commons.lang StringUtils)))

;; Capitalize Function
(defn makeCap "This makes capitals" [inString]
  (StringUtils/capitalize inString))

;; input s1, b1 b2 b3
(defn hasSubstring "has" [ main b ]
  (map #(.indexOf main %) b     ))

;;returns the "type" of a file - i.e. wether its a file or dir
(import '(java.io File))
(defn kind [filename]
  ;;create a new file by calling the File(filename) constructor)
  (let [f (File. filename)]
    (cond 
      (.isFile f) "file"
      (.isDirectory f) "directory"
      (.exists f) "other"
      :else "non-existent")))

;;aaa---



