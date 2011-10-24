(ns ConnjurRV.vizcharts
  (:use [incanter.charts :only (histogram bar-chart scatter-plot add-lines)])  
  (:use [incanter.core :only (view)])
  (:use [incanter.stats :only (pdf-normal linear-model)]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; module interface
;	make-histogram        ::  [x] -> String -> HistogramPopup
;	make-bar-chart        ::  Map key value -> String -> String -> BarchartPopup
;	make-double-bar-chart ::  Map key (lvalue, rvalue) -> String -> String -> DoubleBarChartPopup
;	make-scatter-plot     ::  [(xvalue, yvalue)] -> String -> String -> ScatterPopup
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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
  (let [sorted-data (sort-by first map-data)
        x-vals (map first sorted-data)
        y-vals (map second sorted-data)]
   (view
    (bar-chart x-vals
               y-vals
               :x-label xlabel 
               :y-label ylabel
               :legend true))))


;(comment 
(defn make-double-bar-chart
  "Map key (lvalue, rvalue) -> String -> String -> DoubleBarChartPopup"
  [map-tuple-data x-label [y-label1 y-label2]] ; using destructuring
  ;{:pre [(map? map-tuple-data) (vector? first (vals map-tuple-data) ) (string? x-label) (string? y-label)]}
  'todo) 
  ;;(view
   ;;(bar-chart (keys map-tuple-data) (vals map-tuple-data) 
;              :x-label x-label :y-label y-label1   ;; make sure to use y-label2 also
;              :legend true)))	
;)


(defn make-scatter-plot
  "[(xvalue, yvalue)] -> String -> String -> ScatterPopup"
  [tuple-list x-label y-label]
  (let [lefts (map first tuple-list) 
        rights (map second tuple-list)]
   (view
    (scatter-plot lefts rights
                  :x-label x-label 
                  :y-label y-label
                  :legend true))))	

