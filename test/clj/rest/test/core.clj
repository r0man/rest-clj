(ns rest.test.core
  (:require [routes.params :as params])
  (:use clojure.test
        rest.client
        rest.core
        routes.core))

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
  (with-redefs
    [*client*
     (fn [request]
       (is (= :get (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "api.burningswell.dev" (:server-name request)))
       (is (= 80 (:server-port request)))
       (is (= (continent-path europe) (:uri request)))
       (is (nil? (:body request))))]
    (continent europe)))

(deftest test-continents
  (with-redefs
    [*client*
     (fn [request]
       (is (= :get (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "api.burningswell.dev" (:server-name request)))
       (is (= 80 (:server-port request)))
       (is (= (continents-path) (:uri request)))
       (is (nil? (:body request)))
       (is (= {:page 1} (:query-params request))))]
    (continents {:query-params {:page 1}})))

(deftest test-create-continent
  (with-redefs
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
  (with-redefs
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
  (with-redefs
    [*client*
     (fn [request]
       (is (= :delete (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "api.burningswell.dev" (:server-name request)))
       (is (= 80 (:server-port request)))
       (is (= (continent-path europe) (:uri request)))
       (is (nil? (:body request))))]
    (delete-continent europe)))

(deftest test-new-continent?
  (with-redefs
    [*client*
     (fn [request]
       (is (= :head (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "api.burningswell.dev" (:server-name request)))
       (is (= 80 (:server-port request)))
       (is (= (continent-path europe) (:uri request)))
       (is (nil? (:body request))))]
    (new-continent? europe)))

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
  (with-redefs
    [*client*
     (fn [request]
       (is (= :get (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "api.burningswell.dev" (:server-name request)))
       (is (= 80 (:server-port request)))
       (is (= (country-path germany) (:uri request)))
       (is (nil? (:body request))))]
    (country germany)))

(deftest test-countries
  (with-redefs
    [*client*
     (fn [request]
       (is (= :get (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "api.burningswell.dev" (:server-name request)))
       (is (= 80 (:server-port request)))
       (is (= (countries-path) (:uri request)))
       (is (nil? (:body request))))]
    (countries)))

(deftest test-create-country
  (with-redefs
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
  (with-redefs
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
  (with-redefs
    [*client*
     (fn [request]
       (is (= :delete (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "api.burningswell.dev" (:server-name request)))
       (is (= 80 (:server-port request)))
       (is (= (country-path germany) (:uri request)))
       (is (nil? (:body request))))]
    (delete-country germany)))

(deftest test-new-country?
  (with-redefs
    [*client*
     (fn [request]
       (is (= :head (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "api.burningswell.dev" (:server-name request)))
       (is (= 80 (:server-port request)))
       (is (= (country-path germany) (:uri request)))
       (is (nil? (:body request))))]
    (new-country? germany)))

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
  (with-redefs
    [*client*
     (fn [request]
       (is (= :get (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "api.burningswell.dev" (:server-name request)))
       (is (= 80 (:server-port request)))
       (is (= (countries-of-continent-path europe) (:uri request)))
       (is (nil? (:body request))))]
    (countries-of-continent europe)))

(deftest test-country-of-continent
  (with-redefs
    [*client*
     (fn [request]
       (is (= :get (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "api.burningswell.dev" (:server-name request)))
       (is (= 80 (:server-port request)))
       (is (= (country-of-continent-path europe germany) (:uri request)))
       (is (nil? (:body request))))]
    (country-of-continent europe germany)))

(deftest test-create-country-of-continent
  (with-redefs
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
  (with-redefs
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
  (with-redefs
    [*client*
     (fn [request]
       (is (= :delete (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "api.burningswell.dev" (:server-name request)))
       (is (= 80 (:server-port request)))
       (is (= (country-of-continent-path europe germany) (:uri request)))
       (is (nil? (:body request))))]
    (delete-country-of-continent europe germany)))

(deftest test-new-country-of-continent?
  (with-redefs
    [*client*
     (fn [request]
       (is (= :head (:request-method request)))
       (is (= :http (:scheme request)))
       (is (= "api.burningswell.dev" (:server-name request)))
       (is (= 80 (:server-port request)))
       (is (= (country-of-continent-path europe germany) (:uri request)))
       (is (nil? (:body request))))]
    (new-country-of-continent? europe germany)))

;; ;; (comment

;; ;;   (with-server "http://api.api.burningswell.dev"
;; ;;     (continent (first (continents))))

;; ;;   (with-server "http://api.api.burningswell.dev"
;; ;;     (save-continent (first (continents))))

;; ;;   (with-server "http://api.api.burningswell.dev"
;; ;;     (create-continent {:name "TEST"
;; ;;                        :iso-3166-1-alpha-2 "xx"
;; ;;                        :iso-3166-1-alpha-3 "xxx"
;; ;;                        :freebase-guid "3243234234"
;; ;;                        :geonames-id 123123
;; ;;                        :location {:latitude 0 :longitude 0}}))

;; ;;   (with-server "http://api.api.burningswell.dev"
;; ;;     (save-continent {:name "TEST"
;; ;;                      :iso-3166-1-alpha-2 "xx"
;; ;;                      :iso-3166-1-alpha-3 "xxx"
;; ;;                      :freebase-guid "3243234234"
;; ;;                      :geonames-id 123123
;; ;;                      :location {:latitude 0 :longitude 0}}))

;; ;;   (with-server "http://api.api.burningswell.dev"
;; ;;     (new-continent? {:name "TEST"
;; ;;                      :iso-3166-1-alpha-2 "xx"
;; ;;                      :iso-3166-1-alpha-3 "xxx"
;; ;;                      :freebase-guid "3243234234"
;; ;;                      :geonames-id 123123
;; ;;                      :location {:latitude 0 :longitude 0}}))

;; ;;   (with-server "http://api.api.burningswell.dev"
;; ;;     (delete-continent {:iso-3166-1-alpha-2 "xx"}))

;; ;;   )
