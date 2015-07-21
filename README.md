# inet.data

[![Build Status](https://secure.travis-ci.org/damballa/inet.data.png)](http://travis-ci.org/damballa/inet.data)

Inet.data is a library for modeling various Internet-related conceptual
entities as *data*, supporting applications which are *about* the modeled
entities versus *interfacing* with them.

## Installation

Inet.data is available on Clojars.  Add this `:dependency` to your Leiningen
`project.clj`:

```clj
[com.damballa/inet.data "0.5.7"]
```

Please note that the group ID has changed with with the 0.5.7 release from
`inet.data` to `com.damballa`.  New releases will also be pushed under the
`inet.data` group ID until 0.6.0, but that group ID should be considered
deprecated.

## Usage

Currently inet.data includes support for IP addresses and networks and for DNS
domain names.  Example usage follows; [detailed API
documentation](http://damballa.github.com/inet.data/) available.

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

(seq (ip/network "192.168.0.0/30"))
;;=> (#ip/address "192.168.0.0"
;;    #ip/address "192.168.0.1"
;;    #ip/address "192.168.0.2"
;;    #ip/address "192.168.0.3")

(ip/network-nth "192.168.0.0/30" -1)
;;=> #ip/address "192.168.0.3"

(ip/address-networks "192.168.0.0" "192.168.0.4")
;;=> #{#ip/network "192.168.0.0/30"
;;     #ip/network "192.168.0.4/32"}
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

(dns/domain-parent "www.example.com") ;;=> #dns/domain "example.com"

(let [gtlds (dns/domain-set "com" "net" "org")]
  (get gtlds "example.com") ;;=> (#dns/domain "com")
  (get gtlds "does.not.exist") ;;=> nil
  )
```

### inet.data.format.psl

The `inet.data.format.psl` namespace defines functions for working with files
in the Mozilla Public Suffix List format.  It can automatically use the current
version of the list as maintained by the Mozilla project.  The format is
generally useful for domain suffix applications, but most applications will
need to provide their own list(s) customized for their particular use cases.

```clj
(require '[inet.data.format.psl :as psl])

(psl/lookup "www.example.co.uk") ;;=> #dns/domain "example.co.uk"
```

## License

Copyright © 2012-2014 Marshall Bockrath-Vandegrift & Damballa, Inc.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
