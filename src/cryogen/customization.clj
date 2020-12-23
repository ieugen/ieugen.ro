(ns cryogen.customization)

(defn extend-params [params {:keys [pages posts] :as other-pages}]
  (let [tag-count (->> (:posts-by-tag other-pages)
                       (map (fn [[k v]] [k (count v)]))
                       (into {}))
        tags-with-count (update
                         params :tags
                         #(map (fn [t] (assoc t :count (tag-count (:name t)))) %))
        new-site-data (assoc tags-with-count :posts posts :pages pages)]
    ;; (spit "params.edn" (with-out-str (clojure.pprint/pprint params)))
    ;; (spit "site-data.edn" (with-out-str (clojure.pprint/pprint other-pages)))
    ;; (spit "merged.edn" (with-out-str (clojure.pprint/pprint new-site-data)))
    new-site-data))

(comment

  (let [params {:a 1 :b 2 :c {:a 33 :b 55}}
        site-data {:x 3 :y 4 :c {:z 99}}]
    (->> (update params :tags (fn [_] {:t 1 :z 2}))
         (merge site-data)))

  0)
