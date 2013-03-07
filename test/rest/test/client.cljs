(ns rest.test.client
  (:require-macros [cemerick.cljs.test :refer [is deftest]])
  (:require [cemerick.cljs.test :as t]
            [goog.Uri :as uri]
            [rest.client :as client]))

(def europe "http://burningswell.dev/continents/eu-europe")

(deftest test-send-request
  (let [body [{:name "Germany"} {:name "Spain"}]]
    (binding [client/*client*
              (fn [request]
                (is (= :get (:request-method request)))
                (is (= :http (:scheme request)))
                (is (= "burningswell.dev" (:server-name request)))
                (is (= 80 (:server-port request)))
                (is (= "/countries" (:uri request))))]
      (client/send-request :get "http://burningswell.dev/countries"))))

(deftest test-to-request
  (let [request (client/to-request europe)]
    (is (= :get (:request-method request)))
    (is (= :http (:scheme request)))
    (is (= "burningswell.dev" (:server-name request)))
    (is (= 80 (:server-port request)))
    (is (= "/continents/eu-europe" (:uri request)))
    (is (= {} (:body request))))
  (is (= (client/to-request europe)
         (client/to-request {:uri europe})))
  (is (= (client/to-request europe)
         (client/to-request (goog.Uri. europe)))))
