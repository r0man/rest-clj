(ns rest.test.core
  (:refer-clojure :exclude (get))
  (:use clojure.test
        rest.core
        rest.client))

(deftest test-get
  (with-redefs
    [*client*
     (fn [request]
       (is (= :get (:method request)))
       (is (= :get (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (get "http://example.com/users/1-roman"))
  (with-redefs
    [*client*
     (fn [request]
       (is (= :get (:method request)))
       (is (= :get (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (get {:url "http://example.com/users/1-roman"})))

(deftest test-delete
  (with-redefs
    [*client*
     (fn [request]
       (is (= :delete (:method request)))
       (is (= :delete (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (delete "http://example.com/users/1-roman"))
  (with-redefs
    [*client*
     (fn [request]
       (is (= :delete (:method request)))
       (is (= :delete (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (delete {:url "http://example.com/users/1-roman"})))

(deftest test-head
  (with-redefs
    [*client*
     (fn [request]
       (is (= :head (:method request)))
       (is (= :head (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (head "http://example.com/users/1-roman"))
  (with-redefs
    [*client*
     (fn [request]
       (is (= :head (:method request)))
       (is (= :head (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (head {:url "http://example.com/users/1-roman"})))

(deftest test-options
  (with-redefs
    [*client*
     (fn [request]
       (is (= :options (:method request)))
       (is (= :options (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (options "http://example.com/users/1-roman"))
  (with-redefs
    [*client*
     (fn [request]
       (is (= :options (:method request)))
       (is (= :options (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (options {:url "http://example.com/users/1-roman"})))

(deftest test-post
  (with-redefs
    [*client*
     (fn [request]
       (is (= :post (:method request)))
       (is (= :post (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users" (:uri request))))]
    (post "http://example.com/users"))
  (with-redefs
    [*client*
     (fn [request]
       (is (= :post (:method request)))
       (is (= :post (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users" (:uri request))))]
    (post {:url "http://example.com/users"})))

(deftest test-put
  (with-redefs
    [*client*
     (fn [request]
       (is (= :put (:method request)))
       (is (= :put (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (put "http://example.com/users/1-roman"))
  (with-redefs
    [*client*
     (fn [request]
       (is (= :put (:method request)))
       (is (= :put (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (put {:url "http://example.com/users/1-roman"})))

(deftest test-trace
  (with-redefs
    [*client*
     (fn [request]
       (is (= :trace (:method request)))
       (is (= :trace (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (trace "http://example.com/users/1-roman"))
  (with-redefs
    [*client*
     (fn [request]
       (is (= :trace (:method request)))
       (is (= :trace (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= "/users/1-roman" (:uri request))))]
    (trace {:url "http://example.com/users/1-roman"})))
