(ns inet.data.format.flat
  "Functions for loading inet.data entities from flat line-oriented files."
  (:refer-clojure :exclude [load])
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [inet.data.ip :as ip]
            [inet.data.dns :as dns])
  (:use [inet.data.util :refer [ignore-errors ffilter]]))

(defn ^:private parse-lines [f & readers]
  (->> (mapcat line-seq readers)
       (map #(-> % (str/split #"\s+#" 2) first str/trim))
       (remove (fn [^String s] (or (.isEmpty s) (.startsWith s "#"))))
       (map #(f (str/split % #"\t")))))

(defn load
  "Read the non-`nil` members of `paths` for line-oriented, '#'-commented,
TAB-delimited entries; parse each entry with `entryf`; and parse the sequence
of entries with `collf`."
  [entryf collf & paths]
  (letfn [(step [readers paths]
            (if (seq paths)
              (if-let [path (first paths)]
                (with-open [r (io/reader path)]
                  (step (conj readers r) (rest paths)))
                (step readers (rest paths)))
              (when (seq readers)
                (collf (apply parse-lines entryf readers)))))]
    (step [] paths)))

(defn load-domain-set
  "Produce a dns/domain-set from the entries found in `paths`."
  [& paths]
  (apply load (comp dns/domain first) (partial apply dns/domain-set) paths))

(defn domain-etld
  "Return the effective TLD of the domain `dom` from the set `etlds`."
  [etlds dom]
  (let [dom (dns/domain dom), dlen (dns/domain-length dom)]
    (ffilter #(not= dlen (dns/domain-length %)) (get etlds dom))))

(defn domain-e2ld
  "Return the effective 2LD / zone / bailiwick of the domain `dom`, using
`etlds` as the set off ETLDs."
  [etlds dom]
  (let [dom (dns/domain dom), tld (domain-etld etlds dom)]
    (when tld (dns/domain-next dom tld))))

(defn load-network-set
  "Produce an ip/network-set from the entries found in `paths`."
  [& paths]
  (apply load (comp ip/network first) (partial apply ip/network-set) paths))
