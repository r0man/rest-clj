(ns rest.test.core
  (:require [rest.client :refer [*client* send-request]]
            [routes.server :refer [example *server*]]
            [routes.helper :refer [register-route]])
  (:use-macros [rest.core :only [defresources with-server]]
               [routes.core :only [defroute]]))

(def europe {:iso-3166-1-alpha-2 "eu" :name "Europe"})
(def germany {:iso-3166-1-alpha-2 "de" :name "Germany"})

(with-server "https://example.com"

  (defresources continents [country]
    "/continents/:iso-3166-1-alpha-2-:name")

  (defresources countries [country]
    "/countries/:iso-3166-1-alpha-2-:name")

  (defresources countries-in-continent [continent country]
    "/continents/:iso-3166-1-alpha-2-:name/countries/:iso-3166-1-alpha-2-:name"))

;; CONTINENTS

(defn test-continents-path []
  (assert (= "/continents" (continents-path))))

(defn test-continent-path []
  (assert (= "/continents/eu-europe" (continent-path europe))))

(defn test-new-continent-path []
  (assert (= "/continents/new" (new-continent-path))))

(defn test-edit-continent-path []
  (assert (= "/continents/eu-europe/edit" (edit-continent-path europe))))

(defn test-continent []
  (binding [*client*
            (fn [request]
              (assert (= :get (:request-method request)))
              (assert (= :https (:scheme request)))
              (assert (= "example.com" (:server-name request)))
              (assert (= (continent-path europe) (:uri request)))
              (assert (= {} (:body request))))]
    (continent europe)))

(defn test-continents []
  (binding [*client*
            (fn [request]
              (assert (= :get (:request-method request)))
              (assert (= :https (:scheme request)))
              (assert (= "example.com" (:server-name request)))
              (assert (= (continents-path) (:uri request)))
              (assert (= {} (:body request)))
              (assert (= {:page 1} (:query-params request))))]
    (continents {:query-params {:page 1}})))

(defn test-create-continent []
  (binding [*client*
            (fn [request]
              (assert (= :post (:request-method request)))
              (assert (= :https (:scheme request)))
              (assert (= "example.com" (:server-name request)))
              (assert (= (continents-path) (:uri request)))
              (assert (= europe (:body request))))]
    (create-continent europe)))

(defn test-update-continent []
  (binding [*client*
            (fn [request]
              (assert (= :put (:request-method request)))
              (assert (= :https (:scheme request)))
              (assert (= "example.com" (:server-name request)))
              (assert (= (continent-path europe) (:uri request)))
              (assert (= europe (:body request))))]
    (update-continent europe)))

(defn test-delete-continent []
  (binding [*client*
            (fn [request]
              (assert (= :delete (:request-method request)))
              (assert (= :https (:scheme request)))
              (assert (= "example.com" (:server-name request)))
              (assert (= (continent-path europe) (:uri request))))]
    (delete-continent europe)))

;; COUNTRIES

(defn test-countries-path []
  (assert (= "/countries" (countries-path))))

(defn test-country-path []
  (assert (= "/countries/de-germany" (country-path germany))))

(defn test-new-country-path []
  (assert (= "/countries/new" (new-country-path))))

(defn test-edit-country-path []
  (assert (= "/countries/de-germany/edit" (edit-country-path germany))))

(defn test-country []
  (binding [*client*
            (fn [request]
              (assert (= :get (:request-method request)))
              (assert (= :https (:scheme request)))
              (assert (= "example.com" (:server-name request)))
              (assert (= (country-path germany) (:uri request)))
              (assert (= {} (:body request))))]
    (country germany)))

(defn test-countries []
  (binding [*client*
            (fn [request]
              (assert (= :get (:request-method request)))
              (assert (= :https (:scheme request)))
              (assert (= "example.com" (:server-name request)))
              (assert (= (countries-path) (:uri request)))
              (assert (= {} (:body request))))]
    (countries)))

(defn test-create-country []
  (binding [*client*
            (fn [request]
              (assert (= :post (:request-method request)))
              (assert (= :https (:scheme request)))
              (assert (= "example.com" (:server-name request)))
              (assert (= (countries-path) (:uri request)))
              (assert (= germany (:body request))))]
    (create-country germany)))

(defn test-update-country []
  (binding [*client*
            (fn [request]
              (assert (= :put (:request-method request)))
              (assert (= :https (:scheme request)))
              (assert (= "example.com" (:server-name request)))
              (assert (= (country-path germany) (:uri request)))
              (assert (= germany (:body request))))]
    (update-country germany)))

(defn test-delete-country []
  (binding [*client*
            (fn [request]
              (assert (= :delete (:request-method request)))
              (assert (= :https (:scheme request)))
              (assert (= "example.com" (:server-name request)))
              (assert (= (country-path germany) (:uri request))))]
    (delete-country germany)))

;; COUNTRIES IN CONTINENT

(defn test-countries-in-continent-path []
  (assert (= "/continents/eu-europe/countries" (countries-in-continent-path europe))))

(defn test-country-in-continent-path []
  (assert (= "/continents/eu-europe/countries/de-germany" (country-in-continent-path europe germany))))

(defn test-countries-in-continent []
  (binding [*client*
            (fn [request]
              (assert (= :get (:request-method request)))
              (assert (= :https (:scheme request)))
              (assert (= "example.com" (:server-name request)))
              (assert (= (countries-in-continent-path europe) (:uri request))))]
    (countries-in-continent europe)))

(defn test-country-in-continent []
  (binding [*client*
            (fn [request]
              (assert (= :get (:request-method request)))
              (assert (= :https (:scheme request)))
              (assert (= "example.com" (:server-name request)))
              (assert (= (country-in-continent-path europe germany) (:uri request))))]
    (country-in-continent europe germany)))

(defn test-create-country-in-continent []
  (binding [*client*
            (fn [request]
              (assert (= :post (:request-method request)))
              (assert (= :https (:scheme request)))
              (assert (= "example.com" (:server-name request)))
              (assert (= (countries-in-continent-path europe) (:uri request)))
              (assert (= germany (:body request))))]
    (create-country-in-continent europe germany)))

(defn test-update-country-in-continent []
  (binding [*client*
            (fn [request]
              (assert (= :put (:request-method request)))
              (assert (= :https (:scheme request)))
              (assert (= "example.com" (:server-name request)))
              (assert (= (country-in-continent-path europe germany) (:uri request)))
              (assert (= germany (:body request))))]
    (update-country-in-continent europe germany)))

(defn test-delete-country-in-continent []
  (binding [*client*
            (fn [request]
              (assert (= :delete (:request-method request)))
              (assert (= :https (:scheme request)))
              (assert (= "example.com" (:server-name request)))
              (assert (= (country-in-continent-path europe germany) (:uri request))))]
    (delete-country-in-continent europe germany)))

(defn test []
  (test-continents-path)
  (test-continent-path)
  (test-new-continent-path)
  (test-edit-continent-path)
  (test-continent)
  (test-continents)
  (test-create-continent)
  (test-update-continent)
  (test-delete-continent)
  (test-countries-path)
  (test-country-path)
  (test-new-country-path)
  (test-edit-country-path)
  (test-country)
  (test-countries)
  (test-create-country)
  (test-update-country)
  (test-delete-country)
  (test-countries-in-continent-path)
  (test-country-in-continent-path)
  (test-countries-in-continent)
  (test-country-in-continent)
  (test-create-country-in-continent)
  (test-update-country-in-continent)
  (test-delete-country-in-continent))
