(ns rest.json
  (:require [clojure.data.json :as data]
            [clojure.string :refer [blank?]]))

(defn json-str [arg]
  (data/json-str arg))

(defn read-json [s]
  (if-not (blank? s)
    (data/read-json s)))
