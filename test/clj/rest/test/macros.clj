(ns rest.test.macros
  (:use [rest.core :only (route)]
        clojure.test
        rest.macros))

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
      (is (= [] (:params route))))
    (let [request (continents :page 1)]
      (is (= (continents-path) (:uri request)))
      (is (= {:page 1} (:query-params request)))))
  (testing "/continents/:iso-3166-1-alpha-2-:name"
    (defroute continent [continent]
      "/continents/:iso-3166-1-alpha-2-:name")
    (let [europe {:iso-3166-1-alpha-2 "eu" :name "europe"}]
      (is (= "/continents/eu-europe" (continent-path europe)))
      (is (= "https://example.com/continents/eu-europe" (continent-url europe)))
      (let [route (route :continent)]
        (is (= "/continents/:iso-3166-1-alpha-2-:name" (:pattern route)))
        (is (= ['continent] (:args route)))
        (is (= :continent (:name route)))
        (is (= [[:iso-3166-1-alpha-2 :name]] (:params route))))
      (let [request (continent europe :page 1)]
        (is (= (continent-path europe) (:uri request)))
        (is (= {:page 1} (:query-params request)))))))
