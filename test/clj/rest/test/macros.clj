(ns rest.test.macros
  (:use rest.client
        rest.routes
        clojure.test
        rest.macros)
  (:import rest.routes.Route))

(def europe {:iso-3166-1-alpha-2 "eu" :name "Europe"})
(def spain {:iso-3166-1-alpha-2 "es" :name "Spain"})

(deftest test-defresources
  (defresources continents [continent]
    "/continents/:iso-3166-1-alpha-2-:name")
  (is (= "/continents" (continents-path)))
  (is (= "/continents/eu-europe" (continent-path europe)))
  (is (= "/continents/new" (new-continent-path)))
  (is (= "/continents/eu-europe/edit" (edit-continent-path europe)))
  (with-redefs
    [send-request
     (fn [method url & [request]]
       (is (= :get method))
       (is (= "https://example.com/continents/eu-europe" url))
       (is (nil? request)))]
    (continent europe))
  (with-redefs
    [send-request
     (fn [method url & [request]]
       (is (= :get method))
       (is (= "https://example.com/continents" url))
       (is (nil? request)))]
    (continents))
  (with-redefs
    [send-request
     (fn [method url & [request]]
       (is (= :post method))
       (is (= "https://example.com/continents" url))
       (is (= {:body europe} request)))]
    (create-continent europe))
  (with-redefs
    [send-request
     (fn [method url & [request]]
       (is (= :put method))
       (is (= "https://example.com/continents/eu-europe" url))
       (is (= {:body europe} request)))]
    (update-continent europe))
  (with-redefs
    [send-request
     (fn [method url & [request]]
       (is (= :delete method))
       (is (= "https://example.com/continents/eu-europe" url))
       (is (nil? request)))]
    (delete-continent europe)))
