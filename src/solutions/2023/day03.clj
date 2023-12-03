^{:nextjournal.clerk/visibility :hide-ns}
(ns solutions.2023.day03
  {:nextjournal.clerk/toc true}
  (:require [clojure.java.io :as io]
            [util :as u]
            [nextjournal.clerk :as clerk]
            [clojure.string :as str]
            [clojure.set :as set]))

;; # Problem
{:nextjournal.clerk/visibility {:code :hide :result :show}}
(clerk/html (u/load-problem "03" "2023"))
{:nextjournal.clerk/visibility {:code :show :result :hide}}

;; # Solution
;;
;; First things first, let's load our input and parse it
(def input (->> (slurp (io/resource "inputs/2023/day03.txt"))
                str/split-lines))
(def sample-input (->> "467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598.."
                       str/split-lines))
{:nextjournal.clerk/visibility {:code :show :result :hide}}

;; ## Part 1
; This function returns a list of matches, with start, end, and the match itself
(defn locate-matches [pattern string]
  (let [matcher (re-matcher pattern string)]
    (loop [matches []]
      (if (.find matcher)
        (recur (conj matches
                     {:match (.group matcher)
                      :start (.start matcher)
                      :end (.end matcher)}))
        matches))))

; Get the eight neighbours of any point
(defn neighbours [[x y]]
  (for [dx [-1 0 1]
        dy [-1 0 1]
        :let [nx (+ x dx) ny (+ y dy)]
        :when (not (and (zero? dx) (zero? dy)))]
    [nx ny]))

; See if a point is a neighbour of another. Takes a coordinate and a list of coordinates, since a number can span
; multiple
(defn neighbour? [c1 cs]
  (not (empty? (set/intersection (set (neighbours c1)) (set cs)))))

(defn part-1
  [input]
  (let [num-coords (->> input
                        (map (fn [line]
                               ; Get all numbers on this line, with the x values it spans
                               (->> (locate-matches #"\d+" line)
                                    (map (fn [match] [(Integer/parseInt (match :match)) (range (match :start) (match :end))]))))))
        ; Convert it to a list of [num coords] where coords is a list of the coords the number spans
        values (for [[y num-lines] (map vector (range) num-coords)
                     [num xs] num-lines]
                 [num (for [x xs] [x y])])
        symbol-coords (->> input
                           (map (fn [line]
                                  (->> (locate-matches #"[\\*\\$\\+/&#%@=-]" line)
                                       (map #(% :start) )))))
        symbol-coords (for [[y symbol-coords-line] (map vector (range) symbol-coords)
                            x symbol-coords-line] [x y])
        ; Get all part nums that are in a valid coord
        valid-nums (for [symbol-coord symbol-coords
                         [num coords] values
                         :when (neighbour? symbol-coord coords)]
                     num)]
    ; Add up the part numbers
    (apply + valid-nums)))

{:nextjournal.clerk/visibility {:code :show :result :show}}
(part-1 sample-input)
(part-1 input)

{:nextjournal.clerk/visibility {:code :hide :result :hide}}
(assert (= (neighbours [1 1]) [[0 0] [0 1] [0 2] [1 0] [1 2] [2 0] [2 1] [2 2]]))
(assert (= (neighbour? [0 0] [[1 1]]) true))
(assert (= (neighbour? [0 0] [[1 1] [0 1]]) true))
(assert (= (neighbour? [0 0] [[2 1] [3 1]]) false))

;; ## Part 2
{:nextjournal.clerk/visibility {:code :show :result :hide}}
(defn part-2
  [input]
  (println "Part 2"))

{:nextjournal.clerk/visibility {:code :show :result :show}}
(part-2 input)
