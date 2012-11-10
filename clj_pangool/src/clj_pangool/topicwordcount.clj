(ns clojure.examples.instance
 (gen-class
  :name rudolf.pangool.topicwordcount
  :implements [com.datasalt.pangool.tuplemr.TupleMapper]
  :prefix "impl-"
  :exposes {conf {:get getConf :set setConf}}
  :methods [[run int []]]))

;OVERRIDING 
;public int run(String[] args) throws Exception 
(defn impl-run []
  (if (not (= (length args) 2))
    -1 (run-with args)))

;;TODO Generate map/reducer classes.
(defn get-mapper [] )
(defn get-reducer [] )

(defn run-with [input output]
  (let [fields (. java.util.ArrayList) 
        _ (doto fields 
  		      (.add create com.datasalt.pangool.io.Fields/create "intField" (com.datasalt.pangool.io.Schema.Field.Type/valueOf "INT")
	  	      (.add create com.datasalt.pangool.io.Fields/create "strField" (com.datasalt.pangool.io.Schema.Field.Type/valueOf "STRING")
		        (.add create com.datasalt.pangool.io.Fields/create "longField" (com.datasalt.pangool.io.Schema.Field.Type/valueOf "LONG")
		        (.add create com.datasalt.pangool.io.Fields/create "doubleField" (com.datasalt.pangool.io.Schema.Field.Type/valueOf "DOUBLE"))))))
        schema (. com.datasalt.pangool.io.Schema fields)
        mr (. com.datasalt.pangool.io.TupleMRBuilder (getConf) "Pangool Secondary Sort (clojure)")]
        _  (doto mr 
             (addIntermediateSchema schema)
             (setGroupByFields "intField" "strField")
             ;mapreduce
             (addInput (org.apache.hadoop.fs.Path input) 
               (get-mapper))
             (setTupleReducer 
               (get-reducer))
             (setOutput (org.apache.hadoop.fs.Path output) 
               (. mapred.lib.output.HadoopOutputFormat output) 
               (. (org.apache.hadoop.io.Text/class) )
               (. (org.apache.hadoop.io.DoubleWritable/class) )))
        _ (.waitForCompletion (.createJob mr))))

;public static void main(String[] args)
;ToolRunner.run(new SecondarySort(), args);
(defn -main [args]
  ;invoke the "run" method with a new instance of topicwordcount class, constructed from the input args.
  (ToolRunner/run (. rudolf.pangool.topicwordcount args)))