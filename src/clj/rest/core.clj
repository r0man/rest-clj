(ns rest.core
  (:refer-clojure :exclude (get))
  (:use;*CLJSBUILD-REMOVE*;-macros
   [rest.macros :only (defverb defresources)]))

(def ^:dynamic *routes* (atom {}))

(defn route
  "Lookup a route by `name`."
  [name] (clojure.core/get @*routes* (keyword name)))

(defverb delete)
(defverb get)
(defverb head)
(defverb options)
(defverb post)
(defverb put)
(defverb trace)
