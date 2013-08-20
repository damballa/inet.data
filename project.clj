(defproject inet.data "0.5.4-SNAPSHOT"
  :description "Represent and manipulate various Internet entities as data."
  :url "http://github.com/llasram/inet.data"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [hier-set "1.1.2"]]
  :plugins [[lein-ragel "0.1.0"]
            [codox "0.6.4"]]
  :source-paths ["src/clojure"]
  :java-source-paths ["src/java" "target/ragel"]
  :ragel-source-paths ["src/ragel"]
  :javac-options ["-g"]
  :prep-tasks ["ragel" "javac"]
  :warn-on-reflection true
  :codox {:exclude [inet.data.util]}
  :profiles {:dev {:dependencies [[byteable "0.2.0"]
                                  [com.damballa/abracad "0.4.0"]
                                  [criterium "0.4.1"]]}})
