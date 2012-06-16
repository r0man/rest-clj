(ns rest.test.client
  (:import java.net.MalformedURLException)
  (:refer-clojure :exclude (get))
  (:use clojure.test
        rest.client))

(deftest test-request
  (is (thrown? MalformedURLException (request "")))
  (is (thrown? MalformedURLException (request "x")))
  (let [request (request "http://example.com/")]
    (is (= :http (:scheme request)))
    (is (= "example.com" (:server-name request)))
    (is (= "/" (:uri request))))
  (let [request (request {:url "http://example.com/"})]
    (is (= :http (:scheme request)))
    (is (= "example.com" (:server-name request)))
    (is (= "/" (:uri request)))))
