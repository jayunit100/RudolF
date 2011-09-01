(ns BioClojure.vennnmr
  (:use clojure.contrib.str-utils2))

(defn parse-bmrb-stats
  [string]
  (let [lines (split-lines string)]
   (let [headings (.split (first lines) " +")
         atomlines (rest lines)]
    (for [atomline atomlines]
     (zipmap headings (.split atomline " +"))))))

(defn parse-sequence
  [string]
  (split-lines (.trim string)))

(defn parse-shifts
  [string]
  (let [headers [:id :shift :error :atom :resid]]
   (map #(zipmap headers (.split (trim %) " +")) (split-lines string))))

(defn venn-nmr-help
  [seqstr shiftstr bmrbstr]
  (let [seq (parse-sequence seqstr)
        shifts (parse-shifts shiftstr)
        avg-shifts (parse-bmrb-stats bmrbstr)]
   (let [seq-shifts (merge-shifts seq shifts)] ;; merge-shifts
    (merge-with some-func avg-shifts seq-shifts)))) ;; some-func


(defn venn-nmr
  [seqfile shiftfile bmrbfile]
  (venn-nmr-help (.slurp seqfile)
                 (.slurp shiftfile)
                 (.slurp bmrbfile)))