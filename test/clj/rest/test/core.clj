(ns rest.test.core
  (:use clojure.test
        rest.client
        rest.core
        routes.server))

(def europe {:iso-3166-1-alpha-2 "eu" :name "Europe"})
(def germany {:iso-3166-1-alpha-2 "de" :name "Germany"})

(def german {:iso-639-1 "de" :name "German"})

(with-server example

  (defresources continents [country]
    "/continents/[:iso-3166-1-alpha-2]-[:name]")

  (defresources countries [country]
    "/countries/[:iso-3166-1-alpha-2]-[:name]"
    :server "https://example.com")

  (defresources countries-in-continent [continent country]
    "/continents/[:iso-3166-1-alpha-2]-[:name]/countries/[:iso-3166-1-alpha-2]-[:name]"
    :server *server*))

(defresources languages [continent]
  "/languages/[:iso-639-1]-[:name]"
  :server {:scheme :https :server-name "api.other.com"})

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
    [*client*
     (fn [request]
       (is (= :get (:request-method request)))
       (is (= :https (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= 443 (:server-port request)))
       (is (= (continent-path europe) (:uri request)))
       (is (= {} (:body request))))]
    (continent europe)))

(deftest test-continents
  (with-redefs
    [*client*
     (fn [request]
       (is (= :get (:request-method request)))
       (is (= :https (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= 443 (:server-port request)))
       (is (= (continents-path) (:uri request)))
       (is (= {} (:body request)))
       (is (= {:page 1} (:query-params request))))]
    (continents {:query-params {:page 1}})))

(deftest test-create-continent
  (with-redefs
    [*client*
     (fn [request]
       (is (= :post (:request-method request)))
       (is (= :https (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= 443 (:server-port request)))
       (is (= (continents-path) (:uri request)))
       (is (= europe (:body request))))]
    (create-continent europe)))

(deftest test-update-continent
  (with-redefs
    [*client*
     (fn [request]
       (is (= :put (:request-method request)))
       (is (= :https (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= 443 (:server-port request)))
       (is (= (continent-path europe) (:uri request)))
       (is (= europe (:body request))))]
    (update-continent europe)))

(deftest test-delete-continent
  (with-redefs
    [*client*
     (fn [request]
       (is (= :delete (:request-method request)))
       (is (= :https (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= 443 (:server-port request)))
       (is (= (continent-path europe) (:uri request)))
       (is (= {} (:body request))))]
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
    [*client*
     (fn [request]
       (is (= :get (:request-method request)))
       (is (= :https (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= 443 (:server-port request)))
       (is (= (country-path germany) (:uri request)))
       (is (= {} (:body request))))]
    (country germany)))

(deftest test-countries
  (with-redefs
    [*client*
     (fn [request]
       (is (= :get (:request-method request)))
       (is (= :https (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= 443 (:server-port request)))
       (is (= (countries-path) (:uri request)))
       (is (= {} (:body request))))]
    (countries)))

(deftest test-create-country
  (with-redefs
    [*client*
     (fn [request]
       (is (= :post (:request-method request)))
       (is (= :https (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= 443 (:server-port request)))
       (is (= (countries-path) (:uri request)))
       (is (= germany (:body request))))]
    (create-country germany)))

(deftest test-update-country
  (with-redefs
    [*client*
     (fn [request]
       (is (= :put (:request-method request)))
       (is (= :https (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= 443 (:server-port request)))
       (is (= (country-path germany) (:uri request)))
       (is (= germany (:body request))))]
    (update-country germany)))

(deftest test-delete-country
  (with-redefs
    [*client*
     (fn [request]
       (is (= :delete (:request-method request)))
       (is (= :https (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= 443 (:server-port request)))
       (is (= (country-path germany) (:uri request)))
       (is (= {} (:body request))))]
    (delete-country germany)))

(deftest test-new-country?
  (with-redefs
    [*client*
     (fn [request]
       (is (= :head (:request-method request)))
       (is (= :https (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= 443 (:server-port request)))
       (is (= (country-path germany) (:uri request)))
       (is (= {} (:body request))))]
    (new-country? germany)))

;; COUNTRIES IN CONTINENT

(deftest test-countries-in-continent-path
  (is (= "/continents/eu-europe/countries"
         (countries-in-continent-path europe))))

(deftest test-country-in-continent-path
  (is (= "/continents/eu-europe/countries/de-germany"
         (country-in-continent-path europe germany))))

(deftest test-countries-in-continent
  (with-redefs
    [*client*
     (fn [request]
       (is (= :get (:request-method request)))
       (is (= :https (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= 443 (:server-port request)))
       (is (= (countries-in-continent-path europe) (:uri request)))
       (is (= {} (:body request))))]
    (countries-in-continent europe)))

(deftest test-country-in-continent
  (with-redefs
    [*client*
     (fn [request]
       (is (= :get (:request-method request)))
       (is (= :https (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= 443 (:server-port request)))
       (is (= (country-in-continent-path europe germany) (:uri request)))
       (is (= {} (:body request))))]
    (country-in-continent europe germany)))

(deftest test-create-country-in-continent
  (with-redefs
    [*client*
     (fn [request]
       (is (= :post (:request-method request)))
       (is (= :https (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= 443 (:server-port request)))
       (is (= (countries-in-continent-path europe) (:uri request)))
       (is (= germany (:body request))))]
    (create-country-in-continent europe germany)))

(deftest test-update-country-in-continent
  (with-redefs
    [*client*
     (fn [request]
       (is (= :put (:request-method request)))
       (is (= :https (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= 443 (:server-port request)))
       (is (= (country-in-continent-path europe germany) (:uri request)))
       (is (= germany (:body request))))]
    (update-country-in-continent europe germany)))

(deftest test-delete-country-in-continent
  (with-redefs
    [*client*
     (fn [request]
       (is (= :delete (:request-method request)))
       (is (= :https (:scheme request)))
       (is (= "example.com" (:server-name request)))
       (is (= 443 (:server-port request)))
       (is (= (country-in-continent-path europe germany) (:uri request)))
       (is (= {} (:body request))))]
    (delete-country-in-continent europe germany)))

;; LANGUAGES

(deftest test-languages-path
  (is (= "/languages" (languages-path))))

(deftest test-language-path
  (is (= "/languages/de-german" (language-path german))))

(deftest test-new-language-path
  (is (= "/languages/new" (new-language-path))))

(deftest test-edit-language-path
  (is (= "/languages/de-german/edit" (edit-language-path german))))

(deftest test-language
  (with-redefs
    [*client*
     (fn [request]
       (is (= :get (:request-method request)))
       (is (= :https (:scheme request)))
       (is (= "api.other.com" (:server-name request)))
       (is (= (language-path german) (:uri request)))
       (is (= {} (:body request))))]
    (language german)))

(deftest test-languages
  (with-redefs
    [*client*
     (fn [request]
       (is (= :get (:request-method request)))
       (is (= :https (:scheme request)))
       (is (= "api.other.com" (:server-name request)))
       (is (= (languages-path) (:uri request)))
       (is (= {} (:body request)))
       (is (= {:page 1} (:query-params request))))]
    (languages {:query-params {:page 1}})))

(deftest test-create-language
  (with-redefs
    [*client*
     (fn [request]
       (is (= :post (:request-method request)))
       (is (= :https (:scheme request)))
       (is (= "api.other.com" (:server-name request)))
       (is (= (languages-path) (:uri request)))
       (is (= german (:body request))))]
    (create-language german)))

(deftest test-update-language
  (with-redefs
    [*client*
     (fn [request]
       (is (= :put (:request-method request)))
       (is (= :https (:scheme request)))
       (is (= "api.other.com" (:server-name request)))
       (is (= (language-path german) (:uri request)))
       (is (= german (:body request))))]
    (update-language german)))

(deftest test-delete-language
  (with-redefs
    [*client*
     (fn [request]
       (is (= :delete (:request-method request)))
       (is (= :https (:scheme request)))
       (is (= "api.other.com" (:server-name request)))
       (is (= (language-path german) (:uri request)))
       (is (= {} (:body request))))]
    (delete-language german)))

;; (comment

;;   (with-server "http://api.burningswell.dev"
;;     (continent (first (continents))))

;;   (with-server "http://api.burningswell.dev"
;;     (save-continent (first (continents))))

;;   (with-server "http://api.burningswell.dev"
;;     (create-continent {:name "TEST"
;;                        :iso-3166-1-alpha-2 "xx"
;;                        :iso-3166-1-alpha-3 "xxx"
;;                        :freebase-guid "3243234234"
;;                        :geonames-id 123123
;;                        :location {:latitude 0 :longitude 0}}))

;;   (with-server "http://api.burningswell.dev"
;;     (save-continent {:name "TEST"
;;                      :iso-3166-1-alpha-2 "xx"
;;                      :iso-3166-1-alpha-3 "xxx"
;;                      :freebase-guid "3243234234"
;;                      :geonames-id 123123
;;                      :location {:latitude 0 :longitude 0}}))

;;   (with-server "http://api.burningswell.dev"
;;     (new-continent? {:name "TEST"
;;                      :iso-3166-1-alpha-2 "xx"
;;                      :iso-3166-1-alpha-3 "xxx"
;;                      :freebase-guid "3243234234"
;;                      :geonames-id 123123
;;                      :location {:latitude 0 :longitude 0}}))

;;   (with-server "http://api.burningswell.dev"
;;     (delete-continent {:iso-3166-1-alpha-2 "xx"}))

;;   )
