(ns ConnjurRV.test.modelreducer
  (:use clojure.test)
  (:use ConnjurRV.modelreducer))

(def test-protein {:residues {32 {:aatype "MET", :atoms {"H" {:avg 8.26, :shift 8.704, :id 324}, "N" {:avg 120.1, :shift 122.625, :id 325}}}, 64 {:aatype "ASN", :atoms {"C" {:avg 175.26, :shift 176.82, :id 694}, "H" {:avg 8.34, :shift 7.698, :id 688}, "N" {:avg 118.93, :shift 119.384, :id 697}, "HB2" {:avg 2.81, :shift 2.951, :id 690}, "HA" {:avg 4.67, :shift 4.798, :id 689}, "HB3" {:avg 2.76, :shift 3.168, :id 691}, "HD21" {:avg 7.35, :shift 6.936, :id 692}, "HD22" {:avg 7.14, :shift 7.734, :id 693}, "ND2" {:avg 112.75, :shift 113.408, :id 698}, "CA" {:avg 53.54, :shift 56.908, :id 695}, "CB" {:avg 38.69, :shift 39.218, :id 696}}}, 96 {:aatype "PHE", :atoms {"C" {:avg 175.41, :shift 171.75, :id 1066}, "H" {:avg 8.36, :shift 8.865, :id 1058}, "N" {:avg 120.47, :shift 120.365, :id 1073}, "CE1" {:avg 130.7, :shift 130.76, :id 1071}, "CE2" {:avg 130.75, :shift 130.76, :id 1072}, "CD1" {:avg 131.57, :shift 131.6, :id 1069}, "CD2" {:avg 131.63, :shift 131.6, :id 1070}, "HE1" {:avg 7.08, :shift 7.28, :id 1064}, "HD1" {:avg 7.06, :shift 6.815, :id 1062}, "HE2" {:avg 7.08, :shift 7.28, :id 1065}, "HD2" {:avg 7.06, :shift 6.815, :id 1063}, "HB2" {:avg 3.0, :shift 2.068, :id 1060}, "HA" {:avg 4.63, :shift 5.374, :id 1059}, "HB3" {:avg 2.93, :shift 2.567, :id 1061}, "CA" {:avg 58.08, :shift 55.736, :id 1067}, "CB" {:avg 39.98, :shift 41.738, :id 1068}}}, 128 {:aatype "ASP", :atoms {"CA" {:avg 54.69, :shift 55.945, :id 1432}, "HA" {:avg 4.59, :shift 4.298, :id 1429}, "HB3" {:avg 2.66, :shift 2.483, :id 1431}, "CB" {:avg 40.88, :shift 42.247, :id 1433}, "H" {:avg 8.31, :shift 7.901, :id 1428}, "N" {:avg 120.64, :shift 129.886, :id 1434}, "HB2" {:avg 2.72, :shift 2.607, :id 1430}}}, 1 {:aatype "MET", :atoms {"C" {:avg 176.19, :shift 176.41, :id 6}, "CG" {:avg 32.01, :shift 36.326, :id 9}, "HG2" {:avg 2.43, :shift 2.222, :id 4}, "HG3" {:avg 2.39, :shift 2.222, :id 5}, "HB2" {:avg 2.03, :shift 1.908, :id 2}, "HA" {:avg 4.41, :shift 4.155, :id 1}, "HB3" {:avg 1.99, :shift 2.003, :id 3}, "CA" {:avg 56.11, :shift 57.135, :id 7}, "CB" {:avg 33.0, :shift 29.815, :id 8}}}, 33 {:aatype "ARG", :atoms {"CD" {:avg 43.16, :shift 43.145, :id 336}, "C" {:avg 176.4, :shift 177.29, :id 333}, "CG" {:avg 27.21, :shift 27.53, :id 337}, "HG2" {:avg 1.57, :shift 0.87, :id 331}, "HG3" {:avg 1.54, :shift 1.176, :id 332}, "HD2" {:avg 3.12, :shift 2.895, :id 329}, "HD3" {:avg 3.1, :shift 2.824, :id 330}, "HB2" {:avg 1.79, :shift 0.908, :id 327}, "HA" {:avg 4.3, :shift 3.481, :id 326}, "HB3" {:avg 1.76, :shift 1.468, :id 328}, "CA" {:avg 56.77, :shift 60.318, :id 334}, "CB" {:avg 30.7, :shift 29.362, :id 335}}}, 65 {:aatype "ARG", :atoms {"CD" {:avg 43.16, :shift 43.29, :id 710}, "C" {:avg 176.4, :shift 177.82, :id 707}, "CG" {:avg 27.21, :shift 27.67, :id 711}, "H" {:avg 8.24, :shift 8.562, :id 699}, "N" {:avg 120.8, :shift 122.45, :id 712}, "HG2" {:avg 1.57, :shift 1.16, :id 705}, "HG3" {:avg 1.54, :shift 1.16, :id 706}, "HD2" {:avg 3.12, :shift 2.83, :id 703}, "HD3" {:avg 3.1, :shift 2.83, :id 704}, "HB2" {:avg 1.79, :shift 0.906, :id 701}, "HA" {:avg 4.3, :shift 3.481, :id 700}, "HB3" {:avg 1.76, :shift 1.467, :id 702}, "CA" {:avg 56.77, :shift 59.999, :id 708}, "CB" {:avg 30.7, :shift 29.462, :id 709}}}} :name nil :source nil})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest test-get-residues-map
  (let [res-map (get-residues-map test-protein)]
   (is (map? res-map) "is it a map?")
   (is (= (count res-map) (count (test-protein :residues)))
       "number of residues in map")
   (is (every? #(and (integer? %) (> % 0))
               (keys res-map))
       "keys are integers")
   (is (every? #(map? %)
               (vals res-map))
       "values are maps")))


(deftest test-get-residues-list
  (let [res-list (get-residues-list test-protein)]
   (is (= (count res-list)
          (count (test-protein :residues)))
       "number of residues in list")
   (is (every? map? res-list)
       "each element is a map")
   (is (every? #(and (% :rindex) (% :aatype)) res-list)
       "each element has keys :rindex, :aatype at least")))


(deftest test-get-atoms-map
  (let [atom-map (get-atoms-map test-protein)]
   (is (every? #(and (integer? %) (> % 0))
               (keys atom-map))
       "keys are integer")))


(deftest test-get-atoms-list
  (let [atom-list (get-atoms-list test-protein)]
   (is (every? map? atom-list)
       "all elements are maps")))
