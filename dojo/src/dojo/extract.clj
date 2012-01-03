;(ns dojo.extract
; (:import java.io.File))

(import java.io.File)
(use 'clojure.stacktrace)



(def the-dir "/Network/Servers/macsent.cluster.uchc.edu/Volumes/raid/Users/vyas/nmr/cyana/cyana_exec/pub")


(defn read-dir
  "String -> [String]"
  [string]
  (map #(.getAbsolutePath %) 
       (.listFiles (File. string))))

(defn read-cd
  ""
  []
  (read-dir "."))

(defn parse-path
  "String -> (id, peaks, shifts)"
  [string]
  (let [fields (.split string "_")]
    (println (apply str fields))
    [(nth fields 1)
     (nth fields 3)
     (nth fields 5)]))

(defn new-file-name
  "(id, peaks, shifts) -> bool -> String"
  [[id peaks shifts] isrescue]
  (str id "_" peaks "_" shifts "_" isrescue "_shifts.txt"))

(defn get-shifts
  "dir -> dir -> ()"
  [dir-path end-dir]
  (do
   (str "trying: " dir-path "<>" end-dir)
   (let [new-name-nf (str end-dir "/" (new-file-name (parse-path (.getName (File. dir-path))) 0))
         new-name-f (str end-dir "/" (new-file-name (parse-path (.getName (File. dir-path))) 1))
         nf-content (slurp (str dir-path "/connjur.prot"))
         f-content (slurp (str dir-path "/connjur-final.prot"))]
    (spit new-name-f f-content)
    (spit new-name-nf nf-content))))

(defn extract-files
  "start directory -> ???"
  [start-dir end-dir]
  (let [paths (read-dir start-dir)
        dir-paths (filter #(.isDirectory (File. %)) paths)]
    ; for each of the dir-paths:
    ;   parse the directory path 
    ;   read file ".../connjur.prot"
    ;   read file ".../connjur-final.prot"
    ;   
   (map #(get-shifts % end-dir) dir-paths))) ; not done

(defn example-usage
  ""
  []
  (extract-files the-dir "shift_data"))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn letfn-example
  ""
  []
  (letfn [(boog [x] 
               (if (= 0 (count x)) 
                   0 
                   (+ 1 (boog (rest x)))))] 
    (boog [1 2 3])))
