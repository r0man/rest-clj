(ns rest.test.macros
  (:require [rest.client :refer [send-request]]
            [routes.server :refer [*server*]]
            [routes.helper :refer [register-route]])
  (:use-macros [rest.macros :only [defresources with-server]]
               [routes.core :only [defroute]]))

(def germany {:iso-3166-1-alpha-2 "de" :name "Germany"})

(defn test-defresources []
  (with-server "https://example.com"
    (defresources countries [country]
      "/countries/:iso-3166-1-alpha-2-:name"))
  (assert (= "/countries" (countries-path)))
  (assert (= "/countries/de-germany" (country-path germany)))
  (assert (= "/countries/new" (new-country-path)))
  (assert (= "/countries/de-germany/edit" (edit-country-path germany)))
  (binding [send-request
            (fn [url & [request]]
              (assert (= "https://example.com/countries/de-germany" url))
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
              (assert (= "https://example.com/countries/de-germany" url))
              (assert (= germany (:body request))))]
    (update-country germany))
  (binding [send-request
            (fn [url & [request]]
              (assert (= :delete (:method request)))
              (assert (= "https://example.com/countries/de-germany" url)))]
    (delete-country germany)))

(defn test []
  (test-defresources))