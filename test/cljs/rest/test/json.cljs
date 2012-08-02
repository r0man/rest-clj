(ns rest.test.json
  (:require [rest.json :as json]))

(defn test-json-str []
  (assert (= "null" (json/json-str nil)))
  (assert (= "\"\"" (json/json-str "")))
  (assert (= "\"x\"" (json/json-str "x")))
  (assert (= "1" (json/json-str 1))))

(defn test-read-json []
  (assert (nil? (json/read-json nil)))
  (assert (nil? (json/read-json "")))
  (assert (= 1 (json/read-json "1")))
  (assert (= "x" (json/read-json "\"x\""))))

(defn test []
  (test-json-str)
  (test-read-json))
