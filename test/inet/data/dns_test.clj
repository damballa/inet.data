(ns inet.data.dns-test
  (:require [inet.data.dns :as dns])
  (:use [clojure.test]))

(deftest test-domain?
  (testing "String domains"
    (is (dns/domain? "www.foobar.com") "Accepts valid string domains")
    (is (dns/domain? "this-is-a-valid-domain-even-though-it-has-a-quite-loooong-label.com")
        "Accepts domains with the maximum label length")
    (is (not (dns/domain? "this-is-an-invalid-domain-due-to-having-an-overlong-label-by-one.com"))
        "Rejects domains with overlong labels")
    (is (not (dns/domain? "this-is-an-invalid-domain-because-it-has-an-overlong-label-by-two.com"))
        "Rejects domains with even overlong-er labels"))
  (testing "Byte domains"
    (is (dns/domain? (byte-array (map byte [3 64 64 64 3 99 111 109])))
        "Accepts valid byte domains")
    (is (not (dns/domain? (byte-array (map byte [3 64 64 64 3 99 111]))))
        "Rejects domains which end mid-label")))

(deftest test-domain-roundtrip
  (testing "Round-tripping"
    (is (= "www.example.com" (-> "www.example.com" dns/domain str))
        "Fully ASCII domain names are identical")
    (is (= "www.ExaMple.com" (-> "www.ExaMple.com" dns/domain str))
        "Mixed-case ASCII domain names are identical")
    (is (= "www.xn--exmple-xta.com" (-> "www.exâmple.com" dns/domain str))
        "IDNs are left Punycode-encoded")
    (is (= "www.exâmple.com" (-> "www.exâmple.com" dns/domain dns/idn-str))
        "Explicit IDN string form decodes Punycode")))

(deftest test-domain-compare
  (testing "Domain comparison"
    (testing "Identical domains compare equal"
      (is (= 0 (dns/domain-compare "example.com" "example.com")))
      (is (= 0 (compare (dns/domain "example.com")
                        (dns/domain "example.com")))))
    (let [dom (dns/domain "example.com")]
      (is (not= 0 (dns/domain-compare dom (dns/domain-next dom nil)))
          "Differing domains do not compare as equal")
      (is (= 0 (dns/domain-compare dom (dns/domain-next dom "com")))
          "Equal derived domains do compare as equal"))
    (testing "Case sensitivity"
      (testing "Case-differing domains compare as equal"
        (is (= 0 (dns/domain-compare "example.com" "eXaMpLe.com")))
        (is (= 0 (compare (dns/domain "example.com")
                          (dns/domain "eXaMpLe.com")))))
      (is (not= (dns/domain "example.com") (dns/domain "eXaMpLe.com"))
          "Case-differing domains are not equal"))))

(deftest test-domain-contains?
  (is (dns/domain-contains? nil "example.com")
      "Root domain contains everything")
  (is (dns/domain-contains? "com" "com") "Domain contains itself")
  (is (dns/domain-contains? "com" "example.com") "TLD contains immediate child")
  (is (dns/domain-contains? "com" "www.example.com") "TLD contains descendent")
  (is (not (dns/domain-contains? "com" "example.net"))
      "TLD does not contain non-descedent")
  (is (not (dns/domain-contains? "example.com" "wwwexample.com"))
      "Domain does not contain purely lexicographic suffix"))

(deftest test-domain-subdomain?
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

(deftest test-domain-utility
  (is (= '("www" "example" "com") (dns/domain-labels "www.example.com"))
      "Turn domain into sequence of labels.")
  (is (= (-> "www.google.com" dns/domain dns/domain-parent)
         (-> "google.com" dns/domain))
      "Get immediate parent of domain."))
