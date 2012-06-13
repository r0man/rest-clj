(ns rest.test.json
  (:require [rest.json :as json]))

(defn test-json-str []
  (assert (= "null" (json/json-str nil)))
  (assert (= "\"\"" (json/json-str ""))))

(defn test-read-json []
  (assert (nil? (json/read-json nil)))
  (assert (= 1 (json/read-json "1")))
  (assert (= "x" (json/read-json "\"x\""))))

(defn test []
  (test-json-str)
  (test-read-json))
