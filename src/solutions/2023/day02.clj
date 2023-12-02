^{:nextjournal.clerk/visibility :hide-ns}
(ns solutions.2023.day02
  {:nextjournal.clerk/toc true}
  (:require [clojure.java.io :as io]
            [util :as u]
            [nextjournal.clerk :as clerk]
            [clojure.string :as str]))

;; # Problem
{:nextjournal.clerk/visibility {:code :hide :result :show}}
(clerk/html (u/load-problem "02" "2023"))
{:nextjournal.clerk/visibility {:code :show :result :hide}}

;; # Solution
;;
;; First things first, let's load our input and parse it
(def input (->> (slurp (io/resource "inputs/2023/day02.txt"))
                str/split-lines))

(def sample-input (->> "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"
                       str/split-lines))

;; ## Part 1
(def constraints {"red" 12
                  "green" 13
                  "blue" 14})

;; The key realization is that the rounds don't matter at all. We just need a list of colour:count pairs.
(defn parse-game [game]
  (->> game
       (re-seq #"\d+|red|green|blue")
       rest
       (partition 2)
       (map reverse)
       (map vec)))

;; Then we don't need to check each pull, but instead each game
(defn possible? [game]
  (every? (fn [[col cnt]] (<= (Integer/parseInt cnt) (constraints col))) game))

(defn part-1 [input]
  (->> input
       (map #(->> %
                  parse-game
                  possible?))
       ; Add up the ID of each possible game
       (#(reduce-kv (fn [acc id v]
                      (+ acc (if v
                               (inc id)
                               0)))
                    0 (vec %)))))

(assert (= (part-1 sample-input) 8))
{:nextjournal.clerk/visibility {:code :show :result :show}}
(part-1 input)

;; ## Part 2
{:nextjournal.clerk/visibility {:code :show :result :hide}}
(defn part-2 [input]
  (->> input
       (map #(->> %
                  parse-game
                  ; Get max for each colour
                  (reduce (fn [acc [col cnt]]
                            (merge acc {col (max (Integer/parseInt cnt) (acc col))}))
                          {"red" 0 "green" 0 "blue" 0})
                  ; "power"
                  vals
                  (apply *)))
       (reduce +)))

(assert (= (part-2 sample-input) 2286))
{:nextjournal.clerk/visibility {:code :show :result :show}}
(part-2 input)
