(ns inet.data.dns
  "Functions for interacting with DNS domain names.

Internally represents domain names using an normalized byte-oriented form.  The
normalized form is defined as: (a) all characters converted to lowercase; (b)
domain labels punycode-encoded; (c) domain labels arranged from top-level to
bottom-level (i.e., reversed from typical order); and (d) every label preceded
by a byte indicating the count of bytes in the label.

This form has the following benefits: (a) it may accurately represent any
binary data, just like the DNS wire form; (b) lexicographic byte order is also
hierarchical order; and (c) for a given child domain and ancestor domain, one
may easily find the next longer ancestor of the child.  The primary down-side
is that this form does make it more difficult to find the immediate parent of a
given domain."
  (:require [clojure.string :as str])
  (:use [inet.data.util :only [ffilter ubyte sbyte bytes-hash-code]])
  (:import [clojure.lang IFn ILookup IObj IPersistentMap]
           [inet.data.dns DNSDomainException DNSDomainParser]
           [java.util Arrays]
           [java.net IDN]))

(defprotocol DNSDomainConstruction
  "Construct a full domain object."
  (domain [dom]
    "Create a DNSDomain from another representation."))

(defprotocol DNSDomainOperations
  "Operations on objects which may be treated as domains."
  (^:private domain?* [dom]
    "Returns whether or not the value represents a valid domain.")
  (^:private domain-bytes [dom]
    "Retrieve the internal normalized byte form of the domain.")
  (domain-length [dom]
    "The length in bytes of this domain."))

(defn domain?
  "Determine if dom is a value which represents a DNS domain."
  [dom] (and (satisfies? DNSDomainOperations dom)
             (boolean (domain?* dom))))

(defn- domain-error [msg & args]
  (throw (DNSDomainException. ^String (apply format msg args))))

(defn- domain-compare*
  "Private version of domain-compare.  The value of length must be the minimum
full-label length of the two domains."
  ^long [stable length left right]
  (let [^bytes dom1 (domain-bytes left), ^bytes dom2 (domain-bytes right),
        len1 (domain-length left), len2 (domain-length right),
        length (long (min len1 len2))]
    (loop [i (long 0)]
      (if (>= i length)
        (if-not stable 0 (- len1 len2))
        (let [b1 (aget dom1 i), b2 (aget dom2 i)]
          (if (not= b1 b2)
            (- (ubyte b1) (ubyte b2))
            (recur (inc i))))))))

(defn domain-compare
  "Compare two domains, with the same result semantics as compare.  When stable
is true (the default), 0 will only be returned when the domains are
value-identical.  When stable is false, 0 will be returned as long as the
networks are identical up to their minimum common full-label length."
  (^long [left right] (domain-compare true left right))
  (^long [stable left right]
     (let [length (min (domain-length left) (domain-length right))]
       (domain-compare* stable length left right))))

(defn domain-contains?
  "Determine if the domain child is a subdomain of or identical to the domain
parent."
  [parent child]
  (let [length (domain-length parent)]
    (and (<= length (domain-length child))
         (zero? (domain-compare* false length parent child)))))

(defn domain-subdomain?
  "Determine if the domain child is a subdomain of the domain parent."
  [parent child]
  (let [length (domain-length parent)]
    (and (< length (domain-length child))
         (zero? (domain-compare* false length parent child)))))

(defn domain-hostname?
  "Determine if the provided domain is a valid hostname."
  [domain] (DNSDomainParser/isValidHostname (domain-bytes domain)))

(defn- name->bytes
  "Convert a string domain name into an internal normalized byte form."
  [name] (->> name str/lower-case (#(str/split % #"\." -1)) reverse
              (mapcat (fn [s]
                        (let [bytes (.getBytes (IDN/toASCII s))]
                          (cons (sbyte (count bytes)) bytes))))
              byte-array))

(defn- wire->bytes
  "Convert a DNS wire-form domain name into an internal normalized byte form."
  ([wire]
     (->> [nil wire]
          (iterate (fn [[state data]]
                     (let [n (inc (first data))]
                       [(conj state (take n data))
                        (drop n data)])))
          (ffilter (comp empty? second))
          first (drop 1) (apply concat) byte-array))
  ([wire ^long offset ^long length]
     (wire->bytes (->> wire (drop offset) (take length)))))

(defn- bytes->name
  "Convert the internal normalized byte form of the domain in bytes into its
standard string form."
  ([bytes]
     (->> [nil bytes]
          (iterate (fn [[state data]]
                     (let [n (inc (first data))]
                       [(conj state (drop 1 (take n data)))
                        (drop n data)])))
          (ffilter (comp empty? second)) first
          (#(if (empty? (first %)) (reverse %) %))
          (map #(IDN/toUnicode (String. (byte-array %))))
          (str/join ".")))
  ([bytes ^long offset ^long length]
     (bytes->name (->> bytes (drop offset) (take length)))))

(deftype DNSDomain [^IPersistentMap meta, ^bytes bytes, ^long length]
  Object
  (toString [this] (bytes->name bytes 0 length))
  (hashCode [this] (bytes-hash-code bytes 0 length))
  (equals [this other]
    (and (domain? other)
         (= length (domain-length other))
         (Arrays/equals bytes ^bytes (domain-bytes other))))

  IObj
  (meta [this] meta)
  (withMeta [this new-meta] (DNSDomain. new-meta bytes length))

  Comparable
  (compareTo [this other]
    (domain-compare true this other))

  ILookup
  (valAt [this key]
    (when (domain-contains? this key) key))
  (valAt [this key default]
    (if (domain-contains? this key) key default))

  IFn
  (invoke [this key]
    (when (domain-contains? this key) key))
  (invoke [this key default]
    (if (domain-contains? this key) key default))

  DNSDomainConstruction
  (domain [this] this)

  DNSDomainOperations
  (domain?* [this] true)
  (domain-bytes [this] bytes)
  (domain-length [this] length))

(def ^:private root-domain
  "The singleton empty root domain."
  (DNSDomain. nil (byte-array []) 0))

(defn- domain*
  "Private bytes->domain factory."
  [orig ^bytes bytes]
  (if (domain?* bytes)
    (DNSDomain. nil bytes (alength bytes))
    (domain-error "%s: invalid domain" (str orig))))

(defn domain-next
  "For the domain child which is a subdomain of domain parent, return the
immediate child domain of parent which is either identical to child or also a
parent domain of child.  Returns nil if there is no such domain.  Uses the
implied empty root domain as the parent if not provided."
  ([child]
     (domain-next child nil))
  ([child parent]
     (let [^bytes bytes (domain-bytes child), length (domain-length parent)]
       (when (< length (domain-length child))
         (DNSDomain. nil bytes (+ length (ubyte (aget bytes length)) 1))))))

(defn domain-ancestors
  "Generate a seq of the all the domains for which the provided domain is a
proper subdomain, starting with the domain's TLD and ending with the domain
itself."
  [domain] (->> (iterate #(domain-next domain %) nil) (drop 1)
                (take-while identity)))

(extend-type (java.lang.Class/forName "[B")
  DNSDomainConstruction
  (domain [this] (domain* this this))

  DNSDomainOperations
  (domain?* [this] (DNSDomainParser/isValid this))
  (domain-bytes [this] this)
  (domain-length [this] (alength ^bytes this)))

(extend-type String
  DNSDomainConstruction
  (domain [this] (domain* this (domain-bytes this)))

  DNSDomainOperations
  (domain?* [this] (domain?* (domain-bytes this)))
  (domain-bytes [this] (name->bytes this))
  (domain-length [this] (inc (.length this))))

(extend-type nil
  DNSDomainConstruction
  (domain [this] root-domain)

  DNSDomainOperations
  (domain?* [this] true)
  (domain-bytes [this] (domain-bytes root-domain))
  (domain-length [this] 0))
