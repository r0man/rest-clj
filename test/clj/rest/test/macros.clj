(ns rest.test.macros
  (:use clojure.test
        rest.client
        rest.macros))

(def europe {:iso-3166-1-alpha-2 "eu" :name "Europe"})
(def germany {:iso-3166-1-alpha-2 "de" :name "Germany"})

(with-server "https://example.com"

  (defresources continents [country]
    "/continents/:iso-3166-1-alpha-2-:name")

  (defresources countries [country]
    "/countries/:iso-3166-1-alpha-2-:name")

  (defresources countries-in-continent [continent country]
    "/continents/:iso-3166-1-alpha-2-:name/countries/:iso-3166-1-alpha-2-:name"))

(deftest test-defresources-continents
  (is (= "/continents" (continents-path)))
  (is (= "/continents/eu-europe" (continent-path europe)))
  (is (= "/continents/new" (new-continent-path)))
  (is (= "/continents/eu-europe/edit" (edit-continent-path europe)))
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= (continent-url europe) url))
       (is (nil? request)))]
    (continent europe))
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= "https://example.com/continents" url))
       (is (nil? request)))]
    (continents))
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= :post (:method request)))
       (is (= "https://example.com/continents" url))
       (is (= europe (:body request))))]
    (create-continent europe))
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= :put (:method request)))
       (is (= (continent-url europe) url))
       (is (= europe (:body request))))]
    (update-continent europe))
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= :delete (:method request)))
       (is (= (continent-url europe) url)))]
    (delete-continent europe)))

(deftest test-defresources-countries
  (is (= "/countries" (countries-path)))
  (is (= "/countries/de-germany" (country-path germany)))
  (is (= "/countries/new" (new-country-path)))
  (is (= "/countries/de-germany/edit" (edit-country-path germany)))
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= (country-url germany) url))
       (is (nil? request)))]
    (country germany))
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= "https://example.com/countries" url))
       (is (nil? request)))]
    (countries))
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= :post (:method request)))
       (is (= "https://example.com/countries" url))
       (is (= germany (:body request))))]
    (create-country germany))
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= :put (:method request)))
       (is (= (country-url germany) url))
       (is (= germany (:body request))))]
    (update-country germany))
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= :delete (:method request)))
       (is (= (country-url germany) url)))]
    (delete-country germany)))

(deftest test-defresources-countries-in-continent
  (is (= "/continents/eu-europe/countries" (countries-in-continent-path europe)))
  (is (= "/continents/eu-europe/countries/de-germany" (country-in-continent-path europe germany)))
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= (countries-in-continent-url europe) url)))]
    (countries-in-continent europe))
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= (country-in-continent-url europe germany) url)))]
    (country-in-continent europe germany)))
