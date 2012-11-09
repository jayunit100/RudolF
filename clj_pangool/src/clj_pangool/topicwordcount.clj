(ns clojure.examples.instance
 (gen-class
  :name rudolf.pangool.topicwordcount
  :implements [com.datasalt.pangool.tuplemr.TupleMapper]
  :prefix "impl-"
  :methods [[run int []]]))

 ;OVERRIDING 
 ;public int run(String[] args) throws Exception 
 (defn impl-run [array]
   0)

 ;public static void main(String[] args)
 ;ToolRunner.run(new SecondarySort(), args);
 (defn -main [args] (ToolRunner/run(. rudolf.pangool.topicwordcount args)))
 
 