(ns rest.test
  (:require [rest.test.client :as client]
            [rest.test.core :as core]
            [rest.test.http :as http]
            [rest.test.json :as json]))

(defn ^:export run []
  (client/test)
  (core/test)
  (http/test)
  (json/test)
  "All tests passed.")