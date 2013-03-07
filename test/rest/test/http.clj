(ns rest.test.http
  (:require [clojure.test :refer :all]
            [rest.client :refer [*client*]]
            [rest.http :as http]))

(def europe "http://api.burningswell.dev/continents/eu-europe")

(deftest test-delete
  (with-redefs
    [*client*
     #(do (is (= :delete (:request-method %1)))
          (is (= :http (:scheme %1)))
          (is (= "api.burningswell.dev" (:server-name %1)))
          (is (= "/continents/eu-europe" (:uri %1))))]
    (http/delete europe)))

(deftest test-get
  (with-redefs
    [*client*
     #(do (is (= :get (:request-method %1)))
          (is (= :http (:scheme %1)))
          (is (= "api.burningswell.dev" (:server-name %1)))
          (is (= "/continents/eu-europe" (:uri %1))))]
    (http/get europe)))

(deftest test-head
  (with-redefs
    [*client*
     #(do (is (= :head (:request-method %1)))
          (is (= :http (:scheme %1)))
          (is (= "api.burningswell.dev" (:server-name %1)))
          (is (= "/continents/eu-europe" (:uri %1))))]
    (http/head europe)))

(deftest test-move
  (with-redefs
    [*client*
     #(do (is (= :move (:request-method %1)))
          (is (= :http (:scheme %1)))
          (is (= "api.burningswell.dev" (:server-name %1)))
          (is (= "/continents/eu-europe" (:uri %1))))]
    (http/move europe)))

(deftest test-options
  (with-redefs
    [*client*
     #(do (is (= :options (:request-method %1)))
          (is (= :http (:scheme %1)))
          (is (= "api.burningswell.dev" (:server-name %1)))
          (is (= "/continents/eu-europe" (:uri %1))))]
    (http/options europe)))

(deftest test-patch
  (with-redefs
    [*client*
     #(do (is (= :patch (:request-method %1)))
          (is (= :http (:scheme %1)))
          (is (= "api.burningswell.dev" (:server-name %1)))
          (is (= "/continents/eu-europe" (:uri %1))))]
    (http/patch europe)))

(deftest test-post
  (with-redefs
    [*client*
     #(do (is (= :post (:request-method %1)))
          (is (= :http (:scheme %1)))
          (is (= "api.burningswell.dev" (:server-name %1)))
          (is (= "/continents/eu-europe" (:uri %1))))]
    (http/post europe)))

(deftest test-put
  (with-redefs
    [*client*
     #(do (is (= :put (:request-method %1)))
          (is (= :http (:scheme %1)))
          (is (= "api.burningswell.dev" (:server-name %1)))
          (is (= "/continents/eu-europe" (:uri %1))))]
    (http/put europe)))
