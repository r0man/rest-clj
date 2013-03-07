(ns rest.test.http
  (:require-macros [cemerick.cljs.test :refer [is deftest]])
  (:require [cemerick.cljs.test :as t]
            [rest.client :refer [*client*]]
            [rest.http :as http]))

(def europe "http://api.burningswell.dev/continents/eu-europe")

(deftest test-delete
  (binding [*client*
            #(do (is (= :delete (:request-method %1)))
                 (is (= :http (:scheme %1)))
                 (is (= "api.burningswell.dev" (:server-name %1)))
                 (is (= "/continents/eu-europe" (:uri %1))))]
    (http/delete europe)))

(deftest test-get
  (binding [*client*
            #(do (is (= :get (:request-method %1)))
                 (is (= :http (:scheme %1)))
                 (is (= "api.burningswell.dev" (:server-name %1)))
                 (is (= "/continents/eu-europe" (:uri %1))))]
    (http/get europe)))

(deftest test-head
  (binding [*client*
            #(do (is (= :head (:request-method %1)))
                 (is (= :http (:scheme %1)))
                 (is (= "api.burningswell.dev" (:server-name %1)))
                 (is (= "/continents/eu-europe" (:uri %1))))]
    (http/head europe)))

(deftest test-move
  (binding [*client*
            #(do (is (= :move (:request-method %1)))
                 (is (= :http (:scheme %1)))
                 (is (= "api.burningswell.dev" (:server-name %1)))
                 (is (= "/continents/eu-europe" (:uri %1))))]
    (http/move europe)))

(deftest test-options
  (binding [*client*
            #(do (is (= :options (:request-method %1)))
                 (is (= :http (:scheme %1)))
                 (is (= "api.burningswell.dev" (:server-name %1)))
                 (is (= "/continents/eu-europe" (:uri %1))))]
    (http/options europe)))

(deftest test-patch
  (binding [*client*
            #(do (is (= :patch (:request-method %1)))
                 (is (= :http (:scheme %1)))
                 (is (= "api.burningswell.dev" (:server-name %1)))
                 (is (= "/continents/eu-europe" (:uri %1))))]
    (http/patch europe)))

(deftest test-post
  (binding [*client*
            #(do (is (= :post (:request-method %1)))
                 (is (= :http (:scheme %1)))
                 (is (= "api.burningswell.dev" (:server-name %1)))
                 (is (= "/continents/eu-europe" (:uri %1))))]
    (http/post europe)))

(deftest test-put
  (binding [*client*
            #(do (is (= :put (:request-method %1)))
                 (is (= :http (:scheme %1)))
                 (is (= "api.burningswell.dev" (:server-name %1)))
                 (is (= "/continents/eu-europe" (:uri %1))))]
    (http/put europe)))
