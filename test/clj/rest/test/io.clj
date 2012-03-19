(ns rest.test.io
  (:use [clojure.data.json :only (json-str read-json)]
        clojure.test
        rest.io))

(deftest test-content-type
  (is (nil? (content-type {})))
  (is (nil? (content-type {:headers {}})))
  (is (= "application/json" (content-type {:headers {"content-type" "application/json"}}))))

(deftest test-deserialize-content-type
  (is (nil? (deserialize-content-type nil)))
  (is (= {} (deserialize-content-type {})))
  (let [body {:a 1 :b 2}]
    (testing "application/json"
      (with-content-type :application/clojure
        (let [request (deserialize-content-type {:body (prn-str body)})]
          (is (= body (:body request))))))
    (testing "application/json"
      (with-content-type :application/json
        (let [request (deserialize-content-type {:body (json-str body)})]
          (is (= body (:body request))))))))

(deftest test-serialize-content-type
  (is (nil? (serialize-content-type nil)))
  (is (= {} (serialize-content-type {})))
  (let [body {:a 1 :b 2}]
    (testing "application/json"
      (with-content-type :application/clojure
        (let [request (serialize-content-type {:body body})]
          (is (= "application/clojure" (content-type request)))
          (is (= (prn-str body) (:body request))))))
    (testing "application/json"
      (with-content-type :application/json
        (let [request (serialize-content-type {:body body})]
          (is (= "application/json" (content-type request)))
          (is (= (json-str body) (:body request))))))))
