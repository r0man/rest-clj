(ns rest.test
  (:require [clojure.browser.repl :as repl]
            [rest.test.client :as client]
            [rest.test.core :as core]
            [rest.test.http :as http]
            [rest.test.json :as json]))

(defn ^:export connect []
  (repl/connect "http://localhost:9000/repl"))

(defn ^:export run []
  (client/test)
  (core/test)
  (http/test)
  (json/test)
  "All tests passed.")
