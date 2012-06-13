(ns rest.util
  (:refer-clojure :exclude (replace))
  (:use [clojure.string :only (blank? join lower-case split replace replace-first)]
        [inflections.core :only (parameterize)]))

(def ^:dynamic *server*
  {:scheme :https :server-name "example.com"})

(defn identifier [resource keyseq]
  (->> (map resource keyseq)
       (map str)
       (remove blank?)
       (map lower-case)
       (map parameterize)
       (join "-")))

(defn path
  "Make a path by joining `segments` with a slash."
  [& segments]
  (->> (map str segments)
       (remove blank?)
       (map #(replace %1 #"^/+" ""))
       (join "/")
       (str "/")))

(defn parse-keys [pattern]
  (->> (split pattern #"/")
       (map (fn [segment]
              (->> (re-seq #"\:[^:]+" segment)
                   (map #(replace %1 ":" ""))
                   (map #(replace %1 #"-$" ""))
                   (map keyword)
                   (apply vector))))
       (remove empty?)
       (apply vector)))

(defn format-pattern [pattern & args]
  (reduce
   (fn [pattern [keyseq arg]]
     (reduce #(replace-first
               %1 (str %2)
               (parameterize (lower-case (str (get arg %2)))))
             pattern keyseq))
   pattern (map vector (parse-keys pattern) args)))

(defn server-url []
  (let [{:keys [scheme server-name server-port]} *server*]
    (assert scheme)
    (assert server-name)
    (str (name scheme) "://" server-name
         (if server-port (str ":" server-port)))))
