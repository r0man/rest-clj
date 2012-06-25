(ns rest.client
  (:require [cljs-http.client :refer [parse-url request]]
            [rest.io :refer [wrap-accept wrap-input-coercion wrap-output-coercion]]))

(def ^:dynamic *client*
  (-> request
      wrap-accept
      wrap-input-coercion
      wrap-output-coercion))

(defn send-request
  "Send the HTTP request via *client*."
  [url & [method request]]
  (rest.client/*client* (merge {:method (or method :get) :url url} request)))

(comment (send-request "http://api.burningswell.dev/continents"))
