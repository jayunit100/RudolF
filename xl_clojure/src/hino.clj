(use '[clojure.string :only (join split)])

;;returns a list with tuples like this 
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

;;output a matrix w headers
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


