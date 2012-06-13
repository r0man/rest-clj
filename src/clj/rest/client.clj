(ns rest.client
  (:refer-clojure :exclude (get replace))
  (:require [clj-http.client :as client])
  (:use [clj-http.client :only (parse-url)]
        [clojure.string :only (replace)]
        [rest.io :only (wrap-content-type)]))

(def ^:dynamic *client*
  (wrap-content-type client/request))

(defprotocol IRequest
  (request [arg] "Convert `arg` to Ring request map."))

(defn make-request
  "Make a Ring request map."
  [method resource & [{:as options}]]
  (-> (merge (request resource) options)
      (assoc :method method)
      (assoc :request-method method)))

(defn send-request
  "Send the HTTP request via *client*."
  [request] (*client* request))

(defn delete
  "Get the represention of `resource`."
  [resource & [options]]
  (send-request (make-request :delete resource options)))

(defn get
  "Get the represention of `resource`."
  [resource & [options]]
  (send-request (make-request :get resource options)))

(defn head
  "Check if the `resource` exists."
  [resource & [options]]
  (send-request (make-request :head resource options)))

(defn options
  "Get the options of `resource`."
  [resource & [options]]
  (send-request (make-request :options resource options)))

(defn post
  "Create a new `resource`."
  [resource & [options]]
  (send-request (make-request :post resource options)))

(defn put
  "Change the representation of `resource`."
  [resource & [options]]
  (send-request (make-request :put resource options)))

(defn trace
  "Trace the `resource`."
  [resource & [options]]
  (send-request (make-request :trace resource options)))

(defmacro with-client
  "Evaluate `body` with *client* bound to `client`."
  [client & body]
  `(binding [*client* ~client]
     ~@body))

(extend-type String
  IRequest
  (request [string]
    (parse-url string)))

(extend-type clojure.lang.IPersistentMap
  IRequest
  (request [{:keys [server-name uri url] :as resource}]
    (cond
     (and (string? server-name) (string? uri))
     resource
     (string? url)
     (assoc (parse-url url) :body resource)
     :else (throw (Exception. (str "Can't create Ring request from: " resource))))))


;; :body-encoding is optional and defaults to "UTF-8"
;; (client/post
;;  "http://api.burningswell.dev/continents"
;;  {:form-params {:name "Europe"} :content-type :json})

(defn interpolate [[pattern & keys] record]
  (reduce
   (fn [& [pattern key]]
     (let [value (str (clojure.core/get record key))]
       (replace pattern (str key) value)))
   pattern keys))

(interpolate ["/continents/:id-:name" :id :name] {:id 1 :name "europe"})

(defn create-continent [continent & {:as options}]
  (client/post
   (or (:url continent) "/continents")
   {:content-type :json
    :form-params continent
    :query-params (:query-params options)}))

(defn delete-continent [continent]
  (client/delete
   (or (:url continent) (interpolate ["/continents/:id-:name" :id :name] continent))
   {:content-type :json
    :form-params continent
    :query-params (:query-params options)}))

(defn continent [continent & {:as options}]
  (client/get
   (or (:url continent) (interpolate ["/continents/:id-:name" :id :name] continent))
   {:as :json-string-keys
    :query-params (:query-params options)}))

(defn continents [continent & {:as options}]
  (client/get
   (or (:url continent) "/continents")
   {:as :json-string-keys
    :query-params (:query-params options)}))
