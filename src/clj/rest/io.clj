(ns rest.io
  (:use [clojure.data.json :only (read-json)]))

(def ^:dynamic *content-type* :application/json)

(defn content-type
  "Returns the value of the Content-Type header of `r`."
  [r] (get (:headers r) "Content-Type"))

(defmulti deserialize-content-type
  "Deserialize the body of `response` according to the Content-Type
header or *content-type*."
  (fn [response] (or (content-type response) *content-type*)))

(defmethod deserialize-content-type :application/clojure
  [{:keys [body] :as response}]
  (if (string? body)
    (update-in response [:body] read-string)
    response))

(defmethod deserialize-content-type :application/json
  [{:keys [body] :as response}]
  (if (string? body)
    (update-in response [:body] read-json)
    response))

(defmethod deserialize-content-type :default [response]
  response)

(defmulti serialize-content-type
  "Serialize the body of `response` according to the Content-Type
header or *content-type*."
  (fn [request]
    (or (content-type request) *content-type*)))

(defmethod serialize-content-type :default
  [{:keys [body] :as request}]
  (if body
    (update-in request [:body] str)
    request))
