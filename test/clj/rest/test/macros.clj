(ns rest.test.macros
  (:use rest.client
        clojure.test
        rest.macros))

(def germany {:iso-3166-1-alpha-2 "de" :name "Germany"})

(deftest test-defresources
  (defresources countries [country]
    "/countries/:iso-3166-1-alpha-2-:name")
  (is (= "/countries" (countries-path)))
  (is (= "/countries/de-germany" (country-path germany)))
  (is (= "/countries/new" (new-country-path)))
  (is (= "/countries/de-germany/edit" (edit-country-path germany)))
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= "https://example.com/countries/de-germany" url))
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
       (is (= "https://example.com/countries/de-germany" url))
       (is (= germany (:body request))))]
    (update-country germany))
  (with-redefs
    [send-request
     (fn [url & [request]]
       (is (= :delete (:method request)))
       (is (= "https://example.com/countries/de-germany" url)))]
    (delete-country germany)))
