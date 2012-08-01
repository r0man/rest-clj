(ns rest.test.client
  (:require [clj-http.client :as client])
  (:use clojure.test
        rest.client))

(def europe "http://example.com/continents/eu-europe")

(deftest test-send-request
  (let [body [{:name "Germany"} {:name "Spain"}]]
    (with-redefs
      [*client*
       (fn [request]
         (is (= :get (:request-method request)))
         (is (= "http://example.com/countries" (:uri request)))
         {:body body :page 1 :per-page 2})]
      (let [response (send-request "http://example.com/countries")]
        (is (= body response))
        (is (= {:page 1 :per-page 2} (meta response)))))))

(deftest test-to-request
  (let [request (to-request europe)]
    (is (= :get (:request-method request)))
    (is (= :http (:scheme request)))
    (is (= "example.com" (:server-name request)))
    (is (= "/continents/eu-europe" (:uri request))))
  (let [request (to-request {:uri europe})]
    (is (= :get (:request-method request)))
    (is (= :http (:scheme request)))
    (is (= "example.com" (:server-name request)))
    (is (= "/continents/eu-europe" (:uri request)))
    (is (= request (to-request request)))))
