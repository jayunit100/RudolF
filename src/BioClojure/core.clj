(ns SeqMine2.core 
	
	(:import (org.apache.commons.lang StringUtils)  )
)

;Capitalize Function
(defn makeCap "This makes capitals" [inString]
	(StringUtils/capitalize inString))

; input s1, b1 b2 b3 
(defn hasSubstring "has" [ main b ]
	(map #(.indexOf main %) b     ))

;1 Identify motifs (M1 - Mx) present in DNA and their order



;Confirm initiator motif and terminator motif and positions
;Confirm proper insertion into two restriction sites.
;Flag any incorrect assemblies and sequences with N positions
;Report total number of minimotifs in clone
;Generate a graph of overall structure, show linker regions that glue motifs together and alignm
;of motifs to sequences with labels.
;7. Report any 1 nucleotide mutations in DNA

