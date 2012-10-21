(ns rest.json
  (:require [clojure.data.json :as data]
            [clojure.string :refer [blank?]]))

(defn json-str [arg]
  (data/write-str arg))

(defn read-json [s]
  (cond
   (and (string? s)
        (not (blank? s)))
   (data/read-str s :key-fn keyword)
   (instance? java.io.InputStream s)
   (read-json (slurp s))))
