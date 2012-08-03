;;*CLJSBUILD-MACRO-FILE*;
(ns rest.core
  (:refer-clojure :exclude [replace])
  (:require [clojure.string :refer [join split upper-case replace]]
            [inflections.core :refer [singular plural]]
            [rest.http :as http]
            [routes.core :refer [defroute]]))

(defn- resource-singular [name]
  (if (= (singular name) name)
    (let [[resource & rest] (split (str name) #"-")]
      (join "-" (cons (singular resource) rest)))
    (singular name)))

(defn- route-args [args]
  (if (> (count args) 1)
    (butlast args) args))

(defmacro defresources [name args pattern & {:as options}]
  (let [name# name
        args# args
        pattern# pattern
        singular# (symbol (resource-singular name))
        singular-url# (symbol (str *ns* "/" singular# "-url"))
        plural-url# (symbol (str *ns* "/" name# "-url"))]
    `(do (defroute ~name# [~@(reverse (rest (reverse args#)))]
           ~(replace pattern# #"/[^/]+$" "")
           :server ~(:server options))

         (defroute ~singular# [~@args#]
           ~pattern#
           :server ~(:server options))

         (defroute ~(symbol (str "new-" singular#)) []
           ~(str (replace pattern# #"/[^/]+$" "") "/new")
           :server ~(:server options))

         (defroute ~(symbol (str "edit-" singular#)) [~@args#]
           ~(str pattern# "/edit")
           :server ~(:server options))

         (defn ~name# [~@(rest args#) & [~'opts]]
           (:body (http/get (~plural-url# ~@(rest args#)) ~'opts)))

         (defn ~singular# [~@args# & [~'opts]]
           (:body (http/get (~singular-url# ~@args#) ~'opts)))

         (defn ~(symbol (str "create-" singular#)) [~@args# & [{:as ~'opts}]]
           (:body (http/post (~plural-url# ~@(reverse (rest (reverse args#))))
                             (assoc ~'opts :body ~(last args#)))))

         (defn ~(symbol (str "delete-" singular#)) [~@args# & [~'opts]]
           (:body (http/delete (~singular-url# ~@args#) ~'opts)))

         (defn ~(symbol (str "update-" singular#)) [~@args# & [{:as ~'opts}]]
           (:body (http/put (~singular-url# ~@args#)
                            (assoc ~'opts :body ~(last args#)))))

         (defn ~(symbol (str "new-" singular# "?")) [~@args# & [~'opts]]
           (not (= 200 (:status (http/head (~singular-url# ~@args#) ~'opts)))))

         (defn ~(symbol (str "new-" singular# "?")) [~@args# & [~'opts]]
           (http/head (~singular-url# ~@args#) ~'opts))

         (defn ~(symbol (str "save-" singular#)) [~@args# & [~'opts]]
           (if (~(symbol (str "new-" singular# "?")) ~@args ~'opts)
             (~(symbol (str "create-" singular#)) ~@args ~'opts)
             (~(symbol (str "update-" singular#)) ~@args ~'opts))))))

(defmacro with-server
  "Evaluate `body` with *server* bound to `server`."
  [server & body]
  `(routes.core/with-server ~server
     ~@body))
