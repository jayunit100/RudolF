(ns xl_clojure.core)
(use '(incanter core stats charts io))

;;Reads in a data set of the format
;;header : id	cs	pk	co	r3
;;[4.63 4.63 4.63 4.63 4.63]
;;[4.63 4.63 4.63 4.63 4.63]
;;[4.63 4.63 4.63 4.63 4.63]
;;... from a csv file (that is, a csv file wit 5 columns, each numeric)
(def go(read-dataset "resources/macloplot.csv" :header true))

(def chart (time-series-plot :pk :r3
                             :a-label "peaks"
                             :x-label "% completion"
			     :y-label "r-3"
                             :title "plot of r3"
                             :data go))

(print go) ;; debug chart contents, you should see keys n the header
(view chart);;these commands must be run here, or else use imported 
