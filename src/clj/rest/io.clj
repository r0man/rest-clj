(ns rest.io
  (:refer-clojure :exclude [replace])
  (:require ;*CLJSBUILD-REMOVE*;[rest.clojurescript :refer [read-string]]
   [clojure.string :refer [blank? replace]]
   [rest.json :refer [json-str read-json]]))

(def ^:dynamic *accept* "application/json")

(defn content-type
  "Returns the value of the Content-Type header of `request`."
  [request]
  (let [content-type (get (:headers request) "content-type")]
    (if-not (blank?  content-type)
      (keyword (replace content-type #";.*" "")))))

(defmulti deserialize
  "Deserialize the body of `response` according to the Content-Type header."
  (fn [response] (content-type response)))

(defmethod deserialize :default
  [response] response)

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
  "Serialize the body of `response` according to the Content-Type header."
  (fn [request] (or (content-type request) *accept*)))

(defmethod serialize :default
  [request] request)

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

(defn wrap-accept [handler]
  (fn [request]
    (handler (assoc-in request [:headers "Accept"] *accept*))))

(defn wrap-input-coercion [handler]
  (fn [request] (handler (serialize request))))

(defn wrap-output-coercion [handler]
  (fn [request]
    (deserialize (handler request))))
