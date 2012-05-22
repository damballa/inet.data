(ns inet.data.dns.psl
  "Functions for interfacing with Mozilla Public Suffix List format files."
  (:refer-clojure :exclude [load])
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [inet.data.dns :as dns])
  (:import [java.io Reader]))

(def ^:dynamic *default-psl-url*
  "URL of the default Mozilla Public Suffix List file."
  (str "http://mxr.mozilla.org/mozilla-central/source/netwerk/dns/"
       "effective_tld_names.dat?raw=1"))

(defn load
  "Load a Mozilla Public Suffix List format file from the Reader `source`."
  [^Reader source]
  (letfn [(prefix? [^String s1 ^String s2] (.startsWith s2 s1))
          (ignorable? [s] (or (empty? s) (prefix? "//" s)))
          (convert [entry n rule]
            (let [prefix (-> entry (subs n) dns/domain)]
              [prefix rule]))
          (parse [entry]
            (condp prefix? entry
              "*." (convert entry 2 :wildcard)
              "!"  (convert entry 1 :exception)
                   (convert entry 0 :normal)))
          (step [[prefixes rules] entry]
            (let [[prefix rule] (parse entry)]
              [(conj prefixes prefix) (assoc rules prefix rule)]))]
    (->> (line-seq source) (map str/trim) (remove ignorable?)
         (reduce step [(dns/domain-set) {}]))))

(def ^:private memo-load
  (memoize
   (fn [url]
     (with-open [source (io/reader url)]
       (load source)))))

(defn lookup
  "Determine the E2LD of `domain` as specified by the PSL loaded in `psl`.
Uses the default PSL loaded from `*default-psl-url*` if `psl` is not provided."
  ([dom] (lookup (memo-load *default-psl-url*) dom))
  ([psl dom]
     (let [dom (dns/domain dom), [prefixes rules] psl]
       (when-let [prefix (->> dom (get prefixes) first)]
         (case (get rules prefix)
           :exception prefix
           :normal    (dns/domain-next dom prefix)
           :wildcard  (second (dns/domain-ancestors dom prefix)))))))
