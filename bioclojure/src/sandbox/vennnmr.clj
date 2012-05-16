
(use 'clojure.contrib.str-utils2 
;     'clojure.contrib.generic.functor
     'clojure.stacktrace)

;; parse bmrb shift
;; parse shiftx shifts
;; compare shifts
;; load structure
;; display structure in jmol
;; plot comparison on jmol structure

(defn convert-types
  ""
  [the-map]
  {:id        (Integer/parseInt (the-map :id))
   :resid     (Integer/parseInt (the-map :resid))
   :restype   (the-map :restype)
   :atomtype  (the-map :atomtype)
   :nucleus   (the-map :nucleus)
   :shift     (Float/parseFloat (the-map :shift))
   :error     (the-map :error)
   :ambiguity (Integer/parseInt (the-map :ambiguity))})

(defn parse-bmrb
  "String -> [BMRBLine]"
  [string]
  (let [lines (split-lines string)
        header (first lines) ; ignore header ??
        headings [:id :resid :restype :atomtype :nucleus :shift :error :ambiguity]]
   (for [line (rest lines)] ; continue with the second line
    (convert-types 
     (zipmap headings (.split (trim line) "\\s+"))))))

(defn semantic-bmrb  ;; is this function right?
  "[BMRBLine] -> Something"
  [p]
  (let [f (fn [b n] (assoc-in b [(p :resid) (p :atomtype)] (p :shift)))]
    (reduce f {} parsed)))

(def shiftx-parsed (parse-bmrb (slurp "data/venn_nmr_pre/2L8L_shifts_shiftx.txt")))

(def bmrb-parsed (parse-bmrb (slurp "data/venn_nmr_pre/2L8L_shifts_bmrb.txt")))

(defn my-example
  ""
  [mpath]
  (parse-bmrb (slurp mpath)))
