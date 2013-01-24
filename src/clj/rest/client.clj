(ns rest.client
  (:require [clj-http.client :as client]
            [clj-http.core :refer [request]]
            [clojure.string :refer [blank?]]
            [rest.stacktrace :refer [wrap-stacktrace-client]]
            [rest.io :refer [wrap-accept wrap-input-coercion wrap-output-coercion]]
            [routes.helper :refer [parse-url]]))

(defprotocol IRequest
  (to-request [obj] "Make a Ring request map from `obj`."))

(defn wrap-remote-addr
  "Assoc :remote-addr to the request."
  [client]
  (fn [request]
    (client
     (if (blank? (:remote-addr request))
       (assoc request :remote-addr "127.0.0.1")
       request))))

(def ^:dynamic *client*
  (-> request
      client/wrap-query-params
      client/wrap-basic-auth
      client/wrap-oauth
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
      wrap-stacktrace-client))

(defn test-client [handler]
  (-> handler
      client/wrap-query-params
      client/wrap-basic-auth
      client/wrap-oauth
      client/wrap-user-info
      client/wrap-url
      client/wrap-redirects
      client/wrap-decompression
      ;; client/wrap-input-coercion
      ;; client/wrap-output-coercion
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
      wrap-remote-addr
      wrap-stacktrace-client))

(defn- parse-map [{:keys [server-name uri] :as m}]
  (cond
   (and server-name uri) m
   (not (blank? uri))
   (assoc (parse-url uri)
     :body (if-not (empty? (dissoc m :uri))
             (dissoc m :uri))
     :request-method :get)
   :else (throw (Exception. (str "Can't create Ring request map from: " (prn-str m))))))

(defn- parse-string [s]
  (assoc (parse-url s)
    :body nil
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
