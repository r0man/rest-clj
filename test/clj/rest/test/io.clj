(ns rest.test.io
  (:use [clojure.data.json :only (json-str read-json)]
        clojure.test
        rest.io))

(deftest test-content-type
  (are [type expected]
    (is (= expected (content-type {:headers {"content-type" type}})))
    nil nil
    "" nil
    "appication/json" "appication/json"
    "appication/clojure" "appication/clojure"
    "application/json;charset=UTF-8" "application/json"))

(deftest test-deserialize
  (is (nil? (deserialize nil)))
  (is (= {} (deserialize {})))
  (let [body {:a 1 :b 2}]
    (testing "application/json"
      (binding [*content-type* :application/clojure]
        (let [request (deserialize {:body (prn-str body)})]
          (is (= body (:body request))))))
    (testing "application/json"
      (binding [*content-type* :application/json]
        (let [request (deserialize {:body (json-str body)})]
          (is (= body (:body request))))))))

(deftest test-serialize
  (is (nil? (serialize nil)))
  (is (= {} (serialize {})))
  (let [body {:a 1 :b 2}]
    (testing "application/json"
      (binding [*content-type* :application/clojure]
        (let [request (serialize {:body body})]
          (is (= "application/clojure" (content-type request)))
          (is (= (prn-str body) (:body request))))))
    (testing "application/json"
      (binding [*content-type* :application/json]
        (let [request (serialize {:body body})]
          (is (= "application/json" (content-type request)))
          (is (= (json-str body) (:body request))))))))
