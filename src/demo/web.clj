(ns demo.web
  (:use [ring.adapter.jetty]
        [BioClojure.seqUtils]
        [BioClojure.nmrSampleSchedule]))

(defn demo []
  (let [random_motif (apply str (rand-motif-str (rand-int 30)))
        random_aa (rand-aa-str (rand-int 40))
        translated_aa (aa-to-3-letter random_aa)]
    ["rand dna = " random_motif "; a  rand protein = " random_aa " which is  translated to " translated_aa]))

(defn demoPretty []
  (interpose "<BR> " (demo)))

(defn app [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str
          ["Welcome Mini-ClotifMiner : The Flagship App of the BioClojure Framework <BR></BR> demo:"
           (demo)
           "<H1>A Prettier Version : in one line of code, using ' interpose ' demoPretty:</H1>"
           (demoPretty)
           "<PRE>An NMR sampling schedule:"
           (format-schedule example)
           "</PRE>"])})

(defn -main []
  (let [port (Integer/parseInt (System/getenv "PORT"))]
    (run-jetty app {:port port})))
