(ns rest.routes)

(def ^:dynamic *routes* (atom {}))

(defn route
  "Lookup a route by `name`."
  [name] (get @*routes* (keyword name)))

(defn register
  "Register `route` by it's name."
  [route] (swap! *routes* assoc (:name route) route))
