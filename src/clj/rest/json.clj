(ns rest.json
  (:require [clojure.data.json :as j]))

(defn json-str [arg]
  (j/json-str arg))

(defn read-json [s]
  (if s (j/read-json s)))
