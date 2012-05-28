(defproject inet.data "0.1.1-SNAPSHOT"
  :description "Represent and manipulate various Internet entities as data."
  :url "http://github.com/llasram/inet.data"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [hier-set "1.1.1"]]
  :plugins [[lein-ragel "0.1.0"]]
  :source-paths ["src/clojure"]
  :java-source-paths ["src/java" "target/ragel"]
  :ragel-source-paths ["src/ragel"]
  :javac-options ["-g"]
  :prep-tasks [ragel javac]
  :profiles {:dev {:dependencies [[byteable "0.1.1"]]
                   :warn-on-reflection true}})
