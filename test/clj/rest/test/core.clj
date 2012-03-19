(ns rest.test.core
  (:refer-clojure :exclude (get))
  (:use [rest.request :only (send-request)]
        clojure.test
        rest.core))

(deftest test-get
  (with-redefs
    [send-request
     (fn [request]
       (is (= :get (:request-method request))))]
    (get "/users/1-roman")))

(deftest test-delete
  (with-redefs
    [send-request
     (fn [request]
       (is (= :delete (:request-method request))))]
    (delete "/users/1-roman")))

(deftest test-head
  (with-redefs
    [send-request
     (fn [request]
       (is (= :head (:request-method request))))]
    (head "/users/1-roman")))

(deftest test-options
  (with-redefs
    [send-request
     (fn [request]
       (is (= :options (:request-method request))))]
    (options "/users/1-roman")))

(deftest test-post
  (with-redefs
    [send-request
     (fn [request]
       (is (= :post (:request-method request))))]
    (post "/users")))

(deftest test-put
  (with-redefs
    [send-request
     (fn [request]
       (is (= :put (:request-method request))))]
    (put "/users/1-roman")))

(deftest test-trace
  (with-redefs
    [send-request
     (fn [request]
       (is (= :trace (:request-method request))))]
    (trace "/users/1-roman")))