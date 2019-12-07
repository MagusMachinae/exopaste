(ns exopaste.view
  (:require [hiccup.page :as page]
            [hiccup.form :as form]))

(defn render-paste
  "Given a map representing a paste, return HTML string to display paste"
  [paste]
  (page/html5 [:head
               (page/include-js "https://sos-de-fra-1.exo.io/highlight.js/9.12.0/highlight.min.js")
               (page/include-js "https://sos-de-fra-1.exo.io/highlight.js/9.12.0/go.min.js")
               (page/include-js "https://sos-de-fra-1.exo.io/highlight.js/9.12.0/clojure.min.js")
               (page/include-js "https://sos-de-fra-1.exo.io/highlight.js/9.12.0/yaml.min.js")
               (page/include-css "https://sos-de-fra-1.exo.io/highlight.js/9.12.0/default.min.css")
               [:meta {:charset "UTF-8"}]
               [:script "hljs.initHighlightingOnLoad();"]]
              [:body
               [:pre [:code (:content paste)]]]))

(defn render-form
  "Render minimal HTML form page"
  []
  (page/html5 [:head
               [:meta {:charset "UTF-8"}]]
              [:body
               (form/form-to [:post "/"]
                             (form/text-area {:cols 80
                                              :rows 10} "content")
                             [:div]
                             (form/submit-button "Post Paste"))]))
