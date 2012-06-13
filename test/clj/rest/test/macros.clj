(ns rest.test.macros
  (:use clojure.test
        rest.core
        rest.macros))

(deftest test-defroute
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
    (let [request (continent europe)]
      (is (= (continent-path europe) (:uri request)))
      (is (= {} (:query-params request))))))
