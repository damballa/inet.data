(ns inet.data.s11n.byteable
  (:require [byteable.core :as b]
            [inet.data.ip :as ip]
            [inet.data.dns :as dns])
  (:use [inet.data.util :only [doto-let sbyte ubyte]])
  (:import [inet.data.ip IPAddress IPNetwork]
           [inet.data.dns DNSDomain]))

(b/extend-byteable
  IPAddress
  (read [_ input]
    (ip/address (doto-let [bytes (-> input .readByte ubyte byte-array)]
                  (.readFully input bytes))))
  (write [addr output]
    (let [^bytes bytes (ip/address-bytes addr)]
      (doto output
        (.writeByte (-> bytes alength sbyte))
        (.write     bytes))))

  IPNetwork
  (read [_ input]
    (ip/network (doto-let [bytes (-> input .readByte ubyte byte-array)]
                  (.readFully input bytes))
                (-> input .readByte ubyte)))
  (write [net output]
    (let [^bytes bytes (ip/address-bytes net)]
      (doto output
        (.writeByte (-> bytes alength sbyte))
        (.write     bytes)
        (.writeByte (-> net ip/network-length sbyte)))))

  DNSDomain
  (read [_ input]
    (dns/domain (doto-let [bytes (-> input .readByte ubyte byte-array)]
                  (.readFully input bytes))))
  (write [dom output]
    (let [^bytes bytes (dns/domain-bytes dom), len (dns/domain-length dom)]
      (doto output
        (.writeByte len)
        (.write     bytes 0 len)))))
