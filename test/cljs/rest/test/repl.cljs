(ns rest.test.repl
  (:require [rest.repl :refer [connect]]))

(defn test-connect []
  (connect))

(defn test []
  (test-connect))
