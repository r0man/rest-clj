;;*CLJSBUILD-MACRO-FILE*;
(ns rest.macros
  (:refer-clojure :exclude (replace))
  (:use [clojure.string :only (upper-case replace)]
        [inflections.core :only (singular plural)]
        [rest.util :only (format-pattern parse-keys)]))

(defmacro defroute [name args pattern]
  (let [name# name args# args pattern# pattern]
    `(do (def ~(symbol (str name# "-route"))
           {:name ~(keyword name#)
            :args (quote ~args#)
            :pattern ~pattern#
            :params ~(parse-keys pattern#)})
         (swap! rest.routes/*routes* assoc ~(keyword name#)
                {:name ~(keyword name#)
                 :args (quote ~args#)
                 :pattern ~pattern#
                 :params ~(parse-keys pattern#)})
         (defn ^:export ~(symbol (str name# "-path")) [~@args#]
           (rest.util/format-pattern ~pattern# ~@args#))
         (defn ^:export ~(symbol (str name# "-url")) [~@args#]
           (str (rest.util/server-url)
                (rest.util/format-pattern ~pattern# ~@args#))))))

(defmacro defresources [name args pattern & {:as options}]
  (let [name# name
        args# args
        pattern# pattern
        singular# (singular name)]
    `(do (defroute ~name# [~@(reverse (rest (reverse args#)))]
           ~(replace pattern# #"/[^/]+$" ""))
         (defroute ~singular# [~@args#]
           ~pattern#)
         (defroute ~(symbol (str "new-" singular#)) []
           ~(str (replace pattern# #"/[^/]+$" "") "/new"))
         (defroute ~(symbol (str "edit-" singular#)) [~@args#]
           ~(str pattern# "/edit")))))

(defmacro defverb [verb]
  (let [verb# verb]
    `(defn ~verb#
       ~(format "Send the `request` with the %s method." (upper-case verb#))
       [~'request & ~'options]
       (apply rest.client/send-request ~(keyword verb#) ~'request ~'options))))
