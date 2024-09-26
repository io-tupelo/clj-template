(ns tst.demo.core
  (:use demo.core
        tupelo.core
        tupelo.test)
  (:require
    [clojure.data :as data]
    [schema.core :as s]
    [tupelo.schema :as tsk]
    [tupelo.string :as str]))

(verify
  (is= 5 (+ 2 3))
  )

