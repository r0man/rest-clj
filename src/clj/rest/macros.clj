;;*CLJSBUILD-MACRO-FILE*;
(ns rest.macros
  (:refer-clojure :exclude [replace])
  (:require [clojure.string :refer [upper-case replace]]
            [inflections.core :refer [singular plural]]
            [rest.util :refer [format-pattern parse-keys]]
            [routes.core :refer [defroute]]
            rest.client))

(defmacro defresources [name args pattern & {:as options}]
  (let [name# name
        args# args
        pattern# pattern
        singular# (singular name)
        ns# *ns*]
    `(do (defroute ~name# [~@(reverse (rest (reverse args#)))]
           ~(replace pattern# #"/[^/]+$" ""))
         (defroute ~singular# [~@args#]
           ~pattern#)
         (defroute ~(symbol (str "new-" singular#)) []
           ~(str (replace pattern# #"/[^/]+$" "") "/new"))
         (defroute ~(symbol (str "edit-" singular#)) [~@args#]
           ~(str pattern# "/edit"))
         (defn ~name# [& ~'opts]
           (rest.client/send-request
            :get (~(symbol (str ns# "/" name# "-url")))))
         (defn ~singular# [~singular# & ~'opts]
           (rest.client/send-request
            :get (~(symbol (str ns# "/" singular# "-url")) ~singular#) ~'opts))
         (defn ~(symbol (str "create-" singular#)) [~singular# & ~'opts]
           (rest.client/send-request
            :post (~(symbol (str ns# "/" name# "-url")))
            (merge ~'opts {:body ~singular#})))
         (defn ~(symbol (str "delete-" singular#)) [~singular# & ~'opts]
           (rest.client/send-request
            :delete (~(symbol (str ns# "/" singular# "-url")) ~singular#)
            ~'opts))
         (defn ~(symbol (str "update-" singular#)) [~singular# & ~'opts]
           (rest.client/send-request
            :put (~(symbol (str ns# "/" singular# "-url")) ~singular#)
            (merge ~'opts {:body ~singular#}))))))

(defmacro defverb [verb]
  (let [verb# verb]
    `(defn ~verb#
       ~(format "Send the `request` with the %s method." (upper-case verb#))
       [~'url & ~'request]
       (apply rest.client/send-request ~(keyword verb#) ~'url ~'request))))
