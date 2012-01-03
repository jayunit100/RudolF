;(ns src.dojo.mysqlscript)


(import java.io.File)
(use 'clojure.stacktrace)


(defn read-dir
  "String -> [String]"
  [string]
  (map #(.getAbsolutePath %) 
       (.listFiles (File. string))))


(defn make-command
  "String -> MySQL String"
  [filename]
  (str "load data local infile '" filename "' into table assignedshifts;\n"))


(defn make-script
  "warning:  this could produce extra WRONG statements in the mysql file because of hidden files !!!"
  [script-path]
  (let [all-files (read-dir "shift_data/cleaned")
        command-lines (map make-command all-files)]
   (spit script-path
         (apply str (cons "use dojo;\n" command-lines)))))

(defn example-make
  ""
  []
  (make-script "mysqlscript.txt"))

(defn run-mysql
  ""
  []
  (.. Runtime 
      (getRuntime) 
      (exec "mysql --user=root --password='' < mysqlscript.txt")))
