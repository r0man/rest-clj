(ns rest.core)

(def ^:dynamic *routes* (atom {}))

(defn route
  "Lookup a route by `name`."
  [name] (get @*routes* (keyword name)))

(defn register-route
  "Register `route` by associng `route` onto *routes* using the value
  of the route's :name key."
  [route] (swap! *routes* assoc (keyword (:name route)) route))

;; (get "http://example.com/users" :page 1)
;; (get {:url "http://example.com/users"} :page 1)

;; (encode "/continents")
;; (encode "http://api.burningswell.com/continents?page=1")

;; (defmacro defendpoint [& args])
;; (defmacro defresource [& args])
;; (defmacro defresources [url & args])

;; (defresources countries
;;   "/countries"
;;   [":iso-3166-1-alpha-2-:name" :iso-3166-1-alpha-2 :name])

;; (defresources users
;;   "/users"
;;   [":id-::nick" :id ::nick])

;; (defresource user ["/users/:id-:nick" :id :nick][user]
;;   :uri (interpolate ["/users/:id-:nick" :id :nick] user))

;; (delete "/countries/de-germany")
;; (delete "/countries/de-germany" :page 1)
;; (delete {:name "Germany" :url "/countries/de-germany"} :page 1)

;; (get "/countries")
;; (get "/countries/de-germany")
;; (get "/countries/de-germany" :page 1)
;; (get {:name "Germany" :url "/countries/de-germany"} :page 1)

;; (head "/countries/de-germany")
;; (head "/countries/de-germany" :page 1)
;; (head {:name "Germany" :url "/countries/de-germany"} :page 1)

;; (post "/countries" {:name "Germany"})
;; (post "/countries" {:name "Germany"} :page 1)
;; (post {:name "Germany" :url "/countries/de-germany"} :page 1)

;; (put "/countries/de-germany" {:name "Germany"})
;; (put "/countries/de-germany" {:name "Germany"} :page 1)
;; (put {:name "Germany" :url "/countries/de-germany"} :page 1)

;; (options "/countries/de-germany" {:name "Germany"})
;; (options "/countries/de-germany" {:name "Germany"} :page 1)
;; (options {:name "Germany" :url "/countries/de-germany"} :page 1)

;; (trace "/countries/de-germany" {:name "Germany"})
;; (trace "/countries/de-germany" {:name "Germany"} :page 1)
;; (trace {:name "Germany" :url "/countries/de-germany"} :page 1)

;; (get "http://example.com/users" :page 1)
;; (get {:url "http://example.com/users"} :page 1)
