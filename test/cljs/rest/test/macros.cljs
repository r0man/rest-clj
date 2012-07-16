(ns rest.test.macros
  (:require [rest.client :refer [send-request]]
            [routes.server :refer [example *server*]]
            [routes.helper :refer [register-route]])
  (:use-macros [rest.macros :only [defresources with-server]]
               [routes.core :only [defroute]]))

(def europe {:iso-3166-1-alpha-2 "eu" :name "Europe"})
(def germany {:iso-3166-1-alpha-2 "de" :name "Germany"})

(with-server example

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
  (binding [send-request
            (fn [url & [request]]
              (assert (= (continent-url europe) url))
              (assert (nil? request)))]
    (continent europe)))

(defn test-continents []
  (binding [send-request
            (fn [url & [request]]
              (assert (= "https://example.com/continents" url))
              (assert (nil? request)))]
    (continents)))

(defn test-create-continent []
  (binding [send-request
            (fn [url & [request]]
              (assert (= :post (:method request)))
              (assert (= "https://example.com/continents" url))
              (assert (= europe (:body request))))]
    (create-continent europe)))

(defn test-update-continent []
  (binding [send-request
            (fn [url & [request]]
              (assert (= :put (:method request)))
              (assert (= (continent-url europe) url))
              (assert (= europe (:body request))))]
    (update-continent europe)))

(defn test-delete-continent []
  (binding [send-request
            (fn [url & [request]]
              (assert (= :delete (:method request)))
              (assert (= (continent-url europe) url)))]
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
  (binding [send-request
            (fn [url & [request]]
              (assert (= (country-url germany) url))
              (assert (nil? request)))]
    (country germany)))

(defn test-countries []
  (binding [send-request
            (fn [url & [request]]
              (assert (= "https://example.com/countries" url))
              (assert (nil? request)))]
    (countries)))

(defn test-create-country []
  (binding [send-request
            (fn [url & [request]]
              (assert (= :post (:method request)))
              (assert (= "https://example.com/countries" url))
              (assert (= germany (:body request))))]
    (create-country germany)))

(defn test-update-country []
  (binding [send-request
            (fn [url & [request]]
              (assert (= :put (:method request)))
              (assert (= (country-url germany) url))
              (assert (= germany (:body request))))]
    (update-country germany)))

(defn test-delete-country []
  (binding [send-request
            (fn [url & [request]]
              (assert (= :delete (:method request)))
              (assert (= (country-url germany) url)))]
    (delete-country germany)))

;; COUNTRIES IN CONTINENT

(defn test-countries-in-continent-path []
  (assert (= "/continents/eu-europe/countries" (countries-in-continent-path europe))))

(defn test-country-in-continent-path []
  (assert (= "/continents/eu-europe/countries/de-germany" (country-in-continent-path europe germany))))

(defn test-countries-in-continent []
  (binding [send-request
            (fn [url & [request]]
              (assert (= (countries-in-continent-url europe) url)))]
    (countries-in-continent europe)))

(defn test-country-in-continent []
  (binding [send-request
            (fn [url & [request]]
              (assert (= (country-in-continent-url europe germany) url)))]
    (country-in-continent europe germany)))

(defn test-create-country-in-continent []
  (binding [send-request
            (fn [url & [request]]
              (assert (= :post (:method request)))
              (assert (= (countries-in-continent-url europe) url))
              (assert (= germany (:body request))))]
    (create-country-in-continent europe germany)))

(defn test-update-country-in-continent []
  (binding [send-request
            (fn [url & [request]]
              (assert (= :put (:method request)))
              (assert (= (country-in-continent-url europe germany) url))
              (assert (= germany (:body request))))]
    (update-country-in-continent europe germany)))

(defn test-delete-country-in-continent []
  (binding [send-request
            (fn [url & [request]]
              (assert (= :delete (:method request)))
              (assert (= (country-in-continent-url europe germany) url)))]
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