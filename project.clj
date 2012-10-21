(defproject rest-clj "0.0.2-SNAPSHOT"
  :description "A HTTP REST library for Clojure."
  :url "http://github.com/r0man/rest-clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0"
  :dependencies [[clj-http "0.5.6"]
                 [clj-stacktrace "0.2.4"]
                 [cljs-http "0.0.2-SNAPSHOT"]
                 [org.clojure/clojure "1.4.0"]
                 [org.clojure/data.json "0.2.0"]
                 [routes-clj "0.0.2-SNAPSHOT"]]
  :plugins [[lein-cljsbuild "0.2.7"]]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {:builds [{:compiler {:output-to "target/rest-test.js"
                                   :optimizations :whitespace
                                   :pretty-print true}
                        :source-path "test/cljs"}
                       {:compiler {:output-to "target/rest-debug.js"
                                   :optimizations :whitespace
                                   :pretty-print true}
                        :source-path "src/cljs"}
                       {:compiler {:output-to "target/rest.js"
                                   :optimizations :advanced
                                   :pretty-print true}
                        :source-path "src/cljs"
                        :jar true}]
              :crossover-jar true
              :crossover-path ".crossover-cljs"
              :crossovers [rest.http
                           rest.io]
              :repl-listen-port 9000
              :repl-launch-commands
              {"chromium" ["chromium" "http://localhost:9000/"]
               "firefox" ["firefox" "http://http://localhost:9000/"]}
              :test-commands {"unit" ["./test-cljs.sh"]}}
  :source-paths ["src/clj"]
  :test-paths ["test/clj"])