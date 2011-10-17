(ns BioClojure.connjurRV)


(defn parse-bmrb-stats
  "type :: String -> [Statsline]

   Statsline: map where keys are /Res, Name, Atom, Count, Min. Max., Avg., StdDev/"
  [string]
  (let [lines (split-lines string)
        headings (.split (first lines) " +")
        atomlines (rest lines)]
   (for [atomline atomlines]
    (zipmap headings (.split atomline " +")))))


(defn transform-stats
  "type :: [Statsline] -> Stats
   where Stats :: Map aatype (Map atomname (Map statname statvalue))

   NOTE:  ignores most of the data in the stats, and just pulls out the average chemical shift and std dev
   "
  [shift-stats]
  (let [f (fn [base linemap] 
              (assoc-in base 
                        [(linemap "Res") (linemap "Name")] 
                        {:avg (Float/parseFloat (linemap "Avg.")) 
                         :stddev (Float/parseFloat (linemap "StdDev"))}))] ;; refactor the return value ....
   (reduce f {} shift-stats))) 