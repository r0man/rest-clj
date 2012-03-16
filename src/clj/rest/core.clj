(ns rest.core
  (:refer-clojure :exclude (get))
  (:use [rest.request :only (send-request)]))

(defprotocol IResource
  (decode [request] "Encode the `request`.")
  (encode [response] "Decode the `resource`."))

(extend-type String
  IResource
  (encode [string] {:url string}))

(defn delete
  "Get the represention of `resource`."
  [resource & options])

(defn get
  "Get the represention of `resource`."
  [resource & options]
  (send-request (assoc (encode resource) :method :get)))

(defn head
  "Check if the `resource` exists."
  [resource & options])

(defn options
  "Get the options of `resource`."
  [resource & options])

(defn post
  "Create a new `resource`."
  [resource & options])

(defn put
  "Change the representation of `resource`."
  [resource & options])

(defn trace
  "Trace the `resource`."
  [resource & options])

;; (defmacro defendpoint [& args])
;; (defmacro defresource [& args])
;; (defmacro defresources [url & args])

;; (defresources countries
;;   "/countries"
;;   [":iso-3166-1-alpha-2-:name" :iso-3166-1-alpha-2 :name])

;; (defresources users
;;   "/users"
;;   [":id-::nick" :id ::nick])

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
