(ns rest.core
  (:refer-clojure :exclude (get))
  (:use;*CLJSBUILD-REMOVE*;-macros
   [rest.macros :only (defverb defresources)]))

(defverb delete)
(defverb get)
(defverb head)
(defverb options)
(defverb post)
(defverb put)
(defverb trace)
