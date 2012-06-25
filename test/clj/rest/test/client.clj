(ns rest.test.client
  (:use clojure.test
        rest.client))

(deftest test-send-request
  (let [body [{:name "Germany"} {:name "Spain"}]]
    (with-redefs
      [*client*
       (fn [request]
         (is (= :get (:method request)))
         (is (= "http://example.com/countries" (:url request)))
         {:body body :page 1 :per-page 2})]
      (let [response (send-request "http://example.com/countries")]
        (is (= body response))
        (is (= {:page 1 :per-page 2} (meta response)))))))
