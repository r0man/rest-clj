(ns rest.test.client
  (:require [goog.Uri :as uri]
            [rest.client :as client]))

(def europe "http://example.com/continents/eu-europe")

(defn test-send-request []
  (let [body [{:name "Germany"} {:name "Spain"}]]
    (binding [client/*client*
              (fn [request]
                (assert (= :get (:request-method request)))
                (assert (= "http://example.com/countries" (:uri request)))
                {:body body :page 1 :per-page 2})]
      (client/send-request "http://example.com/countries"))))

(defn test-to-request []
  (let [request (client/to-request europe)]
    (assert (= :get (:request-method request)))
    (assert (= :http (:scheme request)))
    (assert (= "example.com" (:server-name request)))
    (assert (= "/continents/eu-europe" (:uri request))))
  (let [request (client/to-request (goog.Uri. europe))]
    (assert (= :get (:request-method request)))
    (assert (= :http (:scheme request)))
    (assert (= "example.com" (:server-name request)))
    (assert (= "/continents/eu-europe" (:uri request))))
  (let [request (client/to-request {:uri europe})]
    (assert (= :get (:request-method request)))
    (assert (= :http (:scheme request)))
    (assert (= "example.com" (:server-name request)))
    (assert (= "/continents/eu-europe" (:uri request)))
    (assert (= request (client/to-request request)))))

(defn test []
  (test-send-request)
  (test-to-request))