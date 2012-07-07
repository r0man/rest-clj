;;*CLJSBUILD-MACRO-FILE*;
(ns rest.macros
  (:refer-clojure :exclude [replace])
  (:require [clojure.string :refer [join split upper-case replace]]
            [inflections.core :refer [singular plural]]
            [rest.client :refer [send-request]]
            [routes.core :refer [defroute]]))

(defn resource-singular [name]
  (if (= (singular name) name)
    (let [[resource & rest] (split (str name) #"-")]
      (join "-" (cons (singular resource) rest)))
    (singular name)))

(defmacro defresources [name args pattern & {:as options}]
  (let [name# name
        args# args
        pattern# pattern
        singular# (symbol (resource-singular name))
        ns# *ns*]
    `(do (defroute ~name# [~@(reverse (rest (reverse args#)))]
           ~(replace pattern# #"/[^/]+$" ""))
         (defroute ~singular# [~@args#]
           ~pattern#)
         (defroute ~(symbol (str "new-" singular#)) []
           ~(str (replace pattern# #"/[^/]+$" "") "/new"))
         (defroute ~(symbol (str "edit-" singular#)) [~@args#]
           ~(str pattern# "/edit"))
         (defn ~name# [~@(rest args#) & {:as ~'opts}]
           (rest.client/send-request
            (~(symbol (str ns# "/" name# "-url")) ~@(rest args#))))
         (defn ~singular# [~@args# & {:as ~'opts}]
           (rest.client/send-request
            (~(symbol (str ns# "/" singular# "-url")) ~@args#) ~'opts))
         (defn ~(symbol (str "create-" singular#)) [~@args# & {:as ~'opts}]
           (rest.client/send-request
            (~(symbol (str ns# "/" name# "-url")) ~@(reverse (rest (reverse args#))))
            (merge {:method :post :body ~(last args#)} ~'opts)))
         (defn ~(symbol (str "delete-" singular#)) [~@args# & {:as ~'opts}]
           (rest.client/send-request
            (~(symbol (str ns# "/" singular# "-url")) ~@args#)
            (merge {:method :delete} ~'opts)))
         (defn ~(symbol (str "update-" singular#)) [~@args# & {:as ~'opts}]
           (rest.client/send-request
            (~(symbol (str ns# "/" singular# "-url")) ~@args#)
            (merge {:method :put :body ~(last args#)} ~'opts))))))

(defmacro defverb [verb]
  (let [verb# verb]
    `(defn ~verb#
       ~(format "Send the `request` with the %s method." (upper-case verb#))
       [~'url & ~'request]
       (apply rest.client/send-request ~(keyword verb#) ~'url ~'request))))

(defmacro with-server
  "Evaluate `body` with *server* bound to `server`."
  [server & body]
  `(routes.core/with-server ~server
     ~@body))
