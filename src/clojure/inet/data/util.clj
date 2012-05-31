(ns inet.data.util
  "Utility functions."
  (:import [clojure.lang IFn ILookup IObj Seqable]))

(defmacro ignore-errors
  "Returns the result of evaluating body, or nil if it throws an exception."
  [& body] `(try ~@body (catch java.lang.Exception _# nil)))

;; Copied from clojure/core.clj
(defmacro assert-args [& pairs]
  `(do (when-not ~(first pairs)
         (throw (IllegalArgumentException.
                 (str (first ~'&form) " requires " ~(second pairs) " in "
                      ~'*ns* ":" (:line (meta ~'&form))))))
       ~(let [more (nnext pairs)]
          (when more
            (list* `assert-args more)))))

(defmacro doto-let
  "bindings => binding-form expr

Evaluates expr, and evaluates body with its result bound to the binding-form.
Returns the result of expr."
  ([bindings & body]
     (assert-args
       (vector? bindings) "a vector for its binding"
       (= 2 (count bindings)) "exactly 2 forms in binding vector")
     (let [[bf expr] bindings]
       `(let [value# ~expr]
          (let [~bf value#]
            ~@body value#)))))

(defn ffilter
  "Returns the first item in coll for which (pred item) is true."
  ([pred coll]
     (when-let [coll (seq coll)]
       (let [item (first coll)]
         (if (pred item) item (recur pred (rest coll)))))))

(defmacro case-expr
  "Like case, but only supports individual test expressions, which are
evaluated at macro-expansion time."
  [e & clauses]
  `(case ~e
     ~@(concat
        (mapcat (fn [[test result]]
                  [(eval `(let [test# ~test] test#)) result])
                (partition 2 clauses))
        (when (odd? (count clauses))
          (list (last clauses))))))

(defn longest-run
  "Find the longest run of the value x in the collection coll.  Returns the
pair of the starting index and length on success and nil on failure."
  [x coll]
  (let [runs (->> (partition-by identity coll)
                  (reductions (fn [[_ n pos] s]
                                [(first s) (count s) (+ pos n)])
                              [nil 0 0])
                  (drop 1)
                  (filter #(= x (first %))))]
    (when (seq runs)
      (let [[_ n pos] (apply max-key second runs)]
        [pos n]))))

(defn ubyte
  "Unsigned number represented by a byte value."
  {:inline (fn [x] `(bit-and 0xff (long ~x)))}
  (^long [^long x] (bit-and 0xff x)))

(defn sbyte
  "Signed byte representation of an unsigned integral value."
  {:inline (fn [x] `(byte (let [x# ~x] (if (> x# 127) (- x# 256) x#))))}
  ([x] (byte (if (> x 127) (- x 256) x))))

(defn bytes-hash-code
  "Calculate a hash code for a portion of a byte array."
  (^long [^bytes bytes]
     (bytes-hash-code bytes 0 (alength bytes) 0))
  (^long [^bytes bytes ^long initial]
     (bytes-hash-code bytes 0 (alength bytes) initial))
  (^long [^bytes bytes ^long offset ^long length]
     (bytes-hash-code bytes offset length 0))
  (^long [^bytes bytes ^long offset ^long length ^long initial]
     (let [terminal (+ offset length)]
       (loop [result (int initial), i offset]
         (if (>= i terminal)
           result
           (let [x (int (aget bytes i))]
             (recur (->> result
                         (unchecked-multiply-int (int 31))
                         (unchecked-add-int x))
                    (inc i))))))))
