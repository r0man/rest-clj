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

;; CONTINENTS

(deftest test-continents-path
  (is (= "/continents" (continents-path))))

(deftest test-continent-path
  (is (= "/continents/eu-europe" (continent-path europe))))

(deftest test-new-continent-path
  (is (= "/continents/new" (new-continent-path))))

(deftest test-edit-continent-path
  (is (= "/continents/eu-europe/edit" (edit-continent-path europe))))

(deftest test-continent
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= (continent-url europe) url))
       (is (nil? request)))]
    (continent europe)))

(deftest test-continents
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= "https://example.com/continents" url))
       (is (nil? request)))]
    (continents)))

(deftest test-create-continent
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= :post (:method request)))
       (is (= "https://example.com/continents" url))
       (is (= europe (:body request))))]
    (create-continent europe)))

(deftest test-update-continent
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= :put (:method request)))
       (is (= (continent-url europe) url))
       (is (= europe (:body request))))]
    (update-continent europe)))

(deftest test-delete-continent
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= :delete (:method request)))
       (is (= (continent-url europe) url)))]
    (delete-continent europe)))

;; COUNTRIES

(deftest test-countries-path
  (is (= "/countries" (countries-path))))

(deftest test-country-path
  (is (= "/countries/de-germany" (country-path germany))))

(deftest test-new-country-path
  (is (= "/countries/new" (new-country-path))))

(deftest test-edit-country-path
  (is (= "/countries/de-germany/edit" (edit-country-path germany))))

(deftest test-country
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= (country-url germany) url))
       (is (nil? request)))]
    (country germany)))

(deftest test-countries
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= "https://example.com/countries" url))
       (is (nil? request)))]
    (countries)))

(deftest test-create-country
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= :post (:method request)))
       (is (= "https://example.com/countries" url))
       (is (= germany (:body request))))]
    (create-country germany)))

(deftest test-update-country
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= :put (:method request)))
       (is (= (country-url germany) url))
       (is (= germany (:body request))))]
    (update-country germany)))

(deftest test-delete-country
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= :delete (:method request)))
       (is (= (country-url germany) url)))]
    (delete-country germany)))

;; COUNTRIES IN CONTINENT

(deftest test-countries-in-continent-path
  (is (= "/continents/eu-europe/countries" (countries-in-continent-path europe))))

(deftest test-country-in-continent-path
  (is (= "/continents/eu-europe/countries/de-germany" (country-in-continent-path europe germany))))

(deftest test-countries-in-continent
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= (countries-in-continent-url europe) url)))]
    (countries-in-continent europe)))

(deftest test-country-in-continent
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= (country-in-continent-url europe germany) url)))]
    (country-in-continent europe germany)))

(deftest test-create-country-in-continent
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= :post (:method request)))
       (is (= (countries-in-continent-url europe) url))
       (is (= germany (:body request))))]
    (create-country-in-continent europe germany)))

(deftest test-update-country-in-continent
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= :put (:method request)))
       (is (= (country-in-continent-url europe germany) url))
       (is (= germany (:body request))))]
    (update-country-in-continent europe germany)))

(deftest test-delete-country-in-continent
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= :delete (:method request)))
       (is (= (country-in-continent-url europe germany) url)))]
    (delete-country-in-continent europe germany)))
