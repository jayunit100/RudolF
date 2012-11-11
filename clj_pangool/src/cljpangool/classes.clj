(ns cljpangool.classes)


; here's one example where
; we create an object that extends
; Object and overrides the 'toString' 
; method.  usage:
; > (.toString (my-proxy))
; > (.equals (my-proxy) 1)
(defn my-proxy
  []
  (proxy [Object] []
    (toString [] "hi there")))

; here's an example where we pass in arguments
; to the constructor of the proxy object
(defn cons-proxy
  [x y]
  (proxy [Object] []
    (toString [] (str "constructor proxed: " x y))))

; alright, let's do a more complex example where
; we extend something more complicated than Object
(defn arraylist-proxy
  []
  (proxy [java.util.ArrayList] []
    (toString [] "arr")))

; now let's implement an interface
; this example shows that Clojure doesn't seem to check if you don't actually implement the interface properly
(defn comparable-proxy
  []
  (proxy [Comparable] []
    (toString [] "oops, didn't implement the 'compareTo' method")))

; now let's try to faithfully implement the Comparable interface
(defn fake-comp
  []
  (proxy [Comparable] []
    (compareTo [other] false))) ; oops -- fails at runtime b/c 'compareTo' must return an integer!

; now let's implement Comparable for real
; (although note that the implementation is kind of dumb)
; > (.compareTo (real-comp) 3)
(defn real-comp
  []
  (proxy [Comparable] []
    (compareTo [other] 1)))

; known 'issues' with proxy:
;   overloaded methods:  clojure just thinks there's one 

; create a proxied object
;   that uses other proxied objects,
;   or takes them as parameters
; TODO
(defn real-comp
  []
  (proxy [Comparable] []
    (compareTo [other] 1)))

; Nesting proxies. 
(defn comp-hash-map
  []
  (new java.util.TreeSet 
    (proxy [java.util.Comparator] [] 
       (compare [a b] (- (.hashCode a) (.hashCode b))))))
