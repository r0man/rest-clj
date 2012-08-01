(ns rest.client
  (:require [cljs-http.client :as client]
            [rest.io :refer [wrap-accept wrap-input-coercion wrap-output-coercion]]))

(defprotocol IRequest
  (to-request [obj] "Make a Ring request map from `obj`."))

(def ^:dynamic *client*
  (-> client/request
      wrap-accept
      wrap-input-coercion
      wrap-output-coercion))

(defn send-request
  "Send the HTTP request via *client*."
  [url & [method request]]
  (*client* (merge {:request-method (or method :get) :uri url} request)))

(extend-type string
  IRequest
  (to-request [s]
    (client/parse-url s)))
