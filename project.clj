(defproject rest-clj "0.0.1-SNAPSHOT"
  :description "A HTTP REST library for Clojure."
  :url "http://github.com/r0man/rest-clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[clj-http "0.4.3"]
                 [inflections "0.7.0-SNAPSHOT"]
                 [org.clojure/clojure "1.4.0"]
                 [org.clojure/data.json "0.1.2"]]
  :plugins [[lein-cljsbuild "0.2.1"]]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {:builds [{:source-path "src/cljs"
                        :compiler {:output-to "target/rest-debug.js"}}
                       {:source-path "src/cljs"
                        :compiler {:output-to "target/rest.js"
                                   :optimizations :advanced
                                   :pretty-print false}
                        :jar true}]
              :repl-listen-port 9000
              :repl-launch-commands
              {"chromium" ["chromium" "http://localhost:9000/"]
               "firefox" ["firefox" "http://http://localhost:9000/"]}}
  :source-paths ["src/clj"]
  :test-paths ["test/clj"])