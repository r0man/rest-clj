(ns rest.test.core
  (:require-macros [cemerick.cljs.test :refer [is deftest]]
                   [rest.core :refer [defresources with-server]]
                   [routes.core :refer [defroute]])
  (:require [cemerick.cljs.test :as t]
            [cljs-http.client :refer [on-success on-error]]
            [rest.client :refer [*client* send-request]]
            [rest.http :as http]
            [routes.helper :refer [register-route]]
            [routes.params :as params]))

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

(deftest test-continents-url
  (is (= "http://api.burningswell.dev/continents" (continents-url))))

(deftest test-continent-url
  (is (= "http://api.burningswell.dev/continents/1-europe" (continent-url europe))))

(deftest test-new-continent-url
  (is (= "http://api.burningswell.dev/continents/new" (new-continent-url))))

(deftest test-edit-continent-url
  (is (= "http://api.burningswell.dev/continents/1-europe/edit" (edit-continent-url europe))))

(deftest test-continent
  (binding
      [*client*
       (fn [request]
         (is (= :get (:request-method request)))
         (is (= :http (:scheme request)))
         (is (= "api.burningswell.dev" (:server-name request)))
         (is (= 80 (:server-port request)))
         (is (= (continent-path europe) (:uri request)))
         (is (= {} (:body request))))]
    (continent europe)))

(deftest test-continents
  (binding
      [*client*
       (fn [request]
         (is (= :get (:request-method request)))
         (is (= :http (:scheme request)))
         (is (= "api.burningswell.dev" (:server-name request)))
         (is (= 80 (:server-port request)))
         (is (= (continents-path) (:uri request)))
         (is (= {} (:body request)))
         (is (= {:page 1 :per-page 20} (:query-params request))))]
    (continents {:page 1 :per-page 20})))

(deftest test-create-continent
  (binding
      [*client*
       (fn [request]
         (is (= :post (:request-method request)))
         (is (= :http (:scheme request)))
         (is (= "api.burningswell.dev" (:server-name request)))
         (is (= 80 (:server-port request)))
         (is (= (continents-path) (:uri request)))
         (is (= europe (:body request))))]
    (create-continent europe)))

(deftest test-update-continent
  (binding
      [*client*
       (fn [request]
         (is (= :put (:request-method request)))
         (is (= :http (:scheme request)))
         (is (= "api.burningswell.dev" (:server-name request)))
         (is (= 80 (:server-port request)))
         (is (= (continent-path europe) (:uri request)))
         (is (= europe (:body request))))]
    (update-continent europe)))

(deftest test-delete-continent
  (binding
      [*client*
       (fn [request]
         (is (= :delete (:request-method request)))
         (is (= :http (:scheme request)))
         (is (= "api.burningswell.dev" (:server-name request)))
         (is (= 80 (:server-port request)))
         (is (= (continent-path europe) (:uri request)))
         (is (= {} (:body request))))]
    (delete-continent europe)))

;; (deftest test-new-continent?
;;   (binding [*client*
;;             (fn [request]
;;               (is (= :head (:request-method request)))
;;               (is (= :http (:scheme request)))
;;               (is (= "api.burningswell.dev" (:server-name request)))
;;               (is (= 80 (:server-port request)))
;;               (is (= (continent-path europe) (:uri request)))
;;               (is (= {} (:body request))))]
;;     (new-continent? europe)))

;; COUNTRIES

(deftest test-countries-url
  (is (= "http://api.burningswell.dev/countries" (countries-url))))

(deftest test-country-url
  (is (= "http://api.burningswell.dev/countries/de-germany" (country-url germany))))

(deftest test-new-country-url
  (is (= "http://api.burningswell.dev/countries/new" (new-country-url))))

(deftest test-edit-country-url
  (is (= "http://api.burningswell.dev/countries/de-germany/edit" (edit-country-url germany))))

(deftest test-country
  (binding
      [*client*
       (fn [request]
         (is (= :get (:request-method request)))
         (is (= :http (:scheme request)))
         (is (= "api.burningswell.dev" (:server-name request)))
         (is (= 80 (:server-port request)))
         (is (= (country-path germany) (:uri request)))
         (is (= {} (:body request))))]
    (country germany)))

(deftest test-countries
  (binding
      [*client*
       (fn [request]
         (is (= :get (:request-method request)))
         (is (= :http (:scheme request)))
         (is (= "api.burningswell.dev" (:server-name request)))
         (is (= 80 (:server-port request)))
         (is (= (countries-path) (:uri request)))
         (is (= {} (:body request)))
         (is (= {:page 1} (:query-params request))))]
    (countries {:page 1})))

(deftest test-create-country
  (binding
      [*client*
       (fn [request]
         (is (= :post (:request-method request)))
         (is (= :http (:scheme request)))
         (is (= "api.burningswell.dev" (:server-name request)))
         (is (= 80 (:server-port request)))
         (is (= (countries-path) (:uri request)))
         (is (= germany (:body request))))]
    (create-country germany)))

(deftest test-update-country
  (binding
      [*client*
       (fn [request]
         (is (= :put (:request-method request)))
         (is (= :http (:scheme request)))
         (is (= "api.burningswell.dev" (:server-name request)))
         (is (= 80 (:server-port request)))
         (is (= (country-path germany) (:uri request)))
         (is (= germany (:body request))))]
    (update-country germany)))

(deftest test-delete-country
  (binding
      [*client*
       (fn [request]
         (is (= :delete (:request-method request)))
         (is (= :http (:scheme request)))
         (is (= "api.burningswell.dev" (:server-name request)))
         (is (= 80 (:server-port request)))
         (is (= (country-path germany) (:uri request)))
         (is (= {} (:body request))))]
    (delete-country germany)))

;; (deftest test-new-country?
;;   (binding
;;       [*client*
;;        (fn [request]
;;          (is (= :head (:request-method request)))
;;          (is (= :http (:scheme request)))
;;          (is (= "api.burningswell.dev" (:server-name request)))
;;          (is (= 80 (:server-port request)))
;;          (is (= (country-path germany) (:uri request)))
;;          (is (= {} (:body request))))]
;;     (new-country? germany)))

;; COUNTRIES IN CONTINENT

(deftest test-countries-of-continent-url
  (is (= "http://api.burningswell.dev/continents/1-europe/countries"
         (countries-of-continent-url europe))))

(deftest test-country-of-continent-url
  (is (= "http://api.burningswell.dev/continents/1-europe/countries/de-germany"
         (country-of-continent-url europe germany))))

(deftest test-new-country-of-continent-url
  (is (= "http://api.burningswell.dev/continents/1-europe/countries/new"
         (new-country-of-continent-url europe))))

(deftest test-edit-country-of-continent-url
  (is (= "http://api.burningswell.dev/continents/1-europe/countries/de-germany/edit"
         (edit-country-of-continent-url europe germany))))

(deftest test-countries-of-continent
  (binding
      [*client*
       (fn [request]
         (is (= :get (:request-method request)))
         (is (= :http (:scheme request)))
         (is (= "api.burningswell.dev" (:server-name request)))
         (is (= 80 (:server-port request)))
         (is (= (countries-of-continent-path europe) (:uri request)))
         (is (= {} (:body request)))
         (is (= {:page 1} (:query-params request))))]
    (countries-of-continent europe {:page 1})))

(deftest test-country-of-continent
  (binding
      [*client*
       (fn [request]
         (is (= :get (:request-method request)))
         (is (= :http (:scheme request)))
         (is (= "api.burningswell.dev" (:server-name request)))
         (is (= 80 (:server-port request)))
         (is (= (country-of-continent-path europe germany) (:uri request)))
         (is (= {} (:body request))))]
    (country-of-continent europe germany)))

(deftest test-create-country-of-continent
  (binding
      [*client*
       (fn [request]
         (is (= :post (:request-method request)))
         (is (= :http (:scheme request)))
         (is (= "api.burningswell.dev" (:server-name request)))
         (is (= 80 (:server-port request)))
         (is (= (countries-of-continent-path europe) (:uri request)))
         (is (= germany (:body request))))]
    (create-country-of-continent europe germany)))

(deftest test-update-country-of-continent
  (binding
      [*client*
       (fn [request]
         (is (= :put (:request-method request)))
         (is (= :http (:scheme request)))
         (is (= "api.burningswell.dev" (:server-name request)))
         (is (= 80 (:server-port request)))
         (is (= (country-of-continent-path europe germany) (:uri request)))
         (is (= germany (:body request))))]
    (update-country-of-continent europe germany)))

(deftest test-delete-country-of-continent
  (binding
      [*client*
       (fn [request]
         (is (= :delete (:request-method request)))
         (is (= :http (:scheme request)))
         (is (= "api.burningswell.dev" (:server-name request)))
         (is (= 80 (:server-port request)))
         (is (= (country-of-continent-path europe germany) (:uri request)))
         (is (= {} (:body request))))]
    (delete-country-of-continent europe germany)))

;; (deftest test-new-country-of-continent?
;;   (binding
;;       [*client*
;;        (fn [request]
;;          (is (= :head (:request-method request)))
;;          (is (= :http (:scheme request)))
;;          (is (= "api.burningswell.dev" (:server-name request)))
;;          (is (= 80 (:server-port request)))
;;          (is (= (country-of-continent-path europe germany) (:uri request)))
;;          (is (= {} (:body request))))]
;;     (new-country-of-continent? europe germany)))
