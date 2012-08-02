(ns rest.client
  (:require [clj-http.client :as client]
            [clj-http.core :refer [request]]
            [clojure.string :refer [blank?]]
            [rest.stacktrace :refer [wrap-stacktrace-client]]
            [rest.io :refer [wrap-accept wrap-response-as-meta]]
            [rest.io :refer [wrap-input-coercion wrap-output-coercion]]))

(defprotocol IRequest
  (to-request [obj] "Make a Ring request map from `obj`."))

(def ^:dynamic *client*
  (-> request
      client/wrap-query-params
      client/wrap-basic-auth
      ;; client/wrap-oauth
      client/wrap-user-info
      client/wrap-url
      client/wrap-redirects
      client/wrap-decompression
      client/wrap-input-coercion
      client/wrap-output-coercion
      ;; client/wrap-exceptions
      client/wrap-accept
      client/wrap-accept-encoding
      client/wrap-content-type
      client/wrap-form-params
      client/wrap-nested-params
      client/wrap-method
      ;; client/wrap-cookies
      ;; client/wrap-links
      client/wrap-unknown-host
      wrap-accept
      wrap-input-coercion
      wrap-output-coercion
      wrap-stacktrace-client
      wrap-response-as-meta))

(defn- parse-map [{:keys [server-name uri] :as m}]
  (cond
   (and server-name uri) m
   (not (blank? uri))
   (assoc (client/parse-url uri)
     :body (dissoc m :uri)
     :request-method :get)
   :else (throw (Exception. (str "Can't create Ring request map from: " (prn-str m))))))

(defn- parse-string [s]
  (assoc (client/parse-url s)
    :body {}
    :request-method :get))

(defn send-request [method request & [options]]
  (-> (to-request request)
      (merge options)
      (assoc :request-method method)
      (*client*)))

(defmacro with-client
  "Evaluate `body` with *client* bound to `client`."
  [client & body]
  `(binding [*client* ~client]
     ~@body))

(extend-protocol IRequest

  clojure.lang.IPersistentMap
  (to-request [m] (parse-map m))

  java.lang.String
  (to-request [s] (parse-string s))

  java.net.URL
  (to-request [u] (parse-string (str u)))

  java.net.URI
  (to-request [u] (parse-string (str u))))
