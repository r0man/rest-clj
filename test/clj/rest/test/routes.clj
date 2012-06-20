(ns rest.test.routes
  (:use clojure.test
        rest.routes
        rest.client))

(deftest test-routes
  (is (nil? (route :unknown-route))))

(deftest test-register
  (let [example-route {:name :example-route}]
    (register example-route)
    (is (= example-route (get @*routes* (:name example-route))))))
