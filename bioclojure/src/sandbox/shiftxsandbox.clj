
(use 'ConnjurRV.readshiftx)

(use 'ConnjurRV.vizcharts  
     'ConnjurRV.vizstruct  
     'ConnjurRV.modelreducer
     'ConnjurRV.statistics
     'ConnjurRV.readstats
     'ConnjurRV.readpdb
     'ConnjurRV.readcyana 
     'clojure.contrib.generic.functor
     'clojure.stacktrace)


(def text "NUM,RES,ATOMNAME,SHIFT
1,S,C,174.9481
1,S,CA,58.5588
1,S,CB,63.6231
1,S,H,8.3760
1,S,HA,4.3310
1,S,HB2,3.8554
1,S,HB3,3.6272
1,S,N,116.1368
2,D,C,176.6164
2,D,CA,55.8400
2,D,CB,39.6800
2,D,CG,179.4713
2,D,H,8.5104
2,D,HA,4.4688
2,D,HB2,2.7837
2,D,HB3,2.5845
2,D,N,120.8118
3,K,C,175.6029
3,K,CA,54.8900
3,K,CB,33.4696
3,K,CD,28.6781
3,K,CG,24.6600")

(def shifts (parse-shiftx text))

(def shifts-file (parse-shiftx (slurp "data/shiftx/2TRX.pdb.cs")))

(def shiftx-protein (semantic-shiftx shifts-file))


(defn atom-shifts-on-bar-chart
  "plot Map resid shift"
  [atomname]
  (let [func #(try (((% :atoms) atomname) :shift) (catch Exception e 0))
        resid-shifts (fmap func (get-residues-map shiftx-protein))]
   (make-bar-chart 
    resid-shifts
    "residue number"
    (str atomname " shift"))
   resid-shifts))

(def stats-shiftx (merge-bmrb shiftx-protein))

(defn norm-shifts-on-bar-chart-by-atom
  ""
  [atomname]
  (let [func #(try (/ (((% :atoms) atomname) :shift)
                      (((% :atoms) atomname) :avg))
                   (catch Exception e 0))
        resid-shifts (fmap func (get-residues-map stats-shiftx))]
   (make-bar-chart 
    resid-shifts
    "residue number"
    (str "normalized " atomname " shift"))))


