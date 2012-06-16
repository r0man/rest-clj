;;*CLJSBUILD-MACRO-FILE*;
(ns rest.macros
  (:use [clojure.string :only (upper-case)]
        [rest.util :only (format-pattern parse-keys)]))

(defmacro defroute [name args pattern]
  (let [name# name args# args pattern# pattern]
    `(do (swap! rest.core/*routes* assoc ~(keyword name#)
                {:name ~(keyword name#)
                 :args (quote ~args#)
                 :pattern ~pattern#
                 :params ~(parse-keys pattern#)})
         (defn ^:export ~name# [~@args# & {:as ~'params}]
           (merge
            rest.util/*server*
            {:uri (rest.util/format-pattern ~pattern# ~@args#)
             :query-params (or ~'params {})
             :as :auto}))
         (defn ^:export ~(symbol (str name# "-path")) [~@args#]
           (rest.util/format-pattern ~pattern# ~@args#))
         (defn ^:export ~(symbol (str name# "-url")) [~@args#]
           (str (rest.util/server-url)
                (rest.util/format-pattern ~pattern# ~@args#))))))

(defmacro defverb [verb]
  (let [verb# verb]
    `(defn ~verb#
       ~(format "Send the `request` with the %s method." (upper-case verb#))
       [~'request & ~'options]
       (apply rest.client/send-request ~(keyword verb#) ~'request ~'options))))