(ns rest.json
  (:require [clojure.data.json :as data]))

(defn json-str [arg]
  (data/json-str arg))

(defn read-json [s]
  (if s (data/read-json s)))
