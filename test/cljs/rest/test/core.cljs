(ns rest.test.core
  (:require [cljs-http.client :refer [on-success on-error]]
            [rest.client :refer [*client* send-request]]
            [rest.http :as http]
            [routes.helper :refer [register-route]]
            [routes.params :as params])
  (:use-macros [rest.core :only [defresources with-server]]
               [routes.core :only [defroute]]))

(def europe {:id 1 :name "Europe"})
(def germany {:iso-3166-1-alpha-2 "de" :name "Germany"})

(def german {:iso-639-1 "de" :name "German"})

(def ^:dynamic *server*
  {:scheme :http :server-name "api.burningswell.dev" :server-port 80})

(defroute root []
  ["/"]
  :server *server*)

(defresources continents [continent]
  ["/continents/:id-:name" params/integer params/string]
  :root root-route)

(defresources countries [country]
  ["/countries/:iso-3166-1-alpha-2-:name" params/iso-3166-1-alpha-2 params/string]
  :root root-route)

(defresources countries-of-continent [country]
  ["/countries/:iso-3166-1-alpha-2-:name" params/iso-3166-1-alpha-2 params/string]
  :root continent-route)

;; CONTINENTS

(defn test-continents-url []
  (assert (= "http://api.burningswell.dev/continents" (continents-url))))

(defn test-continent-url []
  (assert (= "http://api.burningswell.dev/continents/1-europe" (continent-url europe))))

(defn test-new-continent-url []
  (assert (= "http://api.burningswell.dev/continents/new" (new-continent-url))))

(defn test-edit-continent-url []
  (assert (= "http://api.burningswell.dev/continents/1-europe/edit" (edit-continent-url europe))))

(defn test-continent []
  (binding
      [*client*
       (fn [request]
         (assert (= :get (:request-method request)))
         (assert (= :http (:scheme request)))
         (assert (= "api.burningswell.dev" (:server-name request)))
         (assert (= 80 (:server-port request)))
         (assert (= (continent-path europe) (:uri request)))
         (assert (= {} (:body request))))]
    (continent europe)))

(defn test-continents []
  (binding
      [*client*
       (fn [request]
         (assert (= :get (:request-method request)))
         (assert (= :http (:scheme request)))
         (assert (= "api.burningswell.dev" (:server-name request)))
         (assert (= 80 (:server-port request)))
         (assert (= (continents-path) (:uri request)))
         (assert (= {} (:body request)))
         (assert (= {:page 1 :per-page 20} (:query-params request))))]
    (continents {:page 1 :per-page 20})))

(defn test-create-continent []
  (binding
      [*client*
       (fn [request]
         (assert (= :post (:request-method request)))
         (assert (= :http (:scheme request)))
         (assert (= "api.burningswell.dev" (:server-name request)))
         (assert (= 80 (:server-port request)))
         (assert (= (continents-path) (:uri request)))
         (assert (= europe (:body request))))]
    (create-continent europe)))

(defn test-update-continent []
  (binding
      [*client*
       (fn [request]
         (assert (= :put (:request-method request)))
         (assert (= :http (:scheme request)))
         (assert (= "api.burningswell.dev" (:server-name request)))
         (assert (= 80 (:server-port request)))
         (assert (= (continent-path europe) (:uri request)))
         (assert (= europe (:body request))))]
    (update-continent europe)))

(defn test-delete-continent []
  (binding
      [*client*
       (fn [request]
         (assert (= :delete (:request-method request)))
         (assert (= :http (:scheme request)))
         (assert (= "api.burningswell.dev" (:server-name request)))
         (assert (= 80 (:server-port request)))
         (assert (= (continent-path europe) (:uri request)))
         (assert (= {} (:body request))))]
    (delete-continent europe)))

(defn test-new-continent? []
  (binding
      [*client*
       (fn [request]
         (assert (= :head (:request-method request)))
         (assert (= :http (:scheme request)))
         (assert (= "api.burningswell.dev" (:server-name request)))
         (assert (= 80 (:server-port request)))
         (assert (= (continent-path europe) (:uri request)))
         (assert (= {} (:body request))))]
    (new-continent? europe)))

;; COUNTRIES

(defn test-countries-url []
  (assert (= "http://api.burningswell.dev/countries" (countries-url))))

(defn test-country-url []
  (assert (= "http://api.burningswell.dev/countries/de-germany" (country-url germany))))

(defn test-new-country-url []
  (assert (= "http://api.burningswell.dev/countries/new" (new-country-url))))

(defn test-edit-country-url []
  (assert (= "http://api.burningswell.dev/countries/de-germany/edit" (edit-country-url germany))))

(defn test-country []
  (binding
      [*client*
       (fn [request]
         (assert (= :get (:request-method request)))
         (assert (= :http (:scheme request)))
         (assert (= "api.burningswell.dev" (:server-name request)))
         (assert (= 80 (:server-port request)))
         (assert (= (country-path germany) (:uri request)))
         (assert (= {} (:body request))))]
    (country germany)))

(defn test-countries []
  (binding
      [*client*
       (fn [request]
         (assert (= :get (:request-method request)))
         (assert (= :http (:scheme request)))
         (assert (= "api.burningswell.dev" (:server-name request)))
         (assert (= 80 (:server-port request)))
         (assert (= (countries-path) (:uri request)))
         (assert (= {} (:body request)))
         (assert (= {:page 1} (:query-params request))))]
    (countries {:page 1})))

(defn test-create-country []
  (binding
      [*client*
       (fn [request]
         (assert (= :post (:request-method request)))
         (assert (= :http (:scheme request)))
         (assert (= "api.burningswell.dev" (:server-name request)))
         (assert (= 80 (:server-port request)))
         (assert (= (countries-path) (:uri request)))
         (assert (= germany (:body request))))]
    (create-country germany)))

(defn test-update-country []
  (binding
      [*client*
       (fn [request]
         (assert (= :put (:request-method request)))
         (assert (= :http (:scheme request)))
         (assert (= "api.burningswell.dev" (:server-name request)))
         (assert (= 80 (:server-port request)))
         (assert (= (country-path germany) (:uri request)))
         (assert (= germany (:body request))))]
    (update-country germany)))

(defn test-delete-country []
  (binding
      [*client*
       (fn [request]
         (assert (= :delete (:request-method request)))
         (assert (= :http (:scheme request)))
         (assert (= "api.burningswell.dev" (:server-name request)))
         (assert (= 80 (:server-port request)))
         (assert (= (country-path germany) (:uri request)))
         (assert (= {} (:body request))))]
    (delete-country germany)))

(defn test-new-country? []
  (binding
      [*client*
       (fn [request]
         (assert (= :head (:request-method request)))
         (assert (= :http (:scheme request)))
         (assert (= "api.burningswell.dev" (:server-name request)))
         (assert (= 80 (:server-port request)))
         (assert (= (country-path germany) (:uri request)))
         (assert (= {} (:body request))))]
    (new-country? germany)))

;; COUNTRIES IN CONTINENT

(defn test-countries-of-continent-url []
  (assert (= "http://api.burningswell.dev/continents/1-europe/countries"
             (countries-of-continent-url europe))))

(defn test-country-of-continent-url []
  (assert (= "http://api.burningswell.dev/continents/1-europe/countries/de-germany"
             (country-of-continent-url europe germany))))

(defn test-new-country-of-continent-url []
  (assert (= "http://api.burningswell.dev/continents/1-europe/countries/new"
             (new-country-of-continent-url europe))))

(defn test-edit-country-of-continent-url []
  (assert (= "http://api.burningswell.dev/continents/1-europe/countries/de-germany/edit"
             (edit-country-of-continent-url europe germany))))

(defn test-countries-of-continent []
  (binding
      [*client*
       (fn [request]
         (assert (= :get (:request-method request)))
         (assert (= :http (:scheme request)))
         (assert (= "api.burningswell.dev" (:server-name request)))
         (assert (= 80 (:server-port request)))
         (assert (= (countries-of-continent-path europe) (:uri request)))
         (assert (= {} (:body request)))
         (assert (= {:page 1} (:query-params request))))]
    (countries-of-continent europe {:page 1})))

(defn test-country-of-continent []
  (binding
      [*client*
       (fn [request]
         (assert (= :get (:request-method request)))
         (assert (= :http (:scheme request)))
         (assert (= "api.burningswell.dev" (:server-name request)))
         (assert (= 80 (:server-port request)))
         (assert (= (country-of-continent-path europe germany) (:uri request)))
         (assert (= {} (:body request))))]
    (country-of-continent europe germany)))

(defn test-create-country-of-continent []
  (binding
      [*client*
       (fn [request]
         (assert (= :post (:request-method request)))
         (assert (= :http (:scheme request)))
         (assert (= "api.burningswell.dev" (:server-name request)))
         (assert (= 80 (:server-port request)))
         (assert (= (countries-of-continent-path europe) (:uri request)))
         (assert (= germany (:body request))))]
    (create-country-of-continent europe germany)))

(defn test-update-country-of-continent []
  (binding
      [*client*
       (fn [request]
         (assert (= :put (:request-method request)))
         (assert (= :http (:scheme request)))
         (assert (= "api.burningswell.dev" (:server-name request)))
         (assert (= 80 (:server-port request)))
         (assert (= (country-of-continent-path europe germany) (:uri request)))
         (assert (= germany (:body request))))]
    (update-country-of-continent europe germany)))

(defn test-delete-country-of-continent []
  (binding
      [*client*
       (fn [request]
         (assert (= :delete (:request-method request)))
         (assert (= :http (:scheme request)))
         (assert (= "api.burningswell.dev" (:server-name request)))
         (assert (= 80 (:server-port request)))
         (assert (= (country-of-continent-path europe germany) (:uri request)))
         (assert (= {} (:body request))))]
    (delete-country-of-continent europe germany)))

(defn test-new-country-of-continent? []
  (binding
      [*client*
       (fn [request]
         (assert (= :head (:request-method request)))
         (assert (= :http (:scheme request)))
         (assert (= "api.burningswell.dev" (:server-name request)))
         (assert (= 80 (:server-port request)))
         (assert (= (country-of-continent-path europe germany) (:uri request)))
         (assert (= {} (:body request))))]
    (new-country-of-continent? europe germany)))

(defn test []
  (test-continents-url)
  (test-continent-url)
  (test-new-continent-url)
  (test-edit-continent-url)
  (test-continent)
  (test-continents)
  (test-create-continent)
  (test-update-continent)
  (test-delete-continent)
  (test-new-continent?)
  (test-countries-url)
  (test-country-url)
  (test-new-country-url)
  (test-edit-country-url)
  (test-country)
  (test-countries)
  (test-create-country)
  (test-update-country)
  (test-delete-country)
  (test-new-country?)
  (test-countries-of-continent-url)
  (test-country-of-continent-url)
  (test-new-country-of-continent-url)
  (test-edit-country-of-continent-url)
  (test-countries-of-continent)
  (test-country-of-continent)
  (test-create-country-of-continent)
  (test-update-country-of-continent)
  (test-delete-country-of-continent)
  (test-new-country-of-continent?))
