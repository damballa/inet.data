(ns inet.data.format.flat-test
  (:require [inet.data.format.flat :as flat]
            [inet.data.dns :as dns]
            [inet.data.ip :as ip]
            [clojure.java.io :as io])
  (:use [clojure.test]))

(deftest test-load
  (testing "Generic loading"
    (is (= [["com"] ["co.uk"]]
           (flat/load vec vec (io/resource "flat/etlds")))
        "Loads non-blank, non-comment entries")
    (is (= [["com"] ["co.uk"]]
           (flat/load vec vec nil (io/resource "flat/etlds") nil))
        "Ignores `nil` paths")))

(deftest test-domains
  (let [etlds (flat/load-domain-set (io/resource "flat/etlds"))]
    (is (= #{(dns/domain "com") (dns/domain "co.uk")} etlds)
        "Domain-set loads correctly")
    (testing "ETLD lookup"
      (is (= (dns/domain "co.uk") (flat/domain-etld etlds "www.example.co.uk")))
      (is (= nil (flat/domain-etld etlds "co.uk"))))
    (testing "E2LD lookup"
      (is (= (dns/domain "example.co.uk")
             (flat/domain-e2ld etlds "www.example.co.uk")))
      (is (= nil (flat/domain-e2ld etlds "co.uk"))))))

(deftest test-networks
  (let [rfc1918 (flat/load-network-set (io/resource "flat/rfc1918"))]
    (testing "Loaded network-set lookup"
      (is (= "10.0.0.0/8" (->> "10.3.13.8" (get rfc1918) first str)))
      (is (= nil (->> "11.3.13.8" (get rfc1918)))))))
