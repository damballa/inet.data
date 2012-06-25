(ns inet.data.ip
  "Functions for interacting with IP addresses and networks."
  (:require [clojure.string :as str])
  (:use [inet.data.util :only [ignore-errors case-expr ubyte sbyte longest-run
                               bytes-hash-code doto-let]]
        [hier-set.core :only [hier-set-by]])
  (:import [clojure.lang IFn IObj ILookup]
           [inet.data.ip IPParser IPNetworkComparison]
           [java.util Arrays]
           [java.net InetAddress]))

(defprotocol IPAddressConstruction
  "Construct a full address object."
  (address [addr]
    "Create an IPAddress from another representation."))

(defprotocol IPAddressOperations
  "Operations on objects which may be treated as addresses."
  (^:private address?* [addr]
    "Returns whether or not the value represents a valid address.")
  (address-bytes [addr]
    "Retrieve the bytes representation of this address.")
  (address-length [addr]
    "The length in bits of this address."))

(defprotocol IPNetworkConstruction
  "Construct a full network object."
  (network [net] [prefix length]
    "Create an IPNetwork from another representation."))

(defprotocol IPNetworkOperations
  "Operations on objects which may be treated as networks."
  (^:private network?* [net] [addr length]
    "Returns whether or not the value represents a valid network.")
  (network-length [net]
    "The length in bits of the network prefix."))

(defn address?
  "Determine if `addr` is a value which represents an IP address."
  [addr] (and (satisfies? IPAddressOperations addr)
              (boolean (address?* addr))))

(defn network?
  "Determine if `net` is a value which represents an IP network."
  ([net]
     (and (satisfies? IPNetworkOperations net)
          (boolean (network?* net))))
  ([addr length]
     (and (satisfies? IPNetworkOperations addr)
          (boolean (network?* addr length)))))

(defn inet-address
  "Generate a java.net.InetAddress from the provided value."
  [addr] (InetAddress/getByAddress (address-bytes addr)))

(defn network-trunc
  "Create a network with a prefix consisting of the first `length` bits of
`prefix` and a length of `length`."
  ([prefix]
     (network-trunc prefix (network-length prefix)))
  ([prefix length]
     (network (doto-let [prefix (byte-array (address-bytes prefix))]
                (loop [zbits (long (- (address-length prefix) length)),
                       i (->> prefix alength dec long)]
                  (cond (>= zbits 8) (do (aset prefix i (byte 0))
                                         (recur (- zbits 8) (dec i)))
                        (pos? zbits) (->> (bit-shift-left -1 zbits)
                                          (bit-and (aget prefix i)) byte
                                          (aset prefix i)))))
              length)))

(defn network-compare
  "Compare the prefixes of networks `left` and `right`, with the same result
semantics as `compare`.  When `stable` is true (the default), 0 will only be
returned when the networks are value-identical; when `stable` is false, 0 will
be returned as long as the networks are identical up to their minimum common
prefix length."
  (^long [left right] (network-compare true left right))
  (^long [stable left right]
     (let [^bytes prefix1 (address-bytes left), plen1 (network-length left)
           ^bytes prefix2 (address-bytes right), plen2 (network-length right)]
       (IPNetworkComparison/networkCompare stable prefix1 plen1 prefix2 plen2))))

(defn network-contains?
  "Determine if network `net` contains the address/network `addr`."
  ([net addr]
     (let [length (network-length net)]
       (and (<= length (network-length addr))
            (zero? (network-compare false net addr))))))

(defn network-set
  "Create a hierarchical set from networks `nets`."
  [& nets] (apply hier-set-by network-contains? network-compare
                  (map network nets)))

(defn- string-address-ipv4 [^bytes bytes]
  (->> bytes (map ubyte) (str/join ".")))

(letfn [(->short [[m x]] (-> m (bit-shift-left 8) (bit-or x)))
        (->str [xs] (->> xs (map #(format "%x" %)) (str/join ":")))]
  (defn- string-address-ipv6 [^bytes bytes]
    (let [shorts (->> bytes (map ubyte) (partition 2) (map ->short))]
      (if-let [[nt nd] (longest-run 0 shorts)]
        (str (->str (take nt shorts)) "::" (->str (drop (+ nt nd) shorts)))
        (->str shorts)))))

(defn- string-address
  [^bytes bytes]
  (case-expr (alength bytes)
    IPParser/IPV4_BYTE_LEN (string-address-ipv4 bytes)
    IPParser/IPV6_BYTE_LEN (string-address-ipv6 bytes)))

(deftype IPAddress [meta, ^bytes bytes]
  Object
  (toString [this] (string-address bytes))
  (hashCode [this] (bytes-hash-code bytes))
  (equals [this other]
    (or (identical? this other)
        (and (instance? IPAddress other)
             (Arrays/equals bytes ^bytes (address-bytes other)))))

  IObj
  (meta [this] meta)
  (withMeta [this new-meta] (IPAddress. new-meta bytes))

  Comparable
  (compareTo [this other]
    (let [plen1 (long (address-length bytes))
          ^bytes prefix2 (address-bytes other),
          plen2 (long (network-length other))]
      (IPNetworkComparison/networkCompare bytes plen1 prefix2 plen2)))

  IPAddressOperations
  (address?* [this] true)
  (address-bytes [this] bytes)
  (address-length [this] (address-length bytes))

  IPNetworkOperations
  (network?* [this] false)
  (network-length [this] (address-length bytes)))

(deftype IPNetwork [meta, ^bytes prefix, ^long length]
  Object
  (toString [this] (str (string-address prefix) "/" length))
  (hashCode [this] (bytes-hash-code prefix length))
  (equals [this other]
    (or (identical? this other)
        (and (instance? IPNetwork other)
             (= length (network-length other))
             (Arrays/equals prefix ^bytes (address-bytes other)))))

  IObj
  (meta [this] meta)
  (withMeta [this new-meta] (IPNetwork. new-meta prefix length))

  Comparable
  (compareTo [this other]
    (let [^bytes prefix2 (address-bytes other),
          plen2 (long (network-length other))]
      (IPNetworkComparison/networkCompare prefix length prefix2 plen2)))

  ILookup
  (valAt [this key]
    (when (network-contains? this key) key))
  (valAt [this key default]
    (if (network-contains? this key) key default))

  IFn
  (invoke [this key]
    (when (network-contains? this key) key))
  (invoke [this key default]
    (if (network-contains? this key) key default))

  IPAddressOperations
  (address?* [this] false)
  (address-bytes [this] prefix)
  (address-length [this] (address-length prefix))

  IPNetworkOperations
  (network?* [this] true)
  (network-length [this] length))

(defn- address*
  [orig ^bytes bytes]
  (when (address?* bytes)
    (IPAddress. nil bytes)))

(defn- network*
  [orig ^bytes bytes ^long length]
  (when (network?* bytes length)
    (IPNetwork. nil bytes length)))

(extend-type IPAddress
  IPAddressConstruction
  (address [this] this)

  IPNetworkConstruction
  (network
    ([this] (IPNetwork. nil (address-bytes this) (address-length this)))
    ([this length] (network* this (address-bytes this) length))))

(extend-type IPNetwork
  IPAddressConstruction
  (address [this] (IPAddress. nil (address-bytes this)))

  IPNetworkConstruction
  (network
    ([this] this)
    ([this length] (network* this (address-bytes this) length))))

(extend-type (java.lang.Class/forName "[B")
  IPAddressConstruction
  (address [this] (address* this this))

  IPAddressOperations
  (address?* [this]
    (let [len (alength ^bytes this)]
      (or (= len IPParser/IPV4_BYTE_LEN)
          (= len IPParser/IPV6_BYTE_LEN))))
  (address-bytes [this] this)
  (address-length [this] (* 8 (alength ^bytes this)))

  IPNetworkConstruction
  (network
    ([this] (network* this this (address-length this)))
    ([this length] (network* this this length)))

  IPNetworkOperations
  (network?*
    ([this] false)
    ([this length]
       (and (address?* this)
            (>= length 0)
            (<= length (address-length this))
            (->> (iterate #(if (pos? %) (- % 8) 0) length)
                 (map (fn [b rem]
                        (let [mask (if (<= 8 rem) 0 (bit-shift-right 0xff rem))]
                          (bit-and b mask)))
                      this)
                 (every? zero?)))))
  (network-length [this] (address-length this)))

(defn- string-network-split
  [net] (str/split net #"/" 2))

(defn- string-network-parts
  [net] (let [[prefix length] (string-network-split net)
              length (when length
                       (or (ignore-errors (Long/parseLong length)) -1))]
          [(IPParser/parse prefix) length]))

(extend-type String
  IPAddressConstruction
  (address [addr] (address* addr (address-bytes addr)))

  IPAddressOperations
  (address?* [this]
    (IPParser/isValid (first (string-network-split this))))
  (address-bytes [this]
    (IPParser/parse (first (string-network-split this))))
  (address-length [this]
    (IPParser/length (first (string-network-split this))))

  IPNetworkConstruction
  (network
    ([this]
       (let [[prefix length] (string-network-parts this)]
         (if length
           (network* this prefix length)
           (network* this prefix (address-length prefix)))))
    ([this length]
       (network* this (address-bytes this) length)))

  IPNetworkOperations
  (network?*
    ([this]
       (let [[prefix length] (string-network-parts this)]
         (when length
           (network?* prefix length))))
    ([this length]
       (let [[prefix _] (string-network-parts this)]
         (network?* prefix length))))
  (network-length [this]
    (let [[_ length] (string-network-parts this)]
      (or length (address-length this)))))

(extend-type InetAddress
  IPAddressConstruction
  (address [addr]
    (address* (.getHostAddress addr) (.getAddress addr)))

  IPAddressOperations
  (address?* [addr] true)
  (address-bytes [addr] (.getAddress addr))
  (address-length [addr]
    (case-expr (class addr)
      java.net.Inet4Address IPParser/IPV4_BIT_LEN
      java.net.Inet6Address IPParser/IPV6_BIT_LEN
      -1))

  IPNetworkConstruction
  (network
    ([this] (IPNetwork. nil (address-bytes this) (address-length this)))
    ([this length] (network* this (address-bytes this) length)))

  IPNetworkOperations
  (network?*
    ([this] false)
    ([this length] (network?* (address-bytes this) length)))
  (network-length [this] (address-length this)))

(defmethod clojure.core/print-method IPAddress
  ([^IPAddress addr ^java.io.Writer w]
     (.write w "#ip/address \"")
     (.write w (str addr))
     (.write w "\"")))

(defmethod clojure.core/print-method IPNetwork
  ([^IPNetwork net ^java.io.Writer w]
     (.write w "#ip/network \"")
     (.write w (str net))
     (.write w "\"")))
