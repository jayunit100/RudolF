;; Multi-classifier SVM with 21 classes (21 amino-acids) 
;; Test examples are peptide sequences with gaps represented by X
;; 
;; Run in REPL. No jar yet. 
;; The main functions are:

;; 1. solution-sequence: reports the most likely amino-acids to fill the gaps, trains on example sequence files
;;    input: [example-sequence-file test-sequence-file]
;;    output:[sequence of proposed amino-acids to fill gaps] May return multiple solutions per gap. In this case, the
;;    propsed solutions are ordered by the projection (inner-product) of their respective models onto the feature vector 
;;    representative of the gap. 

;; 2. show-plots: graphs learning statistics for a given amino acid using Incanter
;;    
;; See examples at bottom to use functions

(ns AminoAcidPredictor  
   (:use clojure.stacktrace) 
   (:use clojure.contrib.combinatorics)
   (:use [incanter.charts :only (scatter-plot add-lines)])  
   (:use [incanter.core :only (view)])
   (:import (java.io FileReader BufferedReader)))  			 
       
(def amino-acids   
  ["R" "H" "K" "D" "E" "S" "T" "N" "Q" "C" "U" "G" "P" "A" "V" "I" "L" "M" "F" "Y" "W"])  
   
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; Supporting functions;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn n-tuple-list [n list] "Integer- > TupleList -> List" 
 (map #(nth % n) list))

(defn stringseq [a b] "List -> List -> StringList"
 (map str a  b))

(defn stringseq-tuple [tuple-list] "TupleList -> StringList"
 (stringseq (map first tuple-list) (map second tuple-list)))

(defn bind-time [f & args]
  (let [start (. System (nanoTime)) ;;reports function time in ms
        j     (apply f args)
        end   (. System (nanoTime))]
                        (/ (- end start) 1000000.0)))

(defn random [repeats name] ;;repeats are multiples of 21, random data is of size 21*repeats
 {:pre [(integer? repeats) (string? name)]}
 (binding [*out* (java.io.FileWriter. name)]
          (prn (apply str (repeatedly (int repeats) #(apply str (shuffle amino-acids)))))))


(defn order [repeats seed-vector name]                          ;; interleaves and shuffles neighborhoods to always associate 
 {:pre [(integer? repeats) (string? name) (vector? seed-vector)]} ;; the same features about amino-acids
 (binding [*out* (java.io.FileWriter. name)]
          (prn (apply str (repeatedly (int repeats) #(apply str (shuffle seed-vector)))))))

(defn repeat-seed        ;; Data to run unit tests
 ([repeats seed name]    
 {:pre [(integer? repeats) (string? seed) (string? name)]}
 (binding [*out* (java.io.FileWriter. name)]
          (prn (reduce str (into [] (repeat (int repeats) seed ))))))
 ;;overload when seed is not provided
 ([repeats name]   
 {:pre [(integer? repeats) (string? name)]}
 (binding [*out* (java.io.FileWriter. name)]
          (prn (reduce str (into [] (repeat (int repeats) "KIALK")))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; End supporting functions;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Below are  5 math functions from Mark Reid: http://mark.reid.name/sap/online-learning-in-clojure.html
;; These will be replaced with parallel-colt libraries for increased speed
;; Sparse vectors are represented by maps with key value pairs. The key represents the index in the
;; sparse vector, and the value is the value of the index. Note, only nonzero values are stored. 

(defn add
	"Returns the sparse sum of two sparse vectors x y"
	[x y] (merge-with + x y))

(defn inner
	"Computes the inner product of the sparse vectors (hashes) x and y"
	[x y] (reduce + (map #(* (get x % 0) (get y % 0)) (keys y))))

(defn norm
	"Returns the l_2 norm of the (sparse) vector v"
	[v] (Math/sqrt (inner v v)))

(defn scale
	"Returns the scalar product of the sparse vector v by the scalar a"
	[a v] (zipmap (keys v) (map * (vals v) (repeat a))))

(defn project
  "Returns the projection of a parameter vector w onto the ball of radius r"
	[w r] (scale (min (/ r (norm w)) 1) w))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; End Math functions;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Below is a scheme to transform peptide sequences (FASTA format) into a training set that can 
;; train an SVM to fill gaps in peptide sequences

(def protein-neighbors ;; Sequence of all two letter words from the alphabet "amino-acids" 
	 (cartesian-product amino-acids amino-acids))

(def protein-neighborhood (let [ s (stringseq-tuple protein-neighbors)] ;; Create a hash-map that associates a unique integer
  (zipmap (into (stringseq s (repeat "1")) (stringseq s (repeat "2")))  ;; to each possible inner and outer neighbor
          (iterate inc 1))))

(defn get-protein-neighbor-index [protein-neighbor] ;; Get the unique index for a given inner/outer neighbor
  "String -> Int"
  (protein-neighborhood protein-neighbor))

(defn neighborhood-seq
  "Peptide Sequence (String) -> Neighborhoods "
  [protein target]   ;; returns length 5 neighborhood about target
  (let [ target-regex (reduce str [".." "[" target "]" ".."])
	 neighborhood-sequence (re-seq (re-pattern target-regex) protein)]
         neighborhood-sequence))

(defn make [matches] ;; extract inner and outer neighbors from the neighborhood
 "sequence of neighborhoods (String-seq) -> length 2 sequence of features in neighborhood"
 [(str (nth matches 1) (nth matches 3) 1 ) (str (first matches) (last matches)  2)])

(defn index-tuple [made]                          ;; return length 2 sparse vector with two nonzero values 
;;Feature Vector -> sparse vector with unit value ;; corresponding to the neighborhood features extracted about an amino-acid
 {(get-protein-neighbor-index (first made)) 1, (get-protein-neighbor-index (second made)) 1})

(defn sparse-vec-seq [neighborhood-seq]   ;; returns sequence of sparse vectors
  "neighborhood-sequence (seq string) -> sparse-vector-sequence (seq {}) "
 (map (comp index-tuple make) neighborhood-seq))

(defn target-vectors [amino-acid protein]
  "amino-acid (char) -> protein (string) -> sparse-vector-sequence (seq {})"
 (sparse-vec-seq (neighborhood-seq protein amino-acid)))

(defn error-vectors [amino-acid protein]
   "amino-acid (char) -> protein (string) -> sparse-vector-sequence (seq {})"
 (sparse-vec-seq (neighborhood-seq protein (str "^" amino-acid))))

(defn pos-example [sparse-vector]
 "Sparse vector, a map of key value pairs (map)-> Map assigning sparse vector to positive class (Map)"
  {:y  1 :x sparse-vector})

(defn neg-example [sparse-vector]
 "Sparse vector, a map of key value pairs (map)-> Map assigning sparse vector to positive class (Map)"
  {:y -1 :x sparse-vector})
 
(defn examples [target-vectors error-vectors] ;; A sequence of examples used for training the model in {:y sgn :x sparse vector} format
 (into (map pos-example target-vectors) (map neg-example error-vectors))) ;;examples are unordered and independent of each other

(defn read-file [file-path]
 "File Path (string) - > file (string)"
 {:pre [(string? name)]}
 (reduce str (line-seq (BufferedReader. 
                         (FileReader. file-path)))))

(defn protein-to-examples 
 ([protein-file-path]  ;;reads a peptide sequence text file and turns it into training data
  (let [ protein        (read-file protein-file-path)
         targets-matrix (into [] (map #(target-vectors % protein) amino-acids)) ;;returns matrix of examples for all amino-acids
         errors-matrix  (into [] (map #(error-vectors % protein) amino-acids))
         example-matrix (into [] (map #(examples % %2) targets-matrix errors-matrix)) ];;Specify data to get out                                        
                          example-matrix)) ;;vector of examples ordered in the same way as the amino acids
 
        ;;overload: if amino-acid is specified return examples for that amino-acid               
      ([amino-acid protein-file-path data]   
      {:pre [ (integer? data) (or (= 0 data) (= 1 data)) ]} 
        (let [  protein  (read-file protein-file-path)
                targets  (target-vectors amino-acid protein)          
                errors   (error-vectors amino-acid protein)      
                examples (if (= data 0) (examples targets errors)
                                         targets)]
                          examples)))    

(defn amino-acid-hinge ;; 
 [amino-acid w example] 
 "amino-acid (string) -> model (map) -> amino-acid or "" " ;; projection should be greater than or equal to one 
 (let [ projection  (inner w example)]                     ;; for positive examples
	 (if (<= 1 projection) amino-acid "")))                  ;; return amino-acid, empty string otherwise
 
(defn projection-map ;; 
 [amino-acid w example] 
 "amino-acid (string) -> model (map) -> {} or nil " ;; projection should be greater than or equal to one 
 (let [ projection  (inner w example)]            ;; if (projection => 1) store in map
	 (if (<= 1 projection) {amino-acid projection} nil)))                ;; sort by the projection

(defn conflict-solution-vector 
  [amino-acid model test-data]
   (map #(projection-map amino-acid model %) test-data))  

(defn solution-vector 
  [amino-acid model test-data]
   (map #(amino-acid-hinge amino-acid model %) test-data))  


;;The following is an SVM algorithm inspired by one described by Mark Reid 
;;
;;Functions hinge-loss and correct are from Mark Reid. See: http://mark.reid.name/sap/online-learning-in-clojure.html 

(defn hinge-loss ;; Returns the hinge loss of the weight vector w on the given example
	"model w (map) -> Training example (map) -> non-negative number   "
	[w example] (max 0 (- 1 (* (:y example) (inner w (:x example))))))
	
(defn correct  ;;Returns a corrected version of the weight vector w
	[w example t lambda]
	(let [x   (:x example)
		  y   (:y example)
		  w1  (scale (- 1 (/ 1 t)) w)
		  eta (/ 1 (* lambda t))
		  r   (/ 1 (Math/sqrt lambda))]
		(project (add w1 (scale (* eta y) x)) r)))

(defn update
	"folding step to train the model"
	[model example]
	(let [ t        (:step   model)
	       w        (:w      model)             
	       fix      (> (hinge-loss w example) 0) ]
   
	       { :w     (if fix (correct w example t 0.0001) w),			  
	         :step     (inc t)}))

(defn train
	"Returns a model trained from the initial model on the given examples"
	[initial-model examples]
	(reduce update initial-model examples))

(defn create-model-matrix [file-path]
  (let [  initial-model {:step 1, :w {}} 
          example-matrix (protein-to-examples file-path)
          model-matrix   (map #(:w (train initial-model %)) example-matrix) ]           
                         model-matrix))
 
(defn solution-matrix [model-matrix test-data]
  {:pre [ (= (count model-matrix) (count amino-acids)) ]}
   (map #(solution-vector %  %2 test-data) amino-acids model-matrix))      
    

(defn solution-sequence [training-file-path testing-file-path]
  (let [  model-matrix     (create-model-matrix training-file-path)
          test-data        (protein-to-examples "X"  testing-file-path 1) ] ;; 1 returns features from test data          
          (reduce #(map str % %2) (solution-matrix model-matrix test-data))))   
   

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;Stats;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn build-stats
	"Returns an updated model by taking the last model, the next training 
	 and applying the Pegasos update step. Allocates errors and features indexed by each time step"
	[model example]
	(let [      lambda   (:lambda model)
		    t        (:step   model)
		    w        (:w      model)
                    features (count (keys (:w model)))
		    errors   (:errors model)
		    error    (> (hinge-loss w example) 0)]
   
			{ :w        (if error (correct w example t lambda) w), 
			  :lambda   lambda, 
			  :step     (inc t), 
                          :features (into (:features model) [features])
			  :errors   (into  errors  [(if error (inc (last errors)) (last errors))]) } ))

(defn stats-model
	"Returns statistics about the trained model and the model itself"
	[initial-model examples]
	(reduce build-stats initial-model examples))

(defn list-scatter-plot ;; Makes a scatter plot from two number lists (Incanter)
  "x-list-> y-list -> x-label (String) -> y-label (String)-> ScatterPopup"
  [x-list y-list  x-label y-label]  
   (view
    (scatter-plot x-list y-list
                  :x-label x-label 
                  :y-label y-label
                  :legend true)))	
(defn stats
	"Returns a scatter plot of correction step against the percent correct "
	[model]
	(let [	      last-step     (:step model) ;;fit model here and measure curvature
		      steps         (take (:step model) (iterate inc 1))
		      features      (:features model)
		      errors        (:errors model) 
                      error-to-step (map #(- 1 (/ (float %) %2)) errors steps) ]                      
			(list-scatter-plot  steps  error-to-step "Steps" "Percentage Correct")
                        (list-scatter-plot  steps  features "Steps" "Features")
                        (list-scatter-plot  steps  errors "Steps" "Errors")))

(defn show-plots [amino-acid file-path]
   (let [initial-model {:lambda 0.0001, :step 1, :w {}, :features [0], :errors [0]} 
     examples (protein-to-examples amino-acid file-path 0)     ;; 0 returns examples to train
     model (stats-model initial-model examples)]
      (stats model))) 

 ;; Use the following functions in the repl (assuming Anole.txt and AnoleX.txt are in your current directory):

 ;; (solution-sequence "Anole.txt" "AnoleX.txt")
 ;; (solution-matrix (create-model-matrix "Anole.txt") (protein-to-examples "X"  "AnoleX.txt" 1))
 ;; (order 10 ["LRKDC" "YDEST" "ESGPI"] "order.txt") artificially generated sequence data
 ;; (show-plots "A" "Anole.txt") 


		





