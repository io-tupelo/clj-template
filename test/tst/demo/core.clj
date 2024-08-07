(ns tst.demo.core
  (:use demo.core
        tupelo.core
        tupelo.test)
  (:require
    [schema.core :as s]
    [tupelo.schema :as tsk]
    [tupelo.string :as str]))

(verify
  (is= 5 (+ 2 3))
  )


(comment

  (s/defn parse-coloring :- tsk/KeyMap
    [coloring :- s/Str]
    (let [tokens (clojure.string/split coloring #" ")
          color  (second tokens)
          value  (Integer/parseInt (first tokens))]
      {:color color :value value}))

  (verify
    (is= (parse-coloring "1 green")
      {:color "green" :value 1}))

  (s/defn parse-draw :- [tsk/KeyMap]
    [draw :- s/Str]
    (let [colorings (clojure.string/split draw #", ")]
      (mapv parse-coloring colorings)))

  (verify
    (is= (parse-draw "1 green, 1 blue, 1 red")
      [{:color "green" :value 1}
       {:color "blue" :value 1}
       {:color "red" :value 1}]))

  (s/defn parse-game :- tsk/KeyMap
    [line :- s/Str]
    (let [matches (re-matches #"^Game (\d+): (.*)$" line)
          game-id (Integer/parseInt (nth matches 1))
          game    (nth matches 2)
          draws   (clojure.string/split game #"; ")]
      {:id game-id :draws (mapv parse-draw draws)}))

  (verify
    (is= (parse-game "Game 1: 1 green, 1 blue, 1 red; 11 red, 3 blue")
      {:id    1
       :draws [[{:color "green" :value 1}
                {:color "blue" :value 1}
                {:color "red" :value 1}]
               [{:color "red" :value 11}
                {:color "blue" :value 3}]]}))

  (s/defn separate-games :- [s/Str]
    "Parse a multi-line text string into a seq of 1-line game strings"
    [text :- s/Str]
    (mapv str/trim (str/split-lines (str/trim text))))

  (defn input-text->game-data
    [text]
    (let [game-strs    (separate-games text)
          games-parsed (mapv parse-game game-strs)]
      games-parsed))

  (verify
    (let [; throw in some gratuitous leading/trailing newlines to check robustness
          input         "

            Game 1: 1 green, 1 blue, 1 red; 11 red, 3 blue
            Game 2: 9 green, 9 blue, 1 red; 11 red, 3 blue; 1 blue, 18 red

    "
          game-strs     (separate-games input)
          strs-expected ["Game 1: 1 green, 1 blue, 1 red; 11 red, 3 blue"
                         "Game 2: 9 green, 9 blue, 1 red; 11 red, 3 blue; 1 blue, 18 red"]]
      (is= game-strs strs-expected)
      (let [games-parsed (input-text->game-data input)]
        (is= games-parsed
          [{:draws
            [[{:color "green", :value 1}
              {:color "blue", :value 1}
              {:color "red", :value 1}]
             [{:color "red", :value 11} {:color "blue", :value 3}]],
            :id 1}
           {:draws
            [[{:color "green", :value 9}
              {:color "blue", :value 9}
              {:color "red", :value 1}]
             [{:color "red", :value 11} {:color "blue", :value 3}]
             [{:color "blue", :value 1} {:color "red", :value 18}]],
            :id 2}]))))

  )
