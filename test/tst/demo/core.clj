(ns tst.demo.core
  (:use demo.core tupelo.core tupelo.test)
  (:require
    [tupelo.string :as str]
    ))


(verify
    (is= 5 (add2 2 3)) ; call a function from demo.core
    (isnt= 99 (+ 2 3)) ; do a local calculation
    (is (string? "hello")) ; call a predicate function & verify

    (throws? (/ 1 0))
    (throws-not? (+ 2 3))
    )

