(ns inet.data.s11n.abracad-test
  (:require [clojure.test :refer :all]
            [abracad.avro :as avro]
            [inet.data.dns :as dns]
            [inet.data.ip :as ip]
            [inet.data.s11n.abracad :as s11n]
            [inet.data.util :refer [doto-let]]))

(def schema
  (avro/parse-schema
   [s11n/ip-address-schema
    s11n/ip-network-schema
    s11n/dns-domain-schema]))

(defn round-trip
  [x] (avro/decode schema (avro/binary-encoded schema x)))

(deftest test-ip-address
  (testing "IPAddress abracad de/s11n round-tripping"
    (let [addr (ip/address "192.168.1.1")]
      (is (= addr (round-trip addr))) "for IPv4 addresses")
    (let [addr (ip/address "fe:1100::1")]
      (is (= addr (round-trip addr))) "for IPv6 addresses")))

(deftest test-ip-network
  (testing "IPNetwork abracad de/s11n round-tripping"
    (let [net (ip/network "192.168.0.0/16")]
      (is (= net (round-trip net)) "for IPv4 networks"))
    (let [net (ip/network "fe:1100::/32")]
      (is (= net (round-trip net)) "for IPv6 networks"))
    (let [net (ip/network "fe:1100::/128")]
      (is (= net (round-trip net)) "for full-address IPv6 networks"))))

(deftest test-dns-domain
  (testing "DNSDomain abracad de/s11n round-tripping"
    (let [dom (dns/domain "www.google.com")]
      (is (= dom (round-trip dom))))
    (let [dom (dns/domain-next (dns/domain "www.google.com"))]
      (is (= dom (round-trip dom))))))
