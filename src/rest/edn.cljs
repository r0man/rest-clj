(ns rest.edn
  (:require [cljs.reader :as reader]))

(defn read-string [s]
  (reader/read-string s))