(ns inet.data.dns-test
  (:require [inet.data.dns :as dns])
  (:use [clojure.test]))

(deftest test-dns-contains?
  (is (dns/domain-contains? nil "example.com")
      "Root domain contains everything")
  (is (dns/domain-contains? "com" "com") "Domain contains itself")
  (is (dns/domain-contains? "com" "example.com") "TLD contains immediate child")
  (is (dns/domain-contains? "com" "www.example.com") "TLD contains descendent")
  (is (not (dns/domain-contains? "com" "example.net"))
      "TLD does not contain non-descedent")
  (is (not (dns/domain-contains? "example.com" "wwwexample.com"))
      "Domain does not contain purely lexicographic suffix"))

(deftest test-dns-subdomain?
  (is (dns/domain-subdomain? nil "example.com")
      "Root domains has everything as a subdomain")
  (is (not (dns/domain-subdomain? "com" "com"))
      "Domain is not a subdomain of self")
  (is (dns/domain-subdomain? "com" "example.com")
      "TLD has children as subdomains")
  (is (dns/domain-subdomain? "com" "www.example.com")
      "TLD has descendants as subdomains")
  (is (not (dns/domain-subdomain? "com" "example.net"))
      "TLD does not have non-descendants as subdomains")
  (is (not (dns/domain-subdomain? "example.com" "wwwexample.com"))
      "Domain does not have purely lexicographic suffixes as subdomains"))

