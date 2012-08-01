(ns rest.test.http
  (:require [rest.client :refer [*client*]]
            [rest.http :as http]))

(def europe "http://api.burningswell.dev/continents/eu-europe")

(defn test-delete []
  (binding [*client*
            #(do (assert (= :delete (:request-method %1)))
                 (assert (= :http (:scheme %1)))
                 (assert (= "api.burningswell.dev" (:server-name %1)))
                 (assert (= "/continents/eu-europe" (:uri %1))))]
    (http/delete europe)))

(defn test-get []
  (binding [*client*
            #(do (assert (= :get (:request-method %1)))
                 (assert (= :http (:scheme %1)))
                 (assert (= "api.burningswell.dev" (:server-name %1)))
                 (assert (= "/continents/eu-europe" (:uri %1))))]
    (http/get europe)))

(defn test-head []
  (binding [*client*
            #(do (assert (= :head (:request-method %1)))
                 (assert (= :http (:scheme %1)))
                 (assert (= "api.burningswell.dev" (:server-name %1)))
                 (assert (= "/continents/eu-europe" (:uri %1))))]
    (http/head europe)))

(defn test-move []
  (binding [*client*
            #(do (assert (= :move (:request-method %1)))
                 (assert (= :http (:scheme %1)))
                 (assert (= "api.burningswell.dev" (:server-name %1)))
                 (assert (= "/continents/eu-europe" (:uri %1))))]
    (http/move europe)))

(defn test-options []
  (binding [*client*
            #(do (assert (= :options (:request-method %1)))
                 (assert (= :http (:scheme %1)))
                 (assert (= "api.burningswell.dev" (:server-name %1)))
                 (assert (= "/continents/eu-europe" (:uri %1))))]
    (http/options europe)))

(defn test-patch []
  (binding [*client*
            #(do (assert (= :patch (:request-method %1)))
                 (assert (= :http (:scheme %1)))
                 (assert (= "api.burningswell.dev" (:server-name %1)))
                 (assert (= "/continents/eu-europe" (:uri %1))))]
    (http/patch europe)))

(defn test-post []
  (binding [*client*
            #(do (assert (= :post (:request-method %1)))
                 (assert (= :http (:scheme %1)))
                 (assert (= "api.burningswell.dev" (:server-name %1)))
                 (assert (= "/continents/eu-europe" (:uri %1))))]
    (http/post europe)))

(defn test-put []
  (binding [*client*
            #(do (assert (= :put (:request-method %1)))
                 (assert (= :http (:scheme %1)))
                 (assert (= "api.burningswell.dev" (:server-name %1)))
                 (assert (= "/continents/eu-europe" (:uri %1))))]
    (http/put europe)))

(defn test []
  (test-delete)
  (test-get)
  (test-head)
  (test-move)
  (test-options)
  (test-patch)
  (test-post)
  (test-put))