(ns rest.test
  (:require [rest.test.core :as core]
            [rest.test.json :as json]
            [rest.test.macros :as macros]))

(defn ^:export run []
  (core/test)
  (json/test)
  (macros/test)
  "All tests passed.")