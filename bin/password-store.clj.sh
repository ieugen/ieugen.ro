#!/usr/bin/env bb
(ns script
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [cheshire.core :as json]
            [clojure.data.csv :as csv]
            [clojure.java.shell :refer [sh]]))

;; export BABASHKA_CLASSPATH= "$(clojure -Sdeps '{:deps {clojure-csv {:mvn/version " RELEASE "}}}' -Spath)"

;;
;; Iterez peste directoare / fișiere
;; pass pe fiecare
;; Creez un csv pentru KeePass
;;
;; https://github.com/borkdude/babashka
;; https://book.babashka.org/#style
;; https://book.babashka.org/#child_processes
;; http://clojure-doc.org/articles/cookbooks/files_and_directories.html
;; https://clojuredocs.org/clojure.core/file-seq
;; https://github.com/borkdude/babashka/blob/master/doc/projects.md
;;
;; https://keepassxc.org/docs/KeePassXC_UserGuide.html#_importing_csv_file
;;

(def home (System/getProperty "user.home"))
(def pstore-dir (str home "/.password-store/"))

(defn process-password-store [pstore-dir]
  (let [files (file-seq (io/file pstore-dir))
        gpg-filter (fn gpg-filter [f] (.contains (.toString f) ".gpg"))
        gpg-files (filter gpg-filter files)]
    ;; process all files - read the contents and produce a list of entries
    (for [f gpg-files
          :let [f-str (.toString f)
                pass-arg (-> f-str
                             (.substring (.length pstore-dir))
                             (.replace ".gpg" ""))
                data (sh "/usr/bin/pass" pass-arg)
                lines (str/split-lines (:out data))
                pw (first lines)
                other (rest lines)]]
      {:file f-str
       :data data
       :out (:out data)
       :pass-arg pass-arg
       :password pw
       :other other})))


(defn pwstore-edn->csv-table [elements]
  (let [convert (fn convert [e] (let [{:keys [password pass-arg other]} e
                                      notes (str/join "||" other)]
                                  ["pass" password pass-arg notes]))]
    (map convert elements)))


;; Commend and uncommend the lines below

(println "Processing files in " pstore-dir)

(def processed (process-password-store pstore-dir))

(spit "password-store-export.edn" (with-out-str (pp/pprint processed)))
(spit "password-store-export.json" (with-out-str (json/generate-string processed)))

(println "Done extracting files. Convert edn file to csv.")

(def csv-data (pwstore-edn->csv-table (edn/read-string (slurp "password-store-export.edn"))))

(with-open [writer (io/writer "password-store-export.csv")]
  (csv/write-csv writer csv-data :quote? true))
