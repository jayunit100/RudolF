(ns ConnjurRV.utils)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; get-e :: Map key value -> key -> Either Exception value
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn get-e
  "check for key in map, and throw an exception if not found or nil"
  [mapp keyy]
  (if (contains? mapp keyy)
   (mapp keyy)
   (throw (new Exception 
               (str "can't find key " keyy " in map")))))