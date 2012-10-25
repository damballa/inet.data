(ns inet.data.s11n.byteable-test
  (:require [byteable.core :as b]
            [inet.data.dns :as dns]
            [inet.data.ip :as ip]
            [inet.data.s11n.byteable])
  (:use [clojure.test]
        [inet.data.util :only [doto-let]])
  (:import [java.io ByteArrayInputStream ByteArrayOutputStream
                    DataInputStream DataOutputStream]))

(defn- round-trip
  [x] (let [baos (doto-let [baos (ByteArrayOutputStream.)]
                   (b/write x (DataOutputStream. baos)))
            dis (->> baos .toByteArray ByteArrayInputStream. DataInputStream.)
            read (b/read-for (class x))]
        (read nil dis)))

(deftest test-ip-address
  (testing "IPAddress byteable de/s11n round-tripping"
    (let [addr (ip/address "192.168.1.1")]
      (is (= addr (round-trip addr))) "for IPv4 addresses")
    (let [addr (ip/address "fe:1100::1")]
      (is (= addr (round-trip addr))) "for IPv6 addresses")))

(deftest test-ip-network
  (testing "IPNetwork byteable de/s11n round-tripping"
    (let [net (ip/network "192.168.0.0/16")]
      (is (= net (round-trip net)) "for IPv4 networks"))
    (let [net (ip/network "fe:1100::/32")]
      (is (= net (round-trip net)) "for IPv6 networks"))
    (let [net (ip/network "fe:1100::/128")]
      (is (= net (round-trip net)) "for full-address IPv6 networks"))))

(deftest test-dns-domain
  (testing "DNSDomain byteable de/s11n round-tripping"
    (let [dom (dns/domain "www.google.com")]
      (is (= dom (round-trip dom))))
    (let [dom (dns/domain-next (dns/domain "www.google.com"))]
      (is (= dom (round-trip dom))))))
