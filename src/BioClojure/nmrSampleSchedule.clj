(ns BioClojure.nmrSampleSchedule)

;;@matt : Can we move this to nmr_samplesched.clj for consistency ?
;;bash> git mv sampleSchedule.clj nmr_sampleSchedule.clj

;; Lee: Structs are deprecated, use a record (see 'defrecord') if you
;; actually need an object, or a map if you don't need an object

(defstruct schedule :points :num-dimensions)

; Matt:  resource for using records: http://groups.google.com/group/clojure/browse_thread/thread/94ed69c0cd6951b3
; records create java classes; structs may already be deprecated or may soon be
; records need to be :import (ed)
; why are structs deprecated?
; clojuredocs for defrecord:  http://clojuredocs.org/clojure_core/clojure.core/defrecord#example_454


(defn make-schedule
  [point-map num-dimensions]
  (let [points (keys point-map)
        is-dims-correct? (fn [pt]
                           (= num-dimensions
                              (count (pt :coordinates)) ; is the number of coordinates correct?
                              (count (pt :quadrature))))] ; is the number of quad things correct?
    (if (every? is-dims-correct? points)
      (struct schedule point-map num-dimensions)
      "oops -- there was a problem")))


(defn uniformly-sampled-grid
  [xlow xhigh ylow yhigh]
  (for [x (range xlow xhigh)
        y (range ylow yhigh)]
    {:coordinates (list x y) :quadrature (list \R \R)})) ;; I need this in a map ....


(def example
  (make-schedule
   (into {}
         (map (fn [x] [x 1])
              (uniformly-sampled-grid 1 10 1 10))) ; convert list to map (with default values of 1)
   2))

(defn format-point
  [pt]
  (reduce
   (fn [base next] (str base " " next))
   ""
   (concat
    (pt :coordinates)
    (pt :quadrature))))


(defn format-schedule
  [sched]
  (let [point-lines (map format-point (keys (sched :points)))]
    (reduce (fn [b n] (str b "\n" n)) point-lines)))
