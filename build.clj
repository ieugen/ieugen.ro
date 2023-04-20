(ns build
  (:require [clojure.tools.build.api :as b]
            [cryogen.core :as c]))

(def version (format "0.0.%s" (b/git-count-revs nil)))

(defn clean [_]
  (b/delete {:path "public"}))

(defn site [_]
  (println "Cryogen website")
  (c/build-site)
  (println "Copy files")
  (b/copy-dir {:src-dirs ["node_modules/cookieconsent/build"]
               :target-dir "public/vnd/cookieconsent"}))
