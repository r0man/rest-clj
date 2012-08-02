(ns rest.test.stacktrace
  (:require [rest.stacktrace :refer :all]
            [clj-stacktrace.core :refer [parse-exception]]
            [clojure.test :refer :all]))

(def raw-exception
  (try ("nofn") (catch Exception e e)))

(def parsed-exception (parse-exception raw-exception))

(deftest test-make-trace-element
  (let [expected (nth (.getStackTrace raw-exception) 0)
        element (make-trace-element (nth (:trace-elems parsed-exception) 0))]
    (is (re-matches #"rest.test.stacktrace\$fn.invoke\(stacktrace.clj:7\)$"
                    (str element)))
    (is (re-matches #"rest.test.stacktrace\$fn$"
                    (.getClassName element)))
    (is (=  "rest.test.stacktrace$fn.invoke(stacktrace.clj:7)" (str element)))
    (is (= "rest.test.stacktrace$fn" (.getClassName element)))
    (is (= (.getFileName expected) (.getFileName element)))
    (is (= (.getLineNumber expected) (.getLineNumber element)))
    (is (= (.getMethodName expected) (.getMethodName element))))
  (let [expected (nth (.getStackTrace raw-exception) 11)
        element (make-trace-element (nth (:trace-elems parsed-exception) 11))]
    (is (re-matches #"rest.test.stacktrace\$eval.*(\d+).invoke\(NO_SOURCE_FILE:0\)$"
                    (str element)))
    (is (re-matches #"rest.test.stacktrace\$eval.(\d+)$"
                    (.getClassName element)))
    (is (= (.getFileName expected) (.getFileName element)))
    (is (= (.getLineNumber expected) (dec (.getLineNumber element))))
    (is (= (.getMethodName expected) (.getMethodName element)))))

(deftest test-make-throwable
  (let [throwable (make-throwable parsed-exception)]
    (is (= (:message parsed-exception)
           (.getMessage throwable)))
    (is (= (map make-trace-element (:trace-elems parsed-exception))
           (seq (.getStackTrace throwable))))))


(deftest test-wrap-stacktrace-client
  (is (= {:status 200} ((wrap-stacktrace-client (fn [_] {:status 200})) {})))
  (try
    ((wrap-stacktrace-client (fn [_] {:status 500 :body parsed-exception})) {})
    (assert false)
    (catch Throwable t
      (is (= (.getMessage raw-exception) (.getMessage t))))))

(deftest test-wrap-stacktrace-server
  (let [response ((wrap-stacktrace-server (fn [r] (throw raw-exception))) {})]
    (is (= 500 (:status response)))
    (is (= parsed-exception (:body response)))))