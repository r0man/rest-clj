(ns rest.test.client
  (:require [rest.client :refer [*client* send-request]]))

(defn test-send-request []
  (let [body [{:name "Germany"} {:name "Spain"}]]
    (binding [*client*
              (fn [request]
                (assert (= :get (:request-method request)))
                (assert (= "http://example.com/countries" (:url request)))
                {:body body :page 1 :per-page 2})]
      (send-request "http://example.com/countries"))))

(defn test []
  (test-send-request))