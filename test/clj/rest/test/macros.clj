(ns rest.test.macros
  (:use clojure.test
        rest.macros))

(deftest test-defroute
  (defroute continent [continent]
    "/continents/:iso-3166-1-alpha-2-:name")
  (let [europe {:iso-3166-1-alpha-2 "eu" :name "europe"}]
    (is (fn? continent))
    (is (= "/continents/eu-europe" (continent-path europe)))
    (is (= "https://example.com/continents/eu-europe" (continent-url europe)))
    (let [request (continent europe)]
      (is (= (continent-path europe) (:uri request)))
      (is (= {} (:query-params request))))))
