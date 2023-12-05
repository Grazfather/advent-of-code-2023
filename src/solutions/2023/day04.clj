^{:nextjournal.clerk/visibility :hide-ns}
(ns solutions.2023.day04
  {:nextjournal.clerk/toc true}
  (:require [clojure.java.io :as io]
            [util :as u]
            [nextjournal.clerk :as clerk]
            [clojure.set :as set]
            [clojure.string :as str]
            ))

;; # Problem
{:nextjournal.clerk/visibility {:code :hide :result :show}}
(clerk/html (u/load-problem "04" "2023"))
{:nextjournal.clerk/visibility {:code :show :result :hide}}

;; # Solution
;;
;; First things first, let's load our input and parse it
(def input (->> (slurp (io/resource "inputs/2023/day04.txt"))
                str/split-lines))
(def sample-input (->> "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11"
                       str/split-lines))
{:nextjournal.clerk/visibility {:code :show :result :hide}}

; It's pretty straight forward to get a list of matches per game: Just collect the two sets of numbers, count the size
; of the overlapping set, and use that to calculate the score for that game.
(defn solve [input score-fn]
  (->> input
       (map (fn [line]
              (let [parts (str/split line #" (?<!\\)\| ")
                    winners (as-> (first parts) p
                              (str/split p #":")
                              (second p)
                              (re-seq #"(\d+)" p)
                              (map second p)
                              (set p))
                    mine (as-> (second parts) p
                              (re-seq #"(\d+)" p)
                              (map second p)
                              (set p))
                    num-matches (count (set/intersection winners mine))]
                num-matches)))
       score-fn))

;; ## Part 1
; For part 1 we score simply by scoring each line and adding up the lines.
(defn part-1 [input]
  (solve input
         (fn [matches] (->> matches
                            (map #(if (zero? %)
                                    0
                                    (reduce * (repeat (dec %) 2))))
                            (apply +)))))

{:nextjournal.clerk/visibility {:code :show :result :show}}
(part-1 sample-input)
(part-1 input)

;; ## Part 2
; Part 2 is trickier. We score by reducing over the list of matches, keep a list of copies remaining and totals for each
; line
{:nextjournal.clerk/visibility {:code :show :result :hide}}
(defn part-2 [input]
  (solve input (fn [matches]
                 (as-> matches ms
                   (reduce (fn [[cards copies] matches]
                             (let [total (inc (first copies)) ]
                               [(conj cards total)
                                (map #(+ %1 %2) (drop 1 copie) (concat (repeat matches total)
                                                                        (repeat (- (count copies) matches) 0)))]))
                           [[]
                            (repeat (count ms) 0)]
                           ms)
                   (apply + (first ms)) ))))

{:nextjournal.clerk/visibility {:code :show :result :show}}
(part-2 sample-input)
(part-2 input)
