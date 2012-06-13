(ns rest.test
  (:require [rest.test.core :as core]
            [rest.test.json :as json]))

(defn ^:export run []
  (core/test)
  (json/test)
  "All tests passed.")