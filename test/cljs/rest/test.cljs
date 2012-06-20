(ns rest.test
  (:require [rest.test.core :as core]
            [rest.test.json :as json]
            [rest.test.macros :as macros]
            [rest.test.json :as routes]))

(defn ^:export run []
  (core/test)
  (json/test)
  (macros/test)
  (routes/test)
  "All tests passed.")