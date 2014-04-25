;; Multi-classifier SVM with 21 classes (21 amino-acids) 
;; Test examples are peptide sequences with gaps represented by X
 


(ns AminoAcidPredictor     
   (:use clojure.contrib.combinatorics)
   (:use [clojure.contrib.generic.functor :only (fmap)])  
   (:use [incanter.charts                 :only (scatter-plot add-lines)])  
   (:use [incanter.core                   :only (view)])
   (:import (java.io FileReader BufferedReader))
   (:import (org.apache.commons.lang StringUtils)))  			 
       
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;  Static Definitions  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def amino-acids       ;; 21 amino acids including "U"
  ["R" "H" "K" "D" "E" "S" "T" "N" "Q" "C" "U" "G" "P" "A" "V" "I" "L" "M" "F" "Y" "W"])  

(def protein-neighbors ;; Sequence of all two letter words from the alphabet "amino-acids" 
	 (cartesian-product amino-acids amino-acids))

;; Creates a hash-map that associates a unique integer to each possible feature
(def protein-neighborhood (let [clump-2   (fn [[a b]] (str a b))  s (map clump-2 protein-neighbors)] 
  (zipmap (reduce into (fmap vec [ (map str s (repeat "01")) (map str s (repeat "02")) 
                                   (map str (repeat "1") s) (map str s (repeat "1")) 
                                   (map str amino-acids (repeat "1")) (map str amino-acids (repeat "2"))
                                   (map str  (repeat "1") amino-acids ) (map str (repeat "2") amino-acids) ]))                           
                       (range))))   
   
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;   Supporting functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; reports function evaluation time in ms
(defn bind-time [f & args]
  "function -> args -> evluation time"
  (let [start (. System (nanoTime))         
        j     (apply f args)
        end   (. System (nanoTime))]
                        (/ (- end start) 1000000.0)))

;; writes string f to a file (without quotes)
(defn out-file [f file-name]                
  {:pre [(string? f) (string? file-name)]}
  (binding [*out* (java.io.FileWriter. file-name)]                       
          (prn (symbol f))))

;; reads file and outputs contents as a string
(defn read-file [file-path]
 "File Path (string) - > contents (string)"
 {:pre [(string? name)]}
 (.trim (reduce str (line-seq (BufferedReader. 
                                (FileReader. file-path))))))

