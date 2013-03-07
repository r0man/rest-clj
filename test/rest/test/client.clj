(ns rest.test.client
  (:require [clj-http.client :as client])
  (:use clojure.test
        rest.client))

(def europe "http://burningswell.dev/continents/eu-europe")

(deftest test-send-request
  (let [body [{:name "Germany"} {:name "Spain"}]
        response {:body body :page 1 :per-page 2}]
    (with-redefs
      [*client*
       (fn [request]
         (is (= :get (:request-method request)))
         (is (= :http (:scheme request)))
         (is (= "burningswell.dev" (:server-name request)))
         (is (= 80 (:server-port request)))
         (is (= "/countries" (:uri request)))
         response)]
      (is (= response (send-request :get "http://burningswell.dev/countries"))))))

(deftest test-to-request
  (let [request (to-request europe)]
    (is (= :get (:request-method request)))
    (is (= :http (:scheme request)))
    (is (= "burningswell.dev" (:server-name request)))
         (is (= 80 (:server-port request)))
    (is (= "/continents/eu-europe" (:uri request)))
    (is (nil? (:body request))))
  (let [request (to-request {:uri europe})]
    (is (= :get (:request-method request)))
    (is (= :http (:scheme request)))
    (is (= "burningswell.dev" (:server-name request)))
    (is (= 80 (:server-port request)))
    (is (= "/continents/eu-europe" (:uri request)))
    (is (= request (to-request request))))
  (is (= (to-request europe)
         (to-request {:uri europe})))
  (is (= (to-request europe)
         (to-request (java.net.URL. europe))))
  (is (= (to-request europe)
         (to-request (java.net.URI. europe)))))
