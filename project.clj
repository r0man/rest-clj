(defproject rest-clj "0.0.1-SNAPSHOT"
  :description "A REST library for Clojure."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[clj-http "0.3.3"]
                 [org.clojure/clojure "1.4.0-beta4"]
                 [org.clojure/data.json "0.1.2"]]
  :plugins [[lein-cljsbuild "0.1.2"]]
  :source-paths ["src/clj"]
  :test-paths ["test/clj"])