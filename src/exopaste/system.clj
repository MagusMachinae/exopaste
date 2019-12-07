(ns exopaste.system
  (:require [com.stuartsierra.component :as component]
            [exopaste.store :as store]
            [exopaste.server :as server]
            [clojure.tools.logging :refer [error]]))

(def ^:redef system
  "Holds system"
  nil)

(defn build-system
  "defines sys-map"
  []
  (try
    (-> (component/system-map
         :server (server/make-server)
         :store (store/make-store))
        (component/system-using {:server [:store]}))
    (catch Exception e
      (error "Failed to build system" e))))

(defn init-system
  []
  (let [sys (build-system)]
    (alter-var-root #'system (constantly sys))))

(defn stop!
  "stop system"
  []
  (alter-var-root #'system component/stop-system))

(defn start!
  "start system"
  []
  (alter-var-root #'system component/start-system)
  (println "system started"))
