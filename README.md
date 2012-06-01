# inet.data

Inet.data is a library for modeling various Internet-related conceptual
entities as *data*, supporting applications with are *about* the modeled
entities versus *interfacing* with them.

## Installation

Inet.data is available on Clojars.  Add this `:dependency` to your Leiningen
`project.clj`:

```clj
[inet.data "0.3.0"]
```

## Usage

Currently inet.data includes support for IP addresses and networks and for DNS
domain names.

### inet.data.ip

The `inet.data.ip` namespace defines types for IP addresses and networks and
associated functions.  All public functions work in terms of a protocol which
is also implemented for strings, byte arrays, and `java.net.InetAddress`.

```clj
(require '[inet.data.ip :as ip])

(ip/network-contains? "192.168.1.0/24" "192.168.1.1") ;;=> true

(ip/address? "600d::") ;;=> true
(ip/address? "::bad::") ;;=> false

(let [rfc1918 (ip/network-set "10.0.0.0/8" "172.16.0.0/12" "192.168.0.0/16")]
  (get rfc1918 "10.31.33.7") ;;=> (#ip/network "10.0.0.0/8")
  (get rfc1918 "8.8.8.8") ;;=> nil
  )
```

### inet.data.dns

The `inet.data.dns` namespace defines a type for representing DNS domain names
and associated functions.  All public functions work in terms of a protocol
which is also implemented for strings and byte arrays.

```clj
(require '[inet.data.dns :as dns])

(dns/domain-contains? "com" "example.com") ;;=> true

(dns/domain? "example.com") ;;=> true
(dns/domain? "bad..com") ;;=> false

(let [gtlds (dns/domain-set "com" "net" "org")]
  (get gtlds "example.com") ;;=> (#dns/domain "com")
  (get gtld "does.not.exist") ;;=> nil
  )
```

The `inet.data.dns.psl` namespace defines functions for working with files in
the Mozilla Public Suffix List format.  It can automatically use the current
version of the list as maintained by the Mozilla project.  The format is
generally useful for domain suffix applications, but most applications will
need to provide their own list(s) customized for their particular use cases.

```clj
(require '[inet.data.dns.psl :as psl])

(psl/lookup "www.example.co.uk") ;;=> #dns/domain "example.co.uk"
```

## License

Copyright (C) 2012 Marshall T. Vandegrift

Distributed under the Eclipse Public License, the same as Clojure.
