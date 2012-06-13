(ns rest.test.core
  (:require [rest.core :as core]))

(defn test-route []
  (assert (nil? (core/route :unknown-route))))

(defn test-register-route []
  (let [route {:name "x"}]
    (core/register-route route)
    (assert (= route (core/route (:name route))))))

(defn test []
  (test-route))