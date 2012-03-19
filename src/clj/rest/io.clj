(ns rest.io
  (:use [clojure.data.json :only (json-str read-json)]))

(def ^:dynamic *content-type* :application/json)

(defn content-type
  "Returns the value of the Content-Type header of `r`."
  [r] (get (:headers r) "content-type"))

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

(defmulti serialize-content-type
  "Serialize the body of `response` according to the Content-Type
header or *content-type*."
  (fn [request]
    (or (content-type request) *content-type*)))

(defmethod serialize-content-type :application/clojure
  [{:keys [body] :as request}]
  (if body
    (-> (update-in request [:body] prn-str)
        (assoc-in [:headers "content-type"] "application/clojure"))
    request))

(defmethod serialize-content-type :application/json
  [{:keys [body] :as request}]
  (if body
    (-> (update-in request [:body] json-str)
        (assoc-in [:headers "content-type"] "application/json"))
    request))

(defmacro with-content-type
  "Evaluate `body` with *content-type* bound to `content-type`."
  [content-type & body]
  `(binding [*content-type* ~content-type]
     ~@body))

(defn wrap-content-type
  [handler]
  (fn [request]
    (-> request
        serialize-content-type
        handler
        deserialize-content-type)))
