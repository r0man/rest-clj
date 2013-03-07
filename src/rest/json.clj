(ns rest.json
  (:require [cheshire.core :as json]
            [clojure.string :refer [blank?]]))

(defn json-str [arg]
  (json/encode arg))

(defn read-json [s]
  (cond
   (and (string? s)
        (not (blank? s)))
   (json/decode s true)
   (instance? java.io.InputStream s)
   (json/decode (slurp s) true)))
