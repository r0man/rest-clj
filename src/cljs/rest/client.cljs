(ns rest.client
  (:require [cljs-http.client :as client]
            [clojure.string :refer [blank?]]
            [goog.Uri :as uri]
            [rest.io :refer [wrap-accept wrap-input-coercion wrap-output-coercion]]
            [routes.helper :refer [parse-url]]))

(defprotocol IRequest
  (to-request [obj] "Make a Ring request map from `obj`."))

(def ^:dynamic *client*
  (-> client/request
      wrap-accept
      wrap-input-coercion
      wrap-output-coercion))

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
