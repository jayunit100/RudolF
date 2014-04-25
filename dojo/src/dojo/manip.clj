;(ns dojo.manip)

(use 'clojure.contrib.str-utils2 )
(import java.io.File)


(defn read-dir
  "String -> [String]"
  [string]
  (map #(.getAbsolutePath %) 
       (.listFiles (File. string))))

(defn parse-file-name
  ""
  [string]
  (let [fields (.split string "_")]
    (zipmap [:bmrbid :peaks :shifts :isfinal] fields)))


(defn parse-shifts
  "type :: String -> [Cyanaline]
   where Cyanaline: map with keys /id, shift, error, atom, resid/"
  [text]
  {:pre [(string? text)]}
  (let [headers [:atomid :shift :error :atomname :resid]]
   (map #(zipmap headers (.split (trim %) " +")) ; on whitespace??
         (split-lines text))))

(defn write-line
  ""
  [d]
  (str (d :bmrbid)   "\t"
       (d :peaks)    "\t"
       (d :shifts)   "\t"
       (d :isfinal) "\t"
       (d :atomid)   "\t"
       (d :shift)    "\t"
       (d :error)    "\t"
       (d :atomname) "\t"
       (d :resid)    "\n"))
       

(defn print-file 
  ""
  [dict numb]
  (let [fname (str "shift_data/cleaned/tabular" numb ".txt")
        lines (map write-line dict)]
    (spit fname (apply str lines))))

(defn manip-data
  ""
  []
  (let [all-filenames (read-dir "shift_data")
        all-files (for [fpath all-filenames] 
                       (map #(merge (parse-file-name (.getName (File. fpath))) %)
                            (parse-shifts (slurp fpath))))]
   (map print-file all-files (range))))
    