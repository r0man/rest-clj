(ns rest.test.client
  (:require [clj-http.client :as client])
  (:use clojure.test
        rest.client))

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
  (let [url "http://api.burningswell.com/continents/eu-europe"]
    (is (= (client/parse-url url)
           (to-request url)))
    (is (= (assoc (client/parse-url url) :body {:name "Europe"})
           (to-request {:name "Europe" :uri url})))))
