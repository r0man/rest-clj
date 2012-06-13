(ns rest.test.json
  (:use clojure.test
        rest.json))

(deftest test-json-str
  (is (= "null" (json-str nil)))
  (is (= "\"\"" (json-str ""))))

(deftest test-read-json
  (is (thrown? Exception (read-json nil)))
  (is (= 1 (read-json "1"))))
