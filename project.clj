(defproject inet.data "0.5.7-SNAPSHOT"
  :description "Represent and manipulate various Internet entities as data."
  :url "http://github.com/llasram/inet.data"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :global-vars {*warn-on-reflection* true}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [hier-set "1.1.2"]]
  :plugins [[lein-ragel "0.1.0"]
            [codox "0.6.6"]]
  :source-paths ["src/clojure"]
  :java-source-paths ["src/java" "target/ragel"]
  :ragel-source-paths ["src/ragel"]
  :javac-options ["-target" "1.7" "-source" "1.7"]
  :prep-tasks ["ragel" "javac"]
  :codox {:src-dir-uri "https://github.com/damballa/inet.data/blob/master/"
          :src-linenum-anchor-prefix "L"
          :sources ["src/clojure"]
          :exclude [inet.data.util]
          :output-dir "tmp/codox"}
  :aliases {"prep" ["do" "ragel," "javac"]
            "all" ["with-profile" ~(str "+clojure-1-6:"
                                        "+clojure-1-7")]}
  :profiles {:provided {:dependencies
                        [[byteable "0.2.0"]
                         [com.damballa/abracad "0.4.12"]]}
             :clojure-1-6 {:dependencies
                           [[org.clojure/clojure "1.6.0"]]}
             :clojure-1-7 {:dependencies
                           [[org.clojure/clojure "1.7.0"]]}})
