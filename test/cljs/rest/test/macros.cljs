(ns rest.test.macros
  (:use [rest.core :only (route)]
        [rest.util :only (format-pattern parse-keys)])
  (:use-macros [rest.macros :only (defroute)]))

(defn test-defroute []
  (defroute continents []
    "/continents")
  (assert (= "/continents" (continents-path)))
  (assert (= "https://example.com/continents" (continents-url)))
  (let [route (route :continents)]
    (assert (= "/continents" (:pattern route)))
    (assert (= [] (:args route)))
    (assert (= :continents (:name route)))
    (assert (= [] (:params route))))
  (let [request (continents :page 1)]
    (assert (= (continents-path) (:uri request)))
    (assert (= {:page 1} (:query-params request))))
  (defroute continent [continent]
    "/continents/:iso-3166-1-alpha-2-:name")
  (let [europe {:iso-3166-1-alpha-2 "eu" :name "europe"}]
    (assert (= "/continents/eu-europe" (continent-path europe)))
    (assert (= "https://example.com/continents/eu-europe" (continent-url europe)))
    (let [route (route :continent)]
      (assert (= "/continents/:iso-3166-1-alpha-2-:name" (:pattern route)))
      (assert (= ['continent] (:args route)))
      (assert (= :continent (:name route)))
      (assert (= [[:iso-3166-1-alpha-2 :name]] (:params route))))
    (let [request (continent europe :page 1)]
      (assert (= (continent-path europe) (:uri request)))
      (assert (= {:page 1} (:query-params request))))))

(defn test []
  (test-defroute))