;; Parses a text version of this hymn cross reference: 
;; https://www.churchofjesuschrist.org/bc/content/shared/english/pdf/callings/music/Hymns_000_English_HymnCrossReference_eng.pdf?lang=eng&download=true
;; Into a CSV that can be imported into sqlite

(ns parse
  (:require [clojure.string :as str]))

(defn format-line [row]
  (let [nums (re-seq #"\d+" row)
        names (map #(str "\"" % "\"") (str/split row #" ?\d+ ?"))
        tog (map vector nums names)
        en-es (take 2 tog)
        hymn-pairs (map (partial str/join ",") en-es)
        all-hymns (str/join "," hymn-pairs)]
    all-hymns))

(defn format-lines [acc row]
  (str acc "\n" (format-line row)))

(def raw (slurp "/home/jaketrent/Downloads/enEsHymns.txt"))

(def rows (str/split-lines raw))

(def lines (reduce format-lines "" rows))

(def csv (str "english_num,english_name,spanish_num,spanish_name" lines))

(spit "/home/jaketrent/Downloads/enEsHymns.csv" csv)
