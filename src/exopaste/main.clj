(ns exopaste.main
  (:require [exopaste.system :as system]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (system/init-system)
  (system/start!))
