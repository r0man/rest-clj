(ns rest.edn
  (:refer-clojure :exclude [read-string])
  (:require [clojure.edn :as edn]))

(defn read-string [s]
  (edn/read-string {:readers *data-readers*} s))