(ns rest.client
  (:refer-clojure :exclude (get))
  (:require [clj-http.client :as client])
  (:use [clj-http.client :only (parse-url)]))

(def ^:dynamic *client* client/request)

(defprotocol IRequest
  (request [arg] "Convert `arg` to Ring request map."))

(defn make-request
  "Send the HTTP request via *client*."
  [request-method resource & {:as options}]
  (-> (merge (request resource) options)
      (assoc :request-method request-method)))

(defn send-request
  "Send the HTTP request via *client*."
  [request] (*client* request))

(defn delete
  "Get the represention of `resource`."
  [resource & options]
  (send-request (apply make-request :delete resource options)))

(defn get
  "Get the represention of `resource`."
  [resource & options]
  (send-request (apply make-request :get resource options)))

(defn head
  "Check if the `resource` exists."
  [resource & options]
  (send-request (apply make-request :head resource options)))

(defn options
  "Get the options of `resource`."
  [resource & options]
  (send-request (apply make-request :options resource options)))

(defn post
  "Create a new `resource`."
  [resource & options]
  (send-request (apply make-request :post resource options)))

(defn put
  "Change the representation of `resource`."
  [resource & options]
  (send-request (apply make-request :put resource options)))

(defn trace
  "Trace the `resource`."
  [resource & options]
  (send-request (apply make-request :trace resource options)))

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
     :else (throw (Exception. (str "Can't create Ring request map from: " resource))))))
