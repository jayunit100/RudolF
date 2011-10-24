(defproject BioClojure "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
		 [commons-lang "2.3"]
                 [ring/ring-jetty-adapter "0.3.9"]
                 [jmol/jmol "12.0.49"]
		 [org.biojava/biojava3-core "3.0"]
		 [org.biojava/biojava3-structure "3.0"]
		 [org.biojava/biojava3-structure-gui "3.0"]
		 [incanter "1.2.3-SNAPSHOT"]
		 [lein-eclipse "1.0.0"]]
  :repositories [["biojava-maven-repo" "http://www.biojava.org/download/maven/"]])
