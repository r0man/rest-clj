(ns rest.http
  (:refer-clojure :exclude [get])
  (:require [rest.client :refer [send-request to-request]]))

(defn delete
  "Send an HTTP delete request."
  [url & {:as options}]
  (send-request :delete url options))

(defn get
  "Send an HTTP get request."
  [url & {:as options}]
  (send-request :get url options))

(defn head
  "Send an HTTP head request."
  [url & {:as options}]
  (send-request :head url options))

(defn move
  "Send an HTTP move request."
  [url & {:as options}]
  (send-request :move url options))

(defn options
  "Send an HTTP options request."
  [url & {:as options}]
  (send-request :options url options))

(defn patch
  "Send an HTTP patch request."
  [url & {:as options}]
  (send-request :patch url options))

(defn post
  "Send an HTTP post request."
  [url & {:as options}]
  (send-request :post url options))

(defn put
  "Send an HTTP put request."
  [url & {:as options}]
  (send-request :put url options))
