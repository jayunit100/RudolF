(ns ConnjurRV.vizcharts
  (:use [incanter.charts :only (histogram bar-chart scatter-plot add-lines)])  
  (:use [incanter.core :only (view)])
  (:use [incanter.stats :only (pdf-normal)]))


;; code stubs for colbert to fill in



(defn make-histogram
  "[x] -> String -> HistogramPopup"
  [data xlabel]   
  ;{:pre [(list? data) (string? xlabel)]}
  (let [hist (histogram data :x-label xlabel) norm (pdf-normal data) ]	;; takes list and fits normal distribution to hitogram
   (view hist))) 
       ;;(add-lines hist norm))))             
   

(defn make-bar-chart
  "Map key value -> String -> String -> BarchartPopup"
  [map-data xlabel ylabel]
  ;{:pre [(map? map-data) (string? xlabel) (string? ylabel)]}
  (view
   (bar-chart (keys map-data) (vals map-data) 
              :x-label xlabel :y-label ylabel
              :legend true)))	



(defn make-double-bar-chart
  "Map key (lvalue, rvalue) -> String -> String -> DoubleBarChartPopup"
  [map-tuple-data x-label [y-label1 y-label2]] ; using destructuring
  ;{:pre [(map? map-tuple-data) (vector? first (vals map-tuple-data) ) (string? x-label) (string? y-label)]}
  'todo) 
;  (view
;   (bar-chart (keys map-tuple-data) (vals map-tuple-data) 
;              :x-label x-label :y-label y-label1   ;; make sure to use y-label2 also
;              :legend true)))	

 

( comment (defn make-scatter-plot-two-lists
  "[xvalue] -> [yvalue]-> String -> String -> ScatterPopup"
  [list-one list-two xlabel ylabel]
  ;{:pre [(list? list-one) (list? list-one) (string? xlabel) (string? ylabel)]}
  (let [lm (linear-model ($ list-one) ($ list-two ))]
   (doto 
           (view
            (scatter-plot list-one list-one
                         :x-label xlabel :y-label ylabel
                         :legend true))) (add-lines list-one (:fitted lm)) ))	)

  

(comment (defn make-scatter-plot-list-of-tuples
  "[(xvalue, yvalue)] -> String -> String -> ScatterPopup"
  [tuple-list x-label y-label]
  {:pre [(list? tuple-list) (vector? (first tuple-list) ) (string? xlabel) (string? ylabel)]}
  (let [ left (map first (tuple-list)) right (map last (tuple-list)) lm (linear-model ($ right) ($ left ))]
   (doto 
           (view
            (scatter-plot left right
                         :x-label xlabel :y-label ylabel
                         :legend true))) (add-lines left (:fitted lm)))	))

  


