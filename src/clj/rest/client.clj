(ns rest.client
  (:refer-clojure :exclude (get))
  (:require [clj-http.client :as client])
  (:use [clj-http.client :only (parse-url)]))

(def ^:dynamic *client* client/request)

(defprotocol IRequest
  (request [arg] "Convert `arg` to Ring request map."))

(defn build-request
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
  (send-request (apply build-request :delete resource options)))

(defn get
  "Get the represention of `resource`."
  [resource & options]
  (send-request (apply build-request :get resource options)))

(defn head
  "Check if the `resource` exists."
  [resource & options]
  (send-request (apply build-request :head resource options)))

(defn options
  "Get the options of `resource`."
  [resource & options]
  (send-request (apply build-request :options resource options)))

(defn post
  "Create a new `resource`."
  [resource & options]
  (send-request (apply build-request :post resource options)))

(defn put
  "Change the representation of `resource`."
  [resource & options]
  (send-request (apply build-request :put resource options)))

(defn trace
  "Trace the `resource`."
  [resource & options]
  (send-request (apply build-request :trace resource options)))

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
  (request [{:keys [uri url] :as resource}]
    (cond
     (string? uri)
     resource
     (and (string? url)
          (re-matches #"https?://.*" url))
     (assoc (parse-url url) :body resource))))
