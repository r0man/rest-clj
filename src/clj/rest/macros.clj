;;*CLJSBUILD-MACRO-FILE*;
(ns rest.macros
  (:use [rest.util :only (format-pattern parse-keys)]))

(defmacro defroute [name args pattern]
  (let [name# name args# args pattern# pattern]
    `(do (defn ^:export ~name# [~@args# & {:as ~'params}]
           {:uri (rest.util/format-pattern ~pattern# ~@args#)
            :query-params (or ~'params {})})
         (defn ^:export ~(symbol (str name# "-path")) [~@args#]
           (rest.util/format-pattern ~pattern# ~@args#))
         (def ^:export ~(symbol (str name# "-route"))
           {:name ~(keyword name#)
            :args (quote ~args#)
            :pattern ~pattern#
            :params ~(parse-keys pattern#)})
         (defn ^:export ~(symbol (str name# "-url")) [~@args#]
           (str (rest.util/server-url)
                (rest.util/format-pattern ~pattern# ~@args#))))))