;; randomly repalaces n amino-acids in a protein string with "X" 
(defn x-aa [protein n]                       
 "protein (string) -> number of X replacements (int) -> protein with Xs (string)" 
  (reduce str (apply assoc (vec protein) 
                      (vec (interleave (repeatedly n #(rand-int (count protein))) (repeat n \X))))))

;; reads in a protein file then writes out same-file with animo-acids 
;; randomly replaced with Xs
(defn noise [protein-file proteinX-file n]   
   (let [protein (read-file protein-file)]   
     (out-file (x-aa protein n) proteinX-file))) 

;; Do not call by itself, will not terminate.
(defn rand-aa "Returns a random amino" []     
  (repeatedly #(rand-nth amino-acids)))

;; creates a random protein with "size" amino acids
;; writes to file with name file-out
(defn rand-protein [size file-out] 
 {:pre [(integer? size)]}          
  (out-file (apply str (take size (rand-aa))) file-out))

;; shuffles neighborhoods to always associate 
;; the same features about amino-acids 
(defn order-protein [repeats seed-vector file-out]   
 {:pre [(integer? repeats) (vector? seed-vector)]}   
      (out-file (apply str (repeatedly (int repeats) #(apply str (shuffle seed-vector)))) file-out))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; End supporting functions;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Below are  5 math functions from Mark Reid: http://mark.reid.name/sap/online-learning-in-clojure.html
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

(defn feature-index    ;; Get the unique index for a given feature
   "String -> Int"     ;; uses hash-map protein neighborhood as a function
   [protein-neighbor] 
   (protein-neighborhood protein-neighbor))
        
 
(defn neighborhood-seq
  "Peptide Sequence (String) -> Target amino-acid -> neighborhood length -> Neighborhoods "
  [protein target radius]   ;; returns length 5 neighborhood about target          
  (let [ r                     (apply str (repeat radius '.))
         target-regex          (reduce str [r "[" target "]" r])
	 neighborhood-sequence (re-seq (re-pattern target-regex) protein)]
         neighborhood-sequence))

(defn make [[a b _ c d]]       ; extracts features from the neighborhood  
 "match (String) -> tuple of neighborhood features"
 [(str 2 a) (str 1 b) (str 1 a b) (str b c 0 1 ) (str a d 0 2) (str c d 1) (str c 1) (str d 2)])

(defn index-tuple [[a b c d e f g h]]     
"feature vector (vector) -> sparse vector of local features extracted about an amino-acid (vector)"
 {(feature-index a) 1, (feature-index b) 1, (feature-index c) 1, (feature-index d) 1, 
  (feature-index e) 1, (feature-index f) 1, (feature-index g) 1 ,(feature-index h) 1})

(defn sparse-vec-seq [neighborhood-seq]   ;; returns sequence of sparse vectors
  "neighborhood-sequence (seq string) -> sparse-vector-sequence (seq {}) "
 (map (comp index-tuple make) neighborhood-seq))

(defn target-vectors [amino-acid protein]
  "amino-acid (char) -> protein (string) -> sparse-vector-sequence (seq {})"
 (sparse-vec-seq (neighborhood-seq protein amino-acid 2)))

(defn error-vectors [amino-acid protein]
   "amino-acid (char) -> protein (string) -> sparse-vector-sequence (seq {})"
 (sparse-vec-seq (neighborhood-seq protein (str "^" amino-acid) 2)))

(defn pos-example [sparse-vector]
 "Sparse vector, a map of key value pairs (map)-> Map assigning sparse vector to positive class (Map)"
  {:y  1 :x sparse-vector})

(defn neg-example [sparse-vector]
 "Sparse vector, a map of key value pairs (map)-> Map assigning sparse vector to positive class (Map)"
  {:y -1 :x sparse-vector})
 
(defn examples [target-vectors error-vectors] ;; A sequence of examples used for training the model in {:y sgn :x sparse vector} format
 (into (map pos-example target-vectors) (map neg-example error-vectors))) ;; examples are unordered and independent of each other

;; Given a file path reads a peptide sequence text file and turns it into training data
;; or given an amino acid a file path and option returns examples or target vectors for a given amino acid
(defn protein-to-examples 
 ([protein-file-path]  
  (let [ protein        (read-file protein-file-path)
         targets-matrix (into [] (map #(target-vectors % protein) amino-acids)) ; returns matrix of examples for all amino-acids
         errors-matrix  (into [] (map #(error-vectors % protein) amino-acids))
         example-matrix (into [] (map #(examples % %2) targets-matrix errors-matrix))] ; Specify data to get out                                        
                          example-matrix)) ; vector of examples ordered in the same way as the amino acids
 
      ;; overload: if amino-acid is specified return examples for that amino-acid               
      ([amino-acid protein-file-path data]   
      {:pre [ (integer? data) (or (= 0 data) (= 1 data)) ]} 
        (let [  protein  (read-file protein-file-path)
                targets  (target-vectors amino-acid protein)          
                errors   (error-vectors amino-acid protein)      
                examples (if (= data 0) (examples targets errors)
                                         targets) ]
                          examples)))    
 
;; Projects (inner product) a model onto a feature vector and returns the amino acid corresponding to
;; the model and the projection, in a tuple, if the projection is greater than 1.
;; This projections are used to sort candidates for a given feature vector based on projection.
(defn projection-tuple  
 "amino-acid (string) -> model (map) -> {string #} or nil" 
  [amino-acid w example] 
 (let [ projection  (inner w example)]                      
	 (if (<= 1 projection) [amino-acid projection] nil)))     

;; creates a vector of amino-acid projection pairs given an amino acid
;; its corresponding model and test data
(defn ordered-solution-vector 
  "amino acid (string) -> model (map) -> test-data (string)"
  [amino-acid model test-data]
   (vec (map #(projection-tuple amino-acid model %) test-data)))  




;; Functions hinge-loss and correct are from Mark Reid. See: http://mark.reid.name/sap/online-learning-in-clojure.html 

(defn hinge-loss ;; Returns the hinge loss of the weight vector w on the given example
	"model w (map) -> Training example (map) -> non-negative number   "
	[w example] (max 0 (- 1 (* (:y example) (inner w (:x example))))))
	
(defn correct  ;; Returns a corrected version of the weight vector w
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

;; creates a vector of models (sequence of vectors) for each of 21 amino acids in 
;; the same order as amino acids defined above
(defn create-model-matrix [file-path] 
 "File-path (string) -> ordered vector of 21 models (sequence of vectors)" 
  (let [  initial-model {:step 1, :w {}} 
          example-matrix (protein-to-examples file-path)
          model-matrix   (map #(:w (train initial-model %)) example-matrix) ]          
                         model-matrix))

;; Tests the projection of each model onto the features about each X in the 
;; test data. Note, test data is a string here not a file path
(defn ord-sol-matrix [model-matrix test-data]
  "sequence of vectors -> Test data string -> vector of tuples "
  {:pre [ (= (count model-matrix) (count amino-acids)) ]}
   (vec (map #(ordered-solution-vector %  %2 test-data) amino-acids model-matrix)))         

;; returns the column vectors of an m*n matrix: 
;; ([a11 a12 ... a1n] [a21 a22 ... a2n] ...) --->  ([a11 a21 ... an1] [a12 a22 ... an2]...)
(defn mat-cols [vec-seq]       
  (let [n   (count vec-seq)   
        m   (count (first vec-seq))
        f  #(into [] (map nth (vec %) (repeat n %2)))]    
                     (vec (map #(f vec-seq %) (range m)))))

;; Sorts proposed solutions by projection and concatenates into a string
(defn structure [mat-cols]
  (map (comp #(apply str %) #(map first %) #(sort-by second %)) mat-cols))
 
;; Returns a sequence of strings. Each string is ordered by projection from right to left 
(defn ord-sol-seq [training-file-path testing-file-path]
  (let [  model-matrix     (create-model-matrix training-file-path)
          test-data        (protein-to-examples "X"  testing-file-path 1)] ;; 1 returns features from test data          
          (structure (mat-cols (ord-sol-matrix model-matrix test-data)))))  

;;x is the protein with Xs, s the original, indicies is an index bin
(defn x-seq-index [x s indicies i]        
  (if (= x ((vec s) i)) 
          (into indicies [i]) 
                   indicies))

;; returns the indicies in actual protein corresponding to the X's
(defn findX [x s]      
  (reduce #(x-seq-index x s % %2) [] (range (count s)))) 

;; Finds the ith amino acid in test data without Xs corresponding to the ith X the test data with Xs
(defn actual [t tX]
  (apply str (vec (map #((vec t) %) (findX \X tX)))))

;; Retuns the ratio of how many gaps preducted correctly to the total number of gaps
(defn score [train test]                
  (let [ p        (ord-sol-seq train test)
         t        (read-file train)
         tX       (read-file test)        
         actual   (actual t tX)          
         correct  (map #(if (= % (last %2)) 1 0) actual p)]                
                          (assert (-> correct (= 0) (not))) 
                          (/ (reduce + correct) (count correct))))
;; See introduction
(defn com-pare [t rX r out]
 (out-file (str 
             (cons [:score (score t rX)] (map #(vec [% %2]) 
                                            (actual (read-file t) (read-file rX)) (ord-sol-seq r rX)))) out))
 
    
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

(defn show-plots [amino-acid train-file-path]
   (let [initial-model {:lambda 0.0001, :step 1, :w {}, :features [0], :errors [0]} 
     examples (protein-to-examples amino-acid train-file-path 0)     ;; 0 returns examples to train
     model (stats-model initial-model examples)]
      (stats model)))







		




