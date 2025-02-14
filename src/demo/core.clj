(ns demo.core
  (:use tupelo.core)
  (:require
    [schema.core :as s])
  (:gen-class))

(s/defn add2 :- s/Num
  "An example to demonstrate Plumatic Schema."
  [x :- s/Num
   y :- s/Num]
  (+ x y))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (spy :main--enter)
  (spyx args)
  (spy :main--leave))

(comment

  (add2 3 4)

  )