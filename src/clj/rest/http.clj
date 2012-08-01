(ns rest.http
  (:refer-clojure :exclude [get])
  (:require [rest.client :refer [*client* to-request]]))

(defn delete
  "Send an HTTP delete request."
  [url & {:as options}]
  (*client* (merge (to-request url) (assoc options :request-method :delete))))

(defn get
  "Send an HTTP get request."
  [url & {:as options}]
  (*client* (merge (to-request url) (assoc options :request-method :get))))

(defn head
  "Send an HTTP head request."
  [url & {:as options}]
  (*client* (merge (to-request url) (assoc options :request-method :head))))

(defn move
  "Send an HTTP move request."
  [url & {:as options}]
  (*client* (merge (to-request url) (assoc options :request-method :move))))

(defn options
  "Send an HTTP options request."
  [url & {:as options}]
  (*client* (merge (to-request url) (assoc options :request-method :options))))

(defn patch
  "Send an HTTP patch request."
  [url & {:as options}]
  (*client* (merge (to-request url) (assoc options :request-method :patch))))

(defn post
  "Send an HTTP post request."
  [url & {:as options}]
  (*client* (merge (to-request url) (assoc options :request-method :post))))

(defn put
  "Send an HTTP put request."
  [url & {:as options}]
  (*client* (merge (to-request url) (assoc options :request-method :put))))
