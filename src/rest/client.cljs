(ns rest.client
  (:require [cljs-http.client :as client]
            [clojure.string :refer [blank?]]
            [goog.Uri :as uri]
            [goog.labs.async.SimpleResult :as SimpleResult]
            [rest.io :refer [body wrap-accept]]
            [routes.helper :refer [parse-url]]))

(meta (with-meta [] {}))

(defprotocol IRequest
  (to-request [obj] "Make a Ring request map from `obj`."))

(defn wrap-body-meta
  "Returns a client that returns the :body of the response, with the
  rest of the request added as meta data when possible."
  [client]
  (fn [request]
    (let [result (goog.labs.async.SimpleResult.)]
      (doto (client request)
        (client/on-success #(.setValue result (body %1)))
        (client/on-error #(.setError result (body %1))))
      result)))

(def ^:dynamic *client*
  (-> client/request
      (client/wrap-credentials true)
      (wrap-accept)
      (wrap-body-meta)))

(defn- parse-map [{:keys [server-name uri] :as m}]
  (cond
   (and server-name uri) m
   (not (blank? uri))
   (assoc (parse-url uri)
     :body (dissoc m :uri)
     :request-method :get)
   :else (throw (js/Error (str "Can't create Ring request map from: " (prn-str m))))))

(defn- parse-string [s]
  (assoc (parse-url s)
    :body {}
    :request-method :get))

(defn send-request [method request & [options]]
  (-> (to-request request)
      (merge options)
      (assoc :request-method method)
      (*client*)))

(extend-protocol IRequest

  cljs.core.HashMap
  (to-request [m] (parse-map m))

  cljs.core.ObjMap
  (to-request [m] (parse-map m))

  cljs.core.PersistentArrayMap
  (to-request [m] (parse-map m))

  cljs.core.PersistentHashMap
  (to-request [m] (parse-map m))

  goog.Uri
  (to-request [uri] (parse-string (str uri)))

  string
  (to-request [s] (parse-string s)))
