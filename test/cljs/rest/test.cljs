(ns rest.test
  (:require [rest.test.client :as client]
            [rest.test.http :as http]
            [rest.test.json :as json]
            [rest.test.macros :as macros]))

(defn ^:export run []
  (client/test)
  (http/test)
  (json/test)
  (macros/test)
  "All tests passed.")