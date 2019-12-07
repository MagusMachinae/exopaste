(ns exopaste.server
  (:require [com.stuartsierra.component :as component]
            [bidi.ring :as ring]
            [aleph.http :as http]
            [ring.util.response :as res]
            [ring.util.request :as req]
            [ring.middleware.params :as param]
            [exopaste.view :as view]
            [exopaste.store :as store]))

(defn handle-post
  "Handler for creating paste based on POST-ed data."
  [store request]
  (let [content (get (:form-params request) "content")
        uuid (store/add-new-paste store content)]
    (res/redirect (str "/" uuid) :see-other)))

(defn handle-index
  "Index page with prompt for new paste"
  [request]
  (res/response (view/render-form)))

(defn index-handler
  "Handler for requests sent to root URL
Can be either a GET for the form view, or POST requests for submitted pastes."
  [store request]
  (if (= (:request-method request) :post)
    (handle-post store request)
    (handle-index request)))

(defn paste-handler
  [store request]
  (let [paste (store/get-paste-by-uuid store (:uuid (:route-params request)))]
    (res/response (view/render-paste paste))))

(defn handler
  "Get handler function for routes."
  [store]
  (ring/make-handler ["/" {"" (partial index-handler store)
                           [:uuid] (partial paste-handler store)}]))

(defn app
  [store]
  (-> (handler store)
      param/wrap-params))

(defrecord HttpServer [server]

  component/Lifecycle

  (start [this]
    (assoc this :server (http/start-server (app (:store this)) {:port 8000})))

  (stop [this]
    (dissoc this :server)))

(defn make-server
  []
  (map->HttpServer {}))
