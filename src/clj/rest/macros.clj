;;*CLJSBUILD-MACRO-FILE*;
(ns rest.macros
  (:refer-clojure :exclude [replace])
  (:require [clojure.string :refer [join split upper-case replace]]
            [inflections.core :refer [singular plural]]
            [rest.client :refer [send-request]]
            [rest.http :as http]
            [routes.core :refer [defroute]]))

(defn resource-singular [name]
  (if (= (singular name) name)
    (let [[resource & rest] (split (str name) #"-")]
      (join "-" (cons (singular resource) rest)))
    (singular name)))

(defn route-args [args]
  (if (> (count args) 1)
    (butlast args) args))

(defmacro defresources [name args pattern & {:as options}]
  (let [name# name
        args# args
        pattern# pattern
        singular# (symbol (resource-singular name))
        singular-url# (symbol (str *ns* "/" singular# "-url"))
        plural-url# (symbol (str *ns* "/" name# "-url"))
        ns# *ns*]
    `(do (defroute ~name# [~@(reverse (rest (reverse args#)))]
           ~(replace pattern# #"/[^/]+$" ""))

         (defroute ~singular# [~@args#]
           ~pattern#)

         (defroute ~(symbol (str "new-" singular#)) []
           ~(str (replace pattern# #"/[^/]+$" "") "/new"))

         (defroute ~(symbol (str "edit-" singular#)) [~@args#]
           ~(str pattern# "/edit"))

         (defn ~name# [~@(rest args#) & [~'opts]]
           (rest.http/get (~plural-url# ~@(rest args#)) ~'opts))

         (defn ~singular# [~@args# & [~'opts]]
           (rest.http/get (~singular-url# ~@args#) ~'opts))

         (defn ~(symbol (str "create-" singular#)) [~@args# & [{:as ~'opts}]]
           (rest.http/post
            (~plural-url# ~@(reverse (rest (reverse args#))))
            (assoc ~'opts :body ~(last args#))))

         (defn ~(symbol (str "delete-" singular#)) [~@args# & [~'opts]]
           (rest.http/delete (~singular-url# ~@args#) ~'opts))

         (defn ~(symbol (str "update-" singular#)) [~@args# & [{:as ~'opts}]]
           (rest.http/put
            (~singular-url# ~@args#)
            (assoc ~'opts :body ~(last args#))))

         (defn ~(symbol (str "new-" singular# "?")) [~@args# & [~'opts]]
           (let [response#
                 (rest.http/head
                  (~singular-url# ~@args#)
                  ~'opts)]
             (= 200 (:status response#)))))))

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
