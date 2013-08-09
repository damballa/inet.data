(ns inet.data.s11n.abracad
  "Avro serialization implementations for inet.data types."
  (:require [abracad.avro :as avro]
            [inet.data.ip :as ip]
            [inet.data.dns :as dns])
  (:use [inet.data.util :only [doto-let sbyte ubyte]])
  (:import [java.nio ByteBuffer]
           [inet.data.ip IPAddress IPNetwork]
           [inet.data.dns DNSDomain]))

(def ip-address-schema
  {:name "inet.data.ip.address", :type "record",
   :fields [{:name "address",
             :type [{:name "address-v4", :type "fixed", :size 4},
                    {:name "address-v6", :type "fixed", :size 16}]}]})

(extend-type IPAddress
  avro/AvroSerializable
  (schema-name [_] "inet.data.ip.address")
  (field-get [this _] (ip/address-bytes this))
  (field-list [_] #{:address}))

(def ip-network-schema
  {:name "inet.data.ip.network", :type "record",
   :fields [{:name "prefix",
             :type [{:name "prefix-v4", :type "fixed", :size 4},
                    {:name "prefix-v6", :type "fixed", :size 16}]},
            {:name "length", :type "int"}]})

(extend-type IPNetwork
  avro/AvroSerializable
  (schema-name [_] "inet.data.ip.network")
  (field-get [this field]
    (case field
      :prefix (ip/address-bytes this)
      :length (ip/network-length this)))
  (field-list [_] #{:prefix :length}))

(def dns-domain-schema
  {:name "inet.data.dns.domain", :type "record",
   :fields [{:name "bytes", :type "bytes"}]})

(extend-type DNSDomain
  avro/AvroSerializable
  (schema-name [_] "inet.data.dns.domain")
  (field-get [this _]
    (let [^bytes bytes (dns/domain-bytes this)
          length (dns/domain-length this)]
      (if (= length (alength bytes))
        bytes
        (ByteBuffer/wrap bytes 0 length))))
  (field-list [_] #{:bytes}))
