(defproject rest-clj "0.0.2-SNAPSHOT"
  :description "A HTTP REST library for Clojure."
  :url "http://github.com/r0man/rest-clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0"
  :dependencies [[clj-http "0.6.4"]
                 [clj-stacktrace "0.2.5"]
                 [cljs-http "0.0.2-SNAPSHOT"]
                 [org.clojure/clojure "1.5.0"]
                 [routes-clj "0.0.2-SNAPSHOT"]
                 [slingshot "0.10.3"]]
  :profiles {:dev {:dependencies [[com.cemerick/clojurescript.test "0.0.2"]]}}
  :plugins [[lein-cljsbuild "0.3.0"]]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {:builds [{:compiler {:output-to "target/rest-test.js"
                                   :optimizations :advanced
                                   :pretty-print true}
                        :source-paths ["test"]}
                       {:compiler {:output-to "target/rest-debug.js"
                                   :optimizations :whitespace
                                   :pretty-print true}
                        :source-paths ["src"]}
                       {:compiler {:output-to "target/rest.js"
                                   :optimizations :advanced
                                   :pretty-print true}
                        :source-paths ["src"]}]
              :crossover-jar true
              :crossovers [rest.http rest.io]
              :repl-listen-port 9000
              :repl-launch-commands
              {"chromium" ["chromium" "http://localhost:9000/"]
               "firefox" ["firefox" "http://http://localhost:9000/"]}
              :test-commands {"unit-tests" ["runners/phantomjs.js" "target/rest-test.js"]}})
