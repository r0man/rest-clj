(ns rest.test
  (:require [rest.test.core :as core]
            [rest.test.client :as client]
            [rest.test.http :as http]
            [rest.test.json :as json]
            [rest.test.macros :as macros]))

(defn ^:export run []
  (core/test)
  (client/test)
  (http/test)
  (json/test)
  (macros/test)
  "All tests passed.")