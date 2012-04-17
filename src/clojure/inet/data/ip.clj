(ns inet.data.ip
  "Functions for interacting with IP addresses and networks."
  (:require [clojure.string :as str])
  (:use [inet.data.util :only
         [ignore-errors case-expr ubyte sbyte longest-run bytes-hash-code]])
  (:import [clojure.lang  IFn IObj IPersistentMap ILookup]
           [inet.data.ip IPParser IPAddressException IPNetworkException]
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
  (^:private address-bytes [addr]
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

(defn- address-error [msg & args]
  (throw (IPAddressException. ^String (apply format msg args))))

(defn- network-error [msg & args]
  (throw (IPNetworkException. ^String (apply format msg args))))

(defn- network-compare*
  "Private version of network-compare.  The value of `length` must be the
minimum common prefix length of the two networks."
  ^long [stable ^long length left right]
  (let [^bytes prefix1 (address-bytes left),
        ^bytes prefix2 (address-bytes right),
        len1 (alength prefix1), len2 (alength prefix2)]
    (if (not= len1 len2)
      (- len1 len2)
      (loop [i (long 0), rem (long length)]
        (if-not (pos? rem)
          (if-not stable 0 (- (network-length left) (network-length right)))
          (let [mask (if (< 8 rem) 0xff (bit-not (bit-shift-right 0xff rem)))
                b1 (bit-and mask (ubyte (aget prefix1 i)))
                b2 (bit-and mask (ubyte (aget prefix2 i)))]
            (if (not= b1 b2)
              (- b1 b2)
              (recur (inc i) (- rem 8)))))))))

(defn network-compare
  "Compare the prefixes of networks `left` and `right`, with the same result
semantics as `compare`.  When `stable` is true (the default), 0 will only be
returned when the networks are value-identical; when `stable` is false, 0 will
be returned as long as the networks are identical up to their minimum common
prefix length."
  (^long [left right] (network-compare true left right))
  (^long [stable left right]
     (let [length (min (network-length left) (network-length right))]
       (network-compare* stable length left right))))

(defn network-contains?
  "Determine if network `net` contains the address/network `addr`."
  ([net addr]
     (let [length (network-length net)]
       (and (<= length (network-length addr))
            (zero? (network-compare* false length net addr))))))

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

(deftype IPAddress [^IPersistentMap meta, ^bytes bytes]
  Object
  (toString [this] (string-address bytes))
  (hashCode [this] (bytes-hash-code bytes))
  (equals [this other]
    (and (instance? IPAddress other)
         (Arrays/equals bytes ^bytes (address-bytes other))))

  IObj
  (meta [this] meta)
  (withMeta [this new-meta] (IPAddress. new-meta bytes))

  Comparable
  (compareTo [this other]
    (network-compare true this other))

  IPAddressOperations
  (address?* [this] true)
  (address-bytes [this] bytes)
  (address-length [this] (address-length bytes))

  IPNetworkOperations
  (network?* [this] false)
  (network-length [this] (address-length bytes)))

(deftype IPNetwork [^IPersistentMap meta, ^bytes prefix, ^long length]
  Object
  (toString [this] (str (string-address prefix) "/" length))
  (hashCode [this] (bytes-hash-code bytes length))
  (equals [this other]
    (and (instance? IPNetwork other)
         (= length (network-length other))
         (Arrays/equals prefix ^bytes (address-bytes other))))

  IObj
  (meta [this] meta)
  (withMeta [this new-meta] (IPNetwork. new-meta prefix length))

  Comparable
  (compareTo [this other]
    (network-compare true this other))

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
  (if (address?* bytes)
    (IPAddress. nil bytes)
    (address-error "%s: invalid address" (str orig))))

(defn- network*
  [orig ^bytes bytes ^long length]
  (if (network?* bytes length)
    (IPNetwork. nil bytes length)
    (network-error "%s/%d: invalid network" (str orig) length)))

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
