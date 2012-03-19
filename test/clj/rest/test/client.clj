(ns rest.test.client
  (:import java.net.MalformedURLException)
  (:refer-clojure :exclude (get))
  (:use clojure.test
        rest.client))

(deftest test-get
  (with-redefs
    [send-request
     (fn [request]
       (is (= :get (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (get "http://example.com/users/1-roman"))
  (with-redefs
    [send-request
     (fn [request]
       (is (= :get (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (get {:url "http://example.com/users/1-roman"})))

(deftest test-delete
  (with-redefs
    [send-request
     (fn [request]
       (is (= :delete (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (delete "http://example.com/users/1-roman"))
  (with-redefs
    [send-request
     (fn [request]
       (is (= :delete (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (delete {:url "http://example.com/users/1-roman"})))

(deftest test-head
  (with-redefs
    [send-request
     (fn [request]
       (is (= :head (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (head "http://example.com/users/1-roman"))
  (with-redefs
    [send-request
     (fn [request]
       (is (= :head (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (head {:url "http://example.com/users/1-roman"})))

(deftest test-options
  (with-redefs
    [send-request
     (fn [request]
       (is (= :options (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (options "http://example.com/users/1-roman"))
  (with-redefs
    [send-request
     (fn [request]
       (is (= :options (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (options {:url "http://example.com/users/1-roman"})))

(deftest test-post
  (with-redefs
    [send-request
     (fn [request]
       (is (= :post (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users" (:uri request))))]
    (post "http://example.com/users"))
  (with-redefs
    [send-request
     (fn [request]
       (is (= :post (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users" (:uri request))))]
    (post {:url "http://example.com/users"})))

(deftest test-put
  (with-redefs
    [send-request
     (fn [request]
       (is (= :put (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (put "http://example.com/users/1-roman"))
  (with-redefs
    [send-request
     (fn [request]
       (is (= :put (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (put {:url "http://example.com/users/1-roman"})))

(deftest test-trace
  (with-redefs
    [send-request
     (fn [request]
       (is (= :trace (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (trace "http://example.com/users/1-roman"))
  (with-redefs
    [send-request
     (fn [request]
       (is (= :trace (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (trace {:url "http://example.com/users/1-roman"})))

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
