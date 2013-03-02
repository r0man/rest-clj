(ns rest.stacktrace
  (:require [clj-http.client :refer [unexceptional-status?]]
            [clj-stacktrace.core :refer [parse-exception]]))

(defn format-class
  "Format the class name of a clj-stacktrace trace element."
  [trace-elem]
  (cond
   (:class trace-elem)
   (:class trace-elem)
   (and (:ns trace-elem) (:fn trace-elem))
   (format "%s$%s" (:ns trace-elem) (:fn trace-elem))))

(defn format-method
  "Format the method name of a clj-stacktrace trace element."
  [trace-elem]
  (cond
   (:method trace-elem)
   (:method trace-elem)
   (and (:ns trace-elem) (:fn trace-elem))
   "invoke"))

(defn make-trace-element
  "Make a StackTraceElement from a clj-stacktrace trace element."
  [trace-elem]
  (StackTraceElement.
   (format-class trace-elem)
   (format-method trace-elem)
   (:file trace-elem)
   (or (:line trace-elem) -1)))

(defn make-throwable
  "Make a Throwable from a clj-stacktrace exception map."
  [exception]
  (let [elements (map make-trace-element (:trace-elems exception))]
    (doto (try
            (clojure.lang.Reflector/invokeConstructor
             (resolve (symbol (if (class? (:class exception))
                                (.getName (:class exception))
                                (str (:class exception)))))
             (to-array [(:message exception)]))
            (catch Exception _
              (Throwable. (:message exception))))
      (.setStackTrace (into-array StackTraceElement elements)))))

(defn wrap-stacktrace-client
  "Wrap the clj-http `handler` and return thrown exceptions in a map."
  [handler & [status]]
  (let [status (or status 500)]
    (fn [request]
      (let [response (handler request)]
        (cond
         (= 404 (:status response))
         (assoc response :body nil)
         (= 422 (:status response))
         (throw (IllegalArgumentException. (str (:body response))))
         (unexceptional-status? (:status response))
         response
         :else (throw (make-throwable (:body response))))))))

(defn wrap-stacktrace-server
  "Wrap the Ring `handler` and return thrown exceptions in a map."
  [handler & [status]]
  (let [status (or status 500)]
    (fn [request]
      (try
        (handler request)
        (catch Exception e
          {:status status :body (parse-exception e)})))))
