(ns rest.test.io
  (:use clojure.test
        rest.io))

(deftest test-content-type
  (is (nil? (content-type {})))
  (is (nil? (content-type {:headers {}})))
  (is (= "application/json" (content-type {:headers {"Content-Type" "application/json"}}))))