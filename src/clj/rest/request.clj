(ns rest.request
  (:require [clj-http.client :as client]))

(defn send-request
  "Send the HTTP request."
  [request] (client/request request))
