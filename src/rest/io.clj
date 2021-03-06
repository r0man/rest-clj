(ns rest.io
  (:refer-clojure :exclude [replace])
  (:require [clojure.string :refer [blank? replace]]
            [rest.edn :as edn]
            [rest.json :refer [json-str read-json]]))

(def ^:dynamic *content-type* "application/edn")

(defn body [response]
  (cond
   (or (map? (:body response))
       (sequential? (:body response)))
   (with-meta (:body response)
     (dissoc response :body))))

(defn content-type
  "Returns the value of the Content-Type header of `request`."
  [request]
  (let [content-type (get (:headers request) "content-type")]
    (if-not (blank?  content-type)
      (keyword (replace content-type #";.*" "")))))

(defmulti deserialize
  "Deserialize the body of `response` according to the Content-Type header."
  (fn [response]
    (content-type response)))

(defmethod deserialize :default
  [response] response)

(defmethod deserialize :application/edn
  [{:keys [body] :as response}]
  (if (string? body)
    (update-in response [:body] edn/read-string)
    response))

(defmethod deserialize :application/json
  [{:keys [body] :as response}]
  (if (string? body)
    (update-in response [:body] read-json)
    response))

(defmulti serialize
  "Serialize the body of `response` according to the Content-Type header."
  (fn [request]
    (keyword (or (content-type request) *content-type*))))

(defmethod serialize :application/edn
  [{:keys [body] :as request}]
  (if body
    (-> (update-in request [:body] prn-str)
        (assoc :content-type "application/edn")
        (assoc-in [:headers "content-type"] "application/edn"))
    request))

(defmethod serialize :default
  [{:keys [body] :as request}]
  (if body
    (-> (update-in request [:body] json-str)
        (assoc :content-type "application/json")
        (assoc-in [:headers "content-type"] "application/json"))
    request))

(defn wrap-accept [client & [content-type]]
  (let [content-type (or content-type *content-type*)]
    #(client (assoc-in %1 [:headers "accept"] content-type))))

(defn wrap-debug [client]
  (fn [request]
    (prn ">>> REQUEST >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
    (prn request)
    (println)
    (let [response (client request)]
      (prn ">>> RESPONSE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
      (prn response)
      (println)
      response)))

(defn wrap-input-coercion [client]
  #(client (serialize %1)))

(defn wrap-output-coercion [client]
  #(deserialize (client %1)))
