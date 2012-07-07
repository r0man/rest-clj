(ns rest.test.macros
  (:require [rest.client :refer [send-request]]
            [routes.server :refer [*server*]]
            [routes.helper :refer [register-route]])
  (:use-macros [rest.macros :only [defresources with-server]]
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

(defn test-defresources-continents []
  (assert (= "/continents" (continents-path)))
  (assert (= "/continents/eu-europe" (continent-path europe)))
  (assert (= "/continents/new" (new-continent-path)))
  (assert (= "/continents/eu-europe/edit" (edit-continent-path europe)))
  (binding [send-request
            (fn [url & [request]]
              (assert (= (continent-url europe) url))
              (assert (nil? request)))]
    (continent europe))
  (binding [send-request
            (fn [url & [request]]
              (assert (= "https://example.com/continents" url))
              (assert (nil? request)))]
    (continents))
  (binding [send-request
            (fn [url & [request]]
              (assert (= :post (:method request)))
              (assert (= "https://example.com/continents" url))
              (assert (= europe (:body request))))]
    (create-continent europe))
  (binding [send-request
            (fn [url & [request]]
              (assert (= :put (:method request)))
              (assert (= (continent-url europe) url))
              (assert (= europe (:body request))))]
    (update-continent europe))
  (binding [send-request
            (fn [url & [request]]
              (assert (= :delete (:method request)))
              (assert (= (continent-url europe) url)))]
    (delete-continent europe)))

(defn test-defresources-countries []
  (assert (= "/countries" (countries-path)))
  (assert (= "/countries/de-germany" (country-path germany)))
  (assert (= "/countries/new" (new-country-path)))
  (assert (= "/countries/de-germany/edit" (edit-country-path germany)))
  (binding [send-request
            (fn [url & [request]]
              (assert (= (country-url germany) url))
              (assert (nil? request)))]
    (country germany))
  (binding [send-request
            (fn [url & [request]]
              (assert (= "https://example.com/countries" url))
              (assert (nil? request)))]
    (countries))
  (binding [send-request
            (fn [url & [request]]
              (assert (= :post (:method request)))
              (assert (= "https://example.com/countries" url))
              (assert (= germany (:body request))))]
    (create-country germany))
  (binding [send-request
            (fn [url & [request]]
              (assert (= :put (:method request)))
              (assert (= (country-url germany) url))
              (assert (= germany (:body request))))]
    (update-country germany))
  (binding [send-request
            (fn [url & [request]]
              (assert (= :delete (:method request)))
              (assert (= (country-url germany) url)))]
    (delete-country germany)))

(defn test-defresources-countries-in-continent []
  (assert (= "/continents/eu-europe/countries" (countries-in-continent-path europe)))
  (assert (= "/continents/eu-europe/countries/de-germany" (country-in-continent-path europe germany)))
  (binding [send-request
            (fn [url & [request]]
              (assert (= (countries-in-continent-url europe) url)))]
    (countries-in-continent europe))
  (binding [send-request
            (fn [url & [request]]
              (assert (= (country-in-continent-url europe germany) url)))]
    (country-in-continent europe germany)))

(defn test []
  (test-defresources-continents)
  (test-defresources-countries)
  (test-defresources-countries-in-continent))