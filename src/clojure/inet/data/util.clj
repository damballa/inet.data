(ns inet.data.util
  "Utility functions."
  (:import [clojure.lang IFn ILookup IObj Seqable]))

(defmacro ignore-errors
  "Returns the result of evaluating body, or nil if it throws an exception."
  [& body] `(try ~@body (catch java.lang.Exception _# nil)))

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
  {:inline (fn [x] `(bit-and 0xff ~x))}
  (^long [x] (bit-and 0xff x)))

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

(deftype HierSet [meta contains? contents parents]
  ;; meta - the instance's IObj metadata
  ;; contains? - function for testing if one entity contains another
  ;; contents - the sorted set of the HierSet's members
  ;; parents - map of members to their immediate parent members

  Object
  (toString [this]
    (str contents))

  IObj
  (meta [this] meta)
  (withMeta [this meta]
    (HierSet. meta contains? contents parents))

  ILookup
  (valAt [this key]
    (let [sibling (first (rsubseq contents <= key))
          ancestors-of (fn ancestors-of [k]
                         (when k (cons k (lazy-seq (ancestors-of (parents k))))))
          not-ancestor? (fn [k] (not (contains? k key)))]
      (->> (ancestors-of sibling) (drop-while not-ancestor?) seq)))
  (valAt [this key not-found]
    (or (.valAt this key) not-found))

  IFn
  (invoke [this key]
    (get this key))
  (invoke [this key not-found]
    (get this key not-found))

  Seqable
  (seq [this] (seq contents)))

(defn hier-set-by
  "As hier-set, but specifying the comparator to use for element comparison."
  [contains? comparator & keys]
  (letfn [(find-parent [[parents ancestors] key]
            (let [not-ancestor? (fn [k] (not (contains? k key)))
                  ancestors (drop-while not-ancestor? ancestors)]
              [(assoc parents key (first ancestors)) (cons key ancestors)]))]
    (let [contents (apply sorted-set-by comparator keys)
          parents (first (reduce find-parent [{} ()] contents))]
      (HierSet. nil contains? contents parents))))

(defn hier-set
  "Constructs a set in which the elements are both linearly sorted and may
hierarchically contain subsequent elements.  The contains? predicate defines
the hierachical relationship, with the following two constraints: (a) elements
must sort prior to any elements they contain; and (b) an ancestor element must
contain all elements which sort between it and any descendant element.

Lookup in the set returns a seq of all in-set ancestors of the provided key, or
nil if the provided key is not a descendant of any set member."
  [contains? & keys] (apply hier-set-by contains? compare keys))
