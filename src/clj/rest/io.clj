(ns rest.io
  (:refer-clojure :exclude (replace))
  (:use [clojure.string :only (blank? replace)]
        [rest.json :only (json-str read-json)]))

(def ^:dynamic *content-type* :application/json)

(defn content-type
  "Returns the value of the Content-Type header of `request`."
  [request]
  (let [content-type (get (:headers request) "content-type")]
    (if-not (blank?  content-type)
      (replace content-type #";.*" ""))))

(defmulti deserialize
  "Deserialize the body of `response` according to the Content-Type
header or *content-type*."
  (fn [response] (or (content-type response) *content-type*)))

(defmethod deserialize :application/clojure
  [{:keys [body] :as response}]
  (if (string? body)
    (update-in response [:body] read-string)
    response))

(defmethod deserialize :application/json
  [{:keys [body] :as response}]
  (if (string? body)
    (update-in response [:body] read-json)
    response))

(defmulti serialize
  "Serialize the body of `response` according to the Content-Type
header or *content-type*."
  (fn [request]
    (or (content-type request) *content-type*)))

(defmethod serialize :application/clojure
  [{:keys [body] :as request}]
  (if body
    (-> (update-in request [:body] prn-str)
        (assoc-in [:headers "content-type"] "application/clojure"))
    request))

(defmethod serialize :application/json
  [{:keys [body] :as request}]
  (if body
    (-> (update-in request [:body] json-str)
        (assoc-in [:headers "content-type"] "application/json"))
    request))

(defn wrap-input-coercion [handler]
  (fn [request] (handler (serialize request))))

(defn wrap-output-coercion [handler]
  (fn [request] (handler (deserialize request))))
