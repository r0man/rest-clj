(ns rest.json
  (:require [goog.json :as j]))

(defn json-str
  "Serialize `arg` into a JSON string."
  [arg] (j/serialize arg))

(defn read-json
  "Parse `s` a JSON string and returns the result. If `unsafe` is true
  the fn uses eval so it is open to security issues and it should only
  be used if you trust the source."
  [s & {:keys [unsafe]}]
  (if unsafe
    (j/unsafeParse s)
    (j/parse s)))
