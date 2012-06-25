(ns rest.client
  (:require [clj-http.client :as client]
            [rest.io :refer [wrap-accept wrap-input-coercion wrap-output-coercion]]))

(def ^:dynamic *client*
  (-> client/request
      wrap-accept
      wrap-input-coercion
      wrap-output-coercion))

(defn send-request
  "Send the HTTP request via *client*."
  [url & [request]]
  (let [response (*client* (merge {:method :get :url url} request))]
    (if (instance? clojure.lang.IMeta (:body response))
      (with-meta (:body response)
        (dissoc response :body))
      response)))

(defmacro with-client
  "Evaluate `body` with *client* bound to `client`."
  [client & body]
  `(binding [*client* ~client]
     ~@body))

(comment (map :name (send-request "http://api.burningswell.dev/spots")))
