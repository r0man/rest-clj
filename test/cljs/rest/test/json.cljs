(ns rest.test.json
  (:require-macros [cemerick.cljs.test :refer [is deftest]])
  (:require [rest.json :as json]))

(deftest test-json-str
  (is (= "null" (json/json-str nil)))
  (is (= "\"\"" (json/json-str "")))
  (is (= "\"x\"" (json/json-str "x")))
  (is (= "1" (json/json-str 1))))

(deftest test-read-json
  (is (nil? (json/read-json nil)))
  (is (nil? (json/read-json "")))
  (is (= 1 (json/read-json "1")))
  (is (= "x" (json/read-json "\"x\""))))
