(ns rest.io
  (:refer-clojure :exclude [replace])
  (:require ;*CLJSBUILD-REMOVE*;[rest.clojurescript :refer [read-string]]
   [clojure.string :refer [blank? replace]]
   [rest.json :refer [json-str read-json]]))

(def ^:dynamic *content-type* "application/json")

(defn content-type
  "Returns the value of the Content-Type header of `request`."
  [request]
  (let [content-type (get (:headers request) "content-type")]
    (if-not (blank?  content-type)
      (keyword (replace content-type #";.*" "")))))

(defmulti deserialize
  "Deserialize the body of `response` according to the Content-Type header."
  (fn [response]
    ;; (prn "DESERIALIZE")
    ;; (prn response)
    ;; (println)
    (content-type response)))

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
  (fn [request]
    ;; (prn "SERIALIZE")
    ;; (prn request)
    ;; (println)
    (keyword (or (content-type request) *content-type*))))

(defmethod serialize :application/clojure
  [{:keys [body] :as request}]
  (if body
    (-> (update-in request [:body] prn-str)
        (assoc-in [:headers "content-type"] "application/clojure"))
    request))

(defmethod serialize :default
  [{:keys [body] :as request}]
  (if body
    (-> (update-in request [:body] json-str)
        (assoc-in [:headers "content-type"] "application/json"))
    request))

(defn wrap-accept [handler & [content-type]]
  (let [content-type (or content-type *content-type*)]
    #(handler (assoc-in %1 [:headers "Accept"] content-type))))

(defn wrap-input-coercion [handler]
  #(handler (serialize %1)))

(defn wrap-output-coercion [handler]
  #(deserialize (handler %1)))

(defn wrap-response-as-meta [handler]
  #(let [response (handler %1)]
     (if (instance? clojure.lang.IMeta (:body response))
       (with-meta (:body response)
         (dissoc response :body))
       (:body response))))

(defn wrap-response-as-vector [handler]
  #(let [response (handler %1)]
     [(:body response) (dissoc response :body)]))
