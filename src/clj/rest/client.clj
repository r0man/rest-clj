(ns rest.client
  (:require [clj-http.client :as client]
            [rest.io :refer [wrap-accept wrap-input-coercion wrap-output-coercion]]))

(defprotocol IRequest
  (to-request [obj] "Make a Ring request map from `obj`."))

(def ^:dynamic *client*
  (-> client/request
      wrap-accept
      wrap-input-coercion
      wrap-output-coercion))

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

(extend-type String
  IRequest
  (to-request [s]
    (client/parse-url s)))

(extend-protocol IRequest
  clojure.lang.IPersistentMap
  (to-request [m]
    (let [url (:uri m)]
      (assert url (str "Can't build request without an uri: " (prn-str m)))
      (-> (client/parse-url (str url))
          (assoc :body (dissoc m :uri))))))
