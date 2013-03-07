(ns rest.test.json
  (:use clojure.test
        rest.json))

(deftest test-json-str
  (is (= "null" (json-str nil)))
  (is (= "\"\"" (json-str "")))
  (is (= "\"x\"" (json-str "x")))
  (is (= "1" (json-str 1))))

(deftest test-read-json
  (is (nil? (read-json nil)))
  (is (nil? (read-json "")))
  (is (= 1 (read-json "1"))))
