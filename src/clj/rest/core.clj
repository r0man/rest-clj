;;*CLJSBUILD-MACRO-FILE*;
(ns rest.core
  (:refer-clojure :exclude [replace])
  (:require [clojure.string :refer [join split upper-case replace]]
            [inflections.core :refer [singular plural]]
            [rest.http :as http]
            [routes.core :refer [defroute qualify]]
            [routes.helper :refer [make-route route route-args]]))

(defn- resource-singular [name]
  (if (= (singular name) name)
    (let [[resource & rest] (split (str name) #"-")]
      (join "-" (cons (singular resource) rest)))
    (singular name)))

(defn plural-pattern [pattern]
  (replace pattern #"/[^/]+$" ""))

(defn singular-pattern [pattern]
  (str "/" (last (split pattern #"/"))))

(defn singular-route [name]
  (symbol (str (resource-singular name) "-route")))

(defn plural-route [name]
  (symbol (str name "-route")))

(defmacro defresources [name args [pattern & params] & {:as options}]
  (let [name# name
        args# args
        pattern# pattern
        params# params
        options# options
        singular# (symbol (resource-singular name))
        singular-url# (symbol (str *ns* "/" singular# "-url"))
        singular-opts# (mapcat concat (seq (assoc options# :root (symbol (str name# "-route")))))
        plural-url# (symbol (str *ns* "/" name# "-url"))
        route# (make-route (str *ns*) name# args# pattern# params# options#)
        route# (assoc route# :root (route (qualify (:root options#))))]
    `(do
       (defroute ~name# [~@(butlast (:args route#))]
         [~(plural-pattern pattern#) ~@(butlast (:params route#))]
          ~@(mapcat concat (seq options#)))

       (defroute ~singular# [~@(:args route#)]
         [~(singular-pattern pattern#) ~@params#]
         :root ~(plural-route name#))

       (defroute ~(symbol (str "new-" singular#)) []
         ["/new"] :root ~(plural-route name#))

       (defroute ~(symbol (str "edit-" singular#)) []
         ["/edit"] :root ~(singular-route name#))

       (defn ~name# [~@(butlast (route-args route#)) & [~'opts]]
         (rest.io/meta-body (http/get (~plural-url# ~@(butlast (route-args route#))) ~'opts)))

       (defn ~singular# [~@(route-args route#) & [~'opts]]
         (rest.io/meta-body (http/get (~singular-url# ~@(route-args route#)) ~'opts)))

       (defn ~(symbol (str "create-" singular#)) [~@(route-args route#) & [{:as ~'opts}]]
         (rest.io/meta-body (http/post (~plural-url# ~@(butlast (route-args route#)))
                                       (assoc ~'opts :body ~(last (route-args route#))))))

       (defn ~(symbol (str "delete-" singular#)) [~@(route-args route#) & [~'opts]]
         (rest.io/meta-body (http/delete (~singular-url# ~@(route-args route#)) ~'opts)))

       (defn ~(symbol (str "update-" singular#)) [~@(route-args route#) & [{:as ~'opts}]]
         (rest.io/meta-body (http/put (~singular-url# ~@(route-args route#))
                                      (assoc ~'opts :body ~(last (route-args route#))))))

       (defn ~(symbol (str "new-" singular# "?")) [~@(route-args route#) & [~'opts]]
         (not (= 200 (:status (http/head (~singular-url# ~@(route-args route#)) ~'opts)))))

       (defn ~(symbol (str "save-" singular#)) [~@(route-args route#) & [~'opts]]
         (if (~(symbol (str "new-" singular# "?")) ~@(route-args route#) ~'opts)
           (~(symbol (str "create-" singular#)) ~@(route-args route#) ~'opts)
           (~(symbol (str "update-" singular#)) ~@(route-args route#) ~'opts))))))
