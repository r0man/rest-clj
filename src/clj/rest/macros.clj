;;*CLJSBUILD-MACRO-FILE*;
(ns rest.macros
  (:refer-clojure :exclude [replace])
  (:require [clojure.string :refer [upper-case replace]]
            [inflections.core :refer [singular plural]]
            [routes.core :as routes]
            ;; rest.client
            ))

(defmacro defresources [name args pattern & {:as options}]
  (let [name# name
        args# args
        pattern# pattern
        singular# (singular name)
        ns# *ns*]
    `(do (routes/defroute ~name# [~@(reverse (rest (reverse args#)))]
           ~(replace pattern# #"/[^/]+$" ""))
         (routes/defroute ~singular# [~@args#]
           ~pattern#)
         (routes/defroute ~(symbol (str "new-" singular#)) []
           ~(str (replace pattern# #"/[^/]+$" "") "/new"))
         (routes/defroute ~(symbol (str "edit-" singular#)) [~@args#]
           ~(str pattern# "/edit"))
         (defn ~name# [& {:as ~'opts}]
           (~'rest.client/send-request
            (~(symbol (str ns# "/" name# "-url")))))
         (defn ~singular# [~singular# & {:as ~'opts}]
           (~'rest.client/send-request
            (~(symbol (str ns# "/" singular# "-url")) ~singular#) ~'opts))
         (defn ~(symbol (str "create-" singular#)) [~singular# & {:as ~'opts}]
           (~'rest.client/send-request
            (~(symbol (str ns# "/" name# "-url")))
            (merge {:method :post :body ~singular#} ~'opts)))
         (defn ~(symbol (str "delete-" singular#)) [~singular# & {:as ~'opts}]
           (~'rest.client/send-request
            (~(symbol (str ns# "/" singular# "-url")) ~singular#)
            (merge {:method :delete} ~'opts)))
         (defn ~(symbol (str "update-" singular#)) [~singular# & {:as ~'opts}]
           (~'rest.client/send-request
            (~(symbol (str ns# "/" singular# "-url")) ~singular#)
            (merge {:method :put :body ~singular#} ~'opts))))))

(defmacro defverb [verb]
  (let [verb# verb]
    `(defn ~verb#
       ~(format "Send the `request` with the %s method." (upper-case verb#))
       [~'url & ~'request]
       (apply rest.client/send-request ~(keyword verb#) ~'url ~'request))))

(defmacro with-server
  "Evaluate `body` with *server* bound to `server`."
  [server & body]
  `(routes/with-server ~server
     ~@body))
