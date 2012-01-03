(defproject BioClojure "0.0.2-SNAPSHOT"
  :description "An application for visualizing NMR data on structures and plots"
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [commons-lang "2.3"]
                 [ring/ring-jetty-adapter "0.3.9"]
                 [jmol/jmol "12.0.49"]
                 [org.biojava/biojava3-core "3.0"]
                 [org.biojava/biojava3-structure "3.0"]
                 [org.biojava/biojava3-structure-gui "3.0"]
                 [incanter "1.2.3-SNAPSHOT"]]
  :repositories [["biojava-maven-repo" "http://www.biojava.org/download/maven/"]]
  :main ConnjurRV.core
  :uberjar-exclusions [#"META-INF/RCSB-PDB.SF" #"META-INF/SELFSIGN.SF"])
