(ns rest.core
  (:refer-clojure :exclude (get))
  (:use [clj-http.client :only (parse-url)]
        [rest.request :only (send-request)]))

(defprotocol IResource
  (decode [response] "Decode the Ring request map into a `resource`.")
  (encode [resource] "Encode the `resource` into a Ring request map."))

(defn delete
  "Get the represention of `resource`."
  [resource & options]
  (send-request (assoc (encode resource) :request-method :delete)))

(defn get
  "Get the represention of `resource`."
  [resource & {:as query-params}]
  (assoc (encode resource) :request-method :get))

(defn head
  "Check if the `resource` exists."
  [resource & options]
  (send-request (assoc (encode resource) :request-method :head)))

(defn options
  "Get the options of `resource`."
  [resource & options]
  (send-request (assoc (encode resource) :request-method :options)))

(defn post
  "Create a new `resource`."
  [resource & options]
  (send-request (assoc (encode resource) :request-method :post)))

(defn put
  "Change the representation of `resource`."
  [resource & options]
  (send-request (assoc (encode resource) :request-method :put)))

(defn trace
  "Trace the `resource`."
  [resource & options])

(extend-type String
  IResource
  (encode [string]
    (parse-url string)))

(extend-type clojure.lang.IPersistentMap
  IResource
  (encode [{:keys [uri url] :as resource}]
    (cond
     (string? uri)
     resource
     (and (string? url)
          (re-matches #"https?://.*" url))
     (assoc (parse-url url) :body resource))))

;; (get "http://example.com/users" :page 1)
;; (get {:url "http://example.com/users"} :page 1)

;; (encode "/continents")
;; (encode "http://api.burningswell.com/continents?page=1")

(defmacro defendpoint [& args])
(defmacro defresource [& args])
(defmacro defresources [url & args])

;; (defresources countries
;;   "/countries"
;;   [":iso-3166-1-alpha-2-:name" :iso-3166-1-alpha-2 :name])

;; (defresources users
;;   "/users"
;;   [":id-::nick" :id ::nick])

(defresource user ["/users/:id-:nick" :id :nick][user]
  :uri (interpolate ["/users/:id-:nick" :id :nick] user))

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
