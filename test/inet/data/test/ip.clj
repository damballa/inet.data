(ns inet.data.test.ip
  (:require [inet.data.ip :as ip])
  (:use [clojure.test])
  (:import [java.net InetAddress]))

(deftest test-inet-address
  (testing "Round-tripping IPv4 address strings"
    (is (= "172.12.16.1" (str (ip/address "172.12.16.1")))))
  (testing "Round-tripping IPv6 address strings"
    (is (= "fe:1100::1" (str (ip/address "fe:1100:0::1"))))))

(deftest test-inet-network
  (testing "Creating networks"
    (testing "from IPv4 addresses"
      (let [addr-str   "192.168.0.0"
            addr-obj   (InetAddress/getByName addr-str)
            addr-bytes (.getAddress addr-obj)
            test-data [[addr-str "string"]
                       [addr-obj "InetAddress"]
                       [addr-bytes "bytes"]]]
        (doseq [[addr src] test-data]
          (is (= "192.168.0.0/32" (str (ip/network addr)))
              (format "From %s with implied prefix-length." src))
          (is (= "192.168.0.0/16" (str (ip/network addr 16)))
              (format "From %s with explicit prefix-length." src)))))
    (testing "from IPv6 addresses"
      (let [addr-str   "fe:11::"
            addr-obj   (InetAddress/getByName addr-str)
            addr-bytes (.getAddress addr-obj)
            test-data  [[addr-str "string"]
                        [addr-obj "InetAddress"]
                        [addr-bytes "bytes"]]]
        (doseq [[addr src] test-data]
          (is (= "fe:11::/128" (str (ip/network addr)))
              (format "From %s with implied prefix-length." src))
          (is (= "fe:11::/32" (str (ip/network addr 32)))
              (format "From %s with explicit prefix-length." src)))))))

(deftest test-network-contains
  (testing "Network does contain address"
    (is (ip/network-contains? "192.168.0.0/16" "192.168.13.37"))
    (is (ip/network-contains? "192.168.0.0/17" "192.168.127.1")))
  (testing "Network doesn't contain address"
    (is (not (ip/network-contains? "192.168.0.0/16" "8.8.8.8")))
    (is (not (ip/network-contains? "192.168.0.0/17" "192.168.128.1")))))
