(ns rest.http
  (:refer-clojure :exclude [get])
  (:require [rest.client :refer [send-request]]))

(defn delete
  "Send an HTTP delete request."
  [request & [options]]
  (send-request :delete request options))

(defn get
  "Send an HTTP get request."
  [request & [options]]
  (send-request :get request options))

(defn head
  "Send an HTTP head request."
  [request & [options]]
  (send-request :head request options))

(defn move
  "Send an HTTP move request."
  [request & [options]]
  (send-request :move request options))

(defn options
  "Send an HTTP options request."
  [request & [options]]
  (send-request :options request options))

(defn patch
  "Send an HTTP patch request."
  [request & [options]]
  (send-request :patch request options))

(defn post
  "Send an HTTP post request."
  [request & [options]]
  (send-request :post request options))

(defn put
  "Send an HTTP put request."
  [request & [options]]
  (send-request :put request options))
