(use '[clojure.string :only (join split)])


;;This function exemplifies 
;; 1 reading text into clojure
;; 2 spliting a string and creating a regex using #
;; 3 forwarding arguments so your code is threaded and reads linearly using -> ->>
;; 4 the use of let : let binds a variable, which can then be returned at end of function.
;;regex=#"_|/" splits by _ and / 
(defn ll [regex] "gets a list"
   (let
	[ll

	 (-> ;;forward args as the first to each 
	    (slurp "resources/hino.txt") ;;read in the file 
	    (clojure.string/split #"\n") ;; split by new lines 
	    (->> (map #(clojure.string/split  % regex))) ;;forward input to last arg, not sure why this works. 
	 )
	]	
  ll 
  )
)
;;
;;This code exemplifies 
;;1 Parial functions : by sending the 1st arg to a function, you can then "complete" the function w/ 2nd arg in a map ;;2 Clojures facility for filtering maps by key "select-keys" 
;;3 the use of preconditions to make sure data is formatted (the input here is a list of strings.
;;4 the importance of being careful with maps and intermediate data types.  this functino becomes unweildy.
(defn tomat [l] "listtomatrix"
	{:pre [ (seq? l) ]} ;;precondition ensures that input is sequence 
	(let ;; just to linearize the workflow 
	  [
	   kv ( map (partial zipmap [:a :p :b :P :d :e :C :f :k :R]) l) ;;creates partial func, applies each el of list to the func to fulfill it. 
	   kv_pcr (map #(select-keys % [:P :C :R]) kv);;filters these keys 
	   ]
	 kv_pcr ;;return the filtered / selected map 
	)
	
)
