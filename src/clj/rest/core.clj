(ns rest.core
  (:refer-clojure :exclude [get])
  (:require;*CLJSBUILD-REMOVE*;-macros
   [rest.macros :refer [defverb defresources]]))

(defverb delete)
(defverb get)
(defverb head)
(defverb options)
(defverb post)
(defverb put)
(defverb trace)
