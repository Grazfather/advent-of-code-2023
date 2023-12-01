^{:nextjournal.clerk/visibility :hide-ns}
(ns solutions.2023.day01
  {:nextjournal.clerk/toc true}
  (:require [clojure.java.io :as io]
            [util :as u]
            [nextjournal.clerk :as clerk]
            [clojure.string :as str]))

;; # Problem
{:nextjournal.clerk/visibility {:code :hide :result :show}}
(clerk/html (u/load-problem "01" "2023"))
{:nextjournal.clerk/visibility {:code :show :result :show}}

;; # Solution
;;
;; First things first, let's load our input and parse it
(def input (->> (slurp (io/resource "inputs/2023/day01.txt")) ;; Load the resource
                str/split-lines))                             ;; Split into lines
{:nextjournal.clerk/visibility {:result :hide}}

{:nextjournal.clerk/visibility {:code :hide :result :hide}}
(def sample-input1 (->>  "1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet"
                       (str/split-lines)))
(def sample-input2 (->> "two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen"
                       (str/split-lines)))

{:nextjournal.clerk/visibility {:code :show :result :hide}}
;; We need a way to map written out digits to digits. We map to strings since we parse later.
(def digits {"zero" "0"
             "one" "1"
             "two" "2"
             "three" "3"
             "four" "4"
             "five" "5"
             "six" "6"
             "seven" "7"
             "eight" "8"
             "nine" "9"})

;; We do a best-effort word->digit mapping. If the token doesn't match, then it should be a digit itself.
(defn token->digit [t]
  (-> (or (digits t) t)
      Integer/parseInt))

;; ## Solve
;; Solve function works for both parts
(defn solve [input regex]
  (->> input
       (map #(->> (clojure.core/re-seq regex %)
                  (map second) ; First group is empty
                  (map token->digit)
                  ((fn [t] (str (first t) (last t))))
                  Integer/parseInt))
       (apply +)))

;; ## Part 1
;; For part 1 we just collect digits
{:nextjournal.clerk/visibility {:code :show :result :hide}}
(defn solve-part1 [input]
  (solve input #"(\d)"))

{:nextjournal.clerk/visibility {:code :show :result :show}}
(solve-part1 input)

;; ## Part 2
;; For part 2 we adjust our regex to tokenize on single digits plus any written out digit
{:nextjournal.clerk/visibility {:code :show :result :hide}}
(defn solve-part2 [input]
  (solve input (re-pattern (str "(?=(\\d|" (str/join "|" (keys digits)) "))"))))

{:nextjournal.clerk/visibility {:code :show :result :show}}
(solve-part2 input)
