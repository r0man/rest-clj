(ns rest.test.client
  (:require [goog.Uri :as uri]
            [rest.client :as client]))

(def europe "http://burningswell.dev/continents/eu-europe")

(defn test-send-request []
  (let [body [{:name "Germany"} {:name "Spain"}]]
    (binding [client/*client*
              (fn [request]
                (assert (= :get (:request-method request)))
                (assert (= :http (:scheme request)))
                (assert (= "burningswell.dev" (:server-name request)))
                (assert (= 80 (:server-port request)))
                (assert (= "/countries" (:uri request))))]
      (client/send-request :get "http://burningswell.dev/countries"))))

(defn test-to-request []
  (let [request (client/to-request europe)]
    (assert (= :get (:request-method request)))
    (assert (= :http (:scheme request)))
    (assert (= "burningswell.dev" (:server-name request)))
    (assert (= 80 (:server-port request)))
    (assert (= "/continents/eu-europe" (:uri request)))
    (assert (= {} (:body request))))
  (assert (= (client/to-request europe)
             (client/to-request {:uri europe})))
  (assert (= (client/to-request europe)
             (client/to-request (goog.Uri. europe)))))

(defn test []
  (test-send-request)
  (test-to-request))
