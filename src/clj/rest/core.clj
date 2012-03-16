(ns rest.core
  (:refer-clojure :exlude (get)))

(defn delete
  "Get the represention of `resource`."
  [resource & options])

(defn get
  "Get the represention of `resource`."
  [resource & options])

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

(defmacro defendpoint [& args])
(defmacro defresource [& args])
(defmacro defresources [url & args])

(defresources countries
  "/countries"
  [":iso-3166-1-alpha-2-:name" :iso-3166-1-alpha-2 :name])

(defresources users
  "/users"
  [":id-::nick" :id ::nick])

(delete "/countries/1-germany")
(delete "/countries/1-germany" :page 1)
(delete {:name "Germany" :url "/countries/1-germany"} :page 1)

(get "/countries/1-germany")
(get "/countries/1-germany" :page 1)
(get {:name "Germany" :url "/countries/1-germany"} :page 1)

(head "/countries/1-germany")
(head "/countries/1-germany" :page 1)
(head {:name "Germany" :url "/countries/1-germany"} :page 1)

(post "/countries" {:name "Germany"})
(post "/countries" {:name "Germany"} :page 1)
(post {:name "Germany" :url "/countries/1-germany"} :page 1)

(put "/countries/1-germany" {:name "Germany"})
(put "/countries/1-germany" {:name "Germany"} :page 1)
(put {:name "Germany" :url "/countries/1-germany"} :page 1)

(options "/countries/1-germany" {:name "Germany"})
(options "/countries/1-germany" {:name "Germany"} :page 1)
(options {:name "Germany" :url "/countries/1-germany"} :page 1)

(trace "/countries/1-germany" {:name "Germany"})
(trace "/countries/1-germany" {:name "Germany"} :page 1)
(trace {:name "Germany" :url "/countries/1-germany"} :page 1)
