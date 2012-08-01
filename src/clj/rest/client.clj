(ns rest.client
  (:require [clj-http.client :as client]
            [clojure.string :refer [blank?]]
            [rest.io :refer [wrap-accept wrap-input-coercion wrap-output-coercion]]))

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
   (assoc (client/parse-url uri)
     :body (dissoc m :uri)
     :request-method :get)
   :else (throw (Exception. (str "Can't create Ring request map from: " (prn-str m))))))

(defn- parse-string [s]
  (assoc (client/parse-url s) :request-method :get))

(defn send-request
  "Send the HTTP request via *client*."
  [url & [request]]
  (let [response (*client* (merge {:request-method :get :uri url} request))]
    (if (instance? clojure.lang.IMeta (:body response))
      (with-meta (:body response)
        (dissoc response :body))
      response)))

(defmacro with-client
  "Evaluate `body` with *client* bound to `client`."
  [client & body]
  `(binding [*client* ~client]
     ~@body))

(extend-protocol IRequest

  clojure.lang.IPersistentMap
  (to-request [m] (parse-map m))

  java.lang.String
  (to-request [s] (parse-string s)))
