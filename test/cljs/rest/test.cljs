(ns rest.test
  (:require [rest.test.core :as core]))

(defn ^:export run []
  (core/test)
  "All tests passed.")