(ns dojo.agent 
  (:import (java.io BufferedWriter FileWriter)))

(let [wtr (agent (BufferedWriter. (FileWriter. "agent.log")))]
  (defn log 
    [msg]
    (let [write (fn [out msg]
                    (.write out msg)
                    out)]
     (send wtr write msg)))
  (defn close 
    []
    (send wtr #(.close %))))

(log "test ccolber\n")
(log "another line\n")
(log "test ccolber\n")
(log "another line\n")
(log "test ccolber\n")
(log "another line\n")
(log "test ccolber\n")
(log "another line\n")
(log "test ccolber\n")
(log "another line\n")
(log "test ccolber\n")
(log "another line\n")
(log "test ccolber\n")
(log "another line\n")
(log "test ccolber\n")
(log "another line\n")
(log "test ccolber\n")
(log "another line\n")
(log "test ccolber\n")
(log "another line\n")
(log "test ccolber\n")
(log "another line\n")
(log "test ccolber\n")
(log "another line\n")
(log "test ccolber\n")
(log "another line\n")

(close)