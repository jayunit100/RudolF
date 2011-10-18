(ns ConnjurRV.dataplot
  (:use [incanter.charts :only (histogram)])
  (:use [incanter.core :only (view)]))


;; code stubs for colbert to fill in

(defn make-histogram
  "[x] -> String -> HistogramPopup"
  [values x-label]
  (view                  ;; todo -- show x-label
   (histogram values)))

(defn make-bar-chart
  "Map key value -> String -> String -> BarchartPopup"
  [data x-label y-label]
  'todo)

(defn make-double-bar-chart
  "Map key (lvalue, rvalue) -> String -> String -> DoublebarchartPopup"
  [data x-label y-label]
  'todo)

(defn make-scatter-plot
  "[(xvalue, yvalue)] -> String -> String -> ScatterPopup"
  [data x-label y-label]
  'todo)