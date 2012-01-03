(ns dojo.readcyana
  (:use [clojure.contrib.str-utils2 :only (split-lines trim)])
  (:use clojure.contrib.generic.functor))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; module interface
;	parse-shifts
;	parse-sequence
;	merge-shifts
;	make-protein
;	make-protein-from-files
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; test:  input is contents of cyana shift file
;	output is list of length equal to number of lines in file
;	and each element is a 5-key map
;	value of :id should be unique across the list
;	:resid is positive
;	:atom is an atomname
;	:shift and :error are numeric
(defn parse-shifts
  "type :: String -> [Cyanaline]
   where Cyanaline: map with keys /id, shift, error, atom, resid/"
  [text]
  {:pre [(string? text)]}
  (let [headers [:id :shift :error :atom :resid]]
   (map #(zipmap headers (.split (trim %) " +")) ; on whitespace??
         (split-lines text))))

