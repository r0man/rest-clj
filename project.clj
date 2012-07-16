(defproject rest-clj "0.0.1-SNAPSHOT"
  :description "A HTTP REST library for Clojure."
  :url "http://github.com/r0man/rest-clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0"
  :dependencies [[clj-http "0.4.3"]
                 [cljs-http "0.0.2-SNAPSHOT"]
                 [org.clojure/clojure "1.4.0"]
                 [org.clojure/data.json "0.1.2"]
                 [routes-clj "0.0.2-SNAPSHOT"]]
  :plugins [[lein-cljsbuild "0.2.4"]]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {:builds [{:compiler {:output-to "target/rest-debug.js"}
                        :source-path "src/cljs"}
                       {:compiler {:output-to "target/rest.js"
                                   :optimizations :advanced
                                   :pretty-print false}
                        :source-path "src/cljs"
                        :jar true}
                       {:compiler {:output-to "target/rest-test.js"
                                   :optimizations :whitespace
                                   ;; :optimizations :advanced
                                   :pretty-print true}
                        :source-path "test/cljs"}]
              :crossover-jar true
              :crossover-path ".crossover-cljs"
              :crossovers [rest.core
                           rest.io]
              :repl-listen-port 9000
              :repl-launch-commands
              {"chromium" ["chromium" "http://localhost:9000/"]
               "firefox" ["firefox" "http://http://localhost:9000/"]}
              :test-commands {"unit" ["./test-cljs.sh"]}}
  :source-paths ["src/clj"]
  :test-paths ["test/clj"])