(ns rest.client
  (:import [java.io ByteArrayOutputStream InputStream]
           org.apache.http.entity.StringEntity)
  (:require [clj-http.client :as client]
            [clj-http.core :refer [request]]
            [clojure.string :refer [blank? lower-case]]
            [inflections.transform :refer [transform-keys]]
            [rest.stacktrace :refer [wrap-stacktrace-client]]
            [rest.io :refer [body wrap-accept wrap-debug wrap-input-coercion wrap-output-coercion]]
            [routes.helper :refer [parse-url]]))

(defprotocol IRequest
  (to-request [obj] "Make a Ring request map from `obj`."))

(defn to-bytes
  [in]
  (cond
   (nil? in)
   in
   (string? in)
   (.getBytes in)
   (instance? InputStream in)
   (let [buf (byte-array 4096)
         out (ByteArrayOutputStream.)]
     (loop []
       (let [r (.read in buf)]
         (when (not= r -1)
           (.write out buf 0 r)
           (recur))))
     (.toByteArray out))))

(defn to-input-stream
  [in]
  (cond
   (nil? in)
   in
   (instance? StringEntity in)
   (.getContent in)))

(defn wrap-body-meta
  "Returns a client that returns the :body of the response, with the
  rest of the request added as meta data when possible."
  [client] (fn [request] (body (client request))))

(defn wrap-ring
  "Return the Ring response map into a clj-http compatible format."
  [client]
  (fn [request]
    (-> (client
         (-> request
             (update-in [:body] to-input-stream)))
        (update-in [:body] to-bytes)
        (update-in [:headers] #(transform-keys %1 lower-case)))))

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
      wrap-stacktrace-client
      wrap-body-meta))

(defn test-client [handler]
  (-> handler
      wrap-ring
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
      wrap-remote-addr
      wrap-stacktrace-client
      wrap-body-meta))

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
