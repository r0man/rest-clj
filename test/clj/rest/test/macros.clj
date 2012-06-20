(ns rest.test.macros
  (:use [rest.routes :only (route)]
        clojure.test
        rest.macros))

(def europe {:iso-3166-1-alpha-2 "eu" :name "Europe"})
(def spain {:iso-3166-1-alpha-2 "es" :name "Spain"})

(deftest test-defroute
  (testing "/continents"
    (defroute continents []
      "/continents")
    (is (= "/continents" (continents-path)))
    (is (= "https://example.com/continents" (continents-url)))
    (let [route (route :continents)]
      (is (= "/continents" (:pattern route)))
      (is (= [] (:args route)))
      (is (= :continents (:name route)))
      (is (= [] (:params route)))))
  (testing "/continents/:iso-3166-1-alpha-2-:name"
    (defroute continent [continent]
      "/continents/:iso-3166-1-alpha-2-:name")
    (is (= "/continents/eu-europe" (continent-path europe)))
    (is (= "https://example.com/continents/eu-europe" (continent-url europe)))
    (let [route (route :continent)]
      (is (= "/continents/:iso-3166-1-alpha-2-:name" (:pattern route)))
      (is (= ['continent] (:args route)))
      (is (= :continent (:name route)))
      (is (= [[:iso-3166-1-alpha-2 :name]] (:params route))))))

(deftest test-defresources
  (defresources continents [continent]
    "/continents/:iso-3166-1-alpha-2-:name")
  (is (= "/continents" (continents-path)))
  (is (= "/continents/eu-europe" (continent-path europe)))
  (is (= "/continents/new" (new-continent-path)))
  (is (= "/continents/eu-europe/edit" (edit-continent-path europe))))