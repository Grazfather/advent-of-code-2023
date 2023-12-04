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

; Our solve function produces a list of symbol coordinates and values with a list of their coordinates. Each part takes
; these and uses them to filter for valid values.
(defn solve [input filter-fn]
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
        vals (filter-fn symbol-coords values)]
    ; Add up the part numbers
    (apply + vals)))

;; ## Part 1

; For part 1 we just want to filter all the numbers by the ones touching a symbol.
(defn part-1
  [input]
  (solve input #(for [symbol-coord %1
                      [num coords] %2
                      :when (neighbour? symbol-coord coords)]
                  num)))

{:nextjournal.clerk/visibility {:code :show :result :show}}
(part-1 sample-input)
(part-1 input)

{:nextjournal.clerk/visibility {:code :hide :result :hide}}
(assert (= (neighbours [1 1]) [[0 0] [0 1] [0 2] [1 0] [1 2] [2 0] [2 1] [2 2]]))
(assert (= (neighbour? [0 0] [[1 1]]) true))
(assert (= (neighbour? [0 0] [[1 1] [0 1]]) true))
(assert (= (neighbour? [0 0] [[2 1] [3 1]]) false))

;; ## Part 2
; It seems that we don't need to check for gears, as it's the only symbol that will touch two numbers. We just filter
; for symbols that have exactly two neighbours and multiply them.
{:nextjournal.clerk/visibility {:code :show :result :hide}}
(defn part-2
  [input]
  (solve input (fn [symbol-coords values]
                 (->> symbol-coords
                      (map (fn [gear-coord]
                             (for [[num coords] values
                                   :when (neighbour? gear-coord coords)]
                               num)))
                      (filter #(= (count %) 2))
                      (map #(apply * %))))))

{:nextjournal.clerk/visibility {:code :show :result :show}}
(part-2 sample-input)
(part-2 input)
