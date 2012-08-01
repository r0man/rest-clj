(ns rest.http
  (:refer-clojure :exclude [get])
  (:require [rest.client :refer [send-request]]))

(defn delete
  "Send an HTTP delete request."
  [url & [options]]
  (send-request :delete url options))

(defn get
  "Send an HTTP get request."
  [url & [options]]
  (send-request :get url options))

(defn head
  "Send an HTTP head request."
  [url & [options]]
  (send-request :head url options))

(defn move
  "Send an HTTP move request."
  [url & [options]]
  (send-request :move url options))

(defn options
  "Send an HTTP options request."
  [url & [options]]
  (send-request :options url options))

(defn patch
  "Send an HTTP patch request."
  [url & [options]]
  (send-request :patch url options))

(defn post
  "Send an HTTP post request."
  [url & [options]]
  (send-request :post url options))

(defn put
  "Send an HTTP put request."
  [url & [options]]
  (send-request :put url options))
