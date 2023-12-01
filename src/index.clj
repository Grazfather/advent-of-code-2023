(ns index
  {:nextjournal.clerk/visibility {:code :hide :result :hide}}
  (:require
   [babashka.fs :as fs]
   [nextjournal.clerk :as clerk]
   [nextjournal.clerk.view :as clerk.view]
   [clojure.java.io :as io]
   [clojure.string :as str]))

(alter-var-root #'clerk.view/include-css+js
                (fn [include-css+js-orig extra-includes]
                  (fn [state]
                    (concat (include-css+js-orig state)
                            extra-includes)))
                (list [:style#extra-styles (slurp (clojure.java.io/resource "style.css"))]))

(defn- build-paths []
  (-> "src/solutions"
      (fs/list-dir "*.clj")
      sort))
;
{:nextjournal.clerk/visibility {:result :show}}
^::clerk/no-cache
(clerk/html
  ; "hello"
 ; (map str (build-paths))
 (into [:div]
       (map (fn [path]
              [:a {:href (-> path
       ;                      (str/replace ".clj" "")
                             clerk/doc-url)} path])
            (map str (build-paths))))
 )
