(ns BioClojure.nmrSampleSchedule)


(defstruct schedule :points :num-dimensions)

;;@matt : Can we move this to nmr_samplesched.clj for consistency ? 
;;bash> git mv sampleSchedule.clj nmr_sampleSchedule.clj
(defn make-schedule
  [ point-map num-dimensions ]
  (let [points (keys point-map)
        is-dims-correct? (fn [pt] 
                           (= num-dimensions 
                             (count (pt :coordinates)) ; is the number of coordinates correct?
                             (count (pt :quadrature))))] ; is the number of quad things correct?
    (if (every? is-dims-correct? points)
      (struct schedule point-map num-dimensions)
      "oops -- there was a problem")))


(defn uniformly-sampled-grid
  [ xlow xhigh ylow yhigh ]
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
  [ pt ]
  (reduce 
    (fn [b n] (str b " " n)) 
    "" 
    (concat 
      (pt :coordinates)
      (pt :quadrature))))
  

(defn format-schedule
  [ sched ]
  (let [point-lines (map format-point (keys (sched :points)))]
    (reduce (fn [b n] (str b "\n" n)) point-lines)))
