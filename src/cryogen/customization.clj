(ns cryogen.customization)

(defn extend-params [params site-data]
  (let [tag-count (->> (:posts-by-tag site-data)
                       (map (fn [[k v]] [k (count v)]))
                       (into {}))]
    ; (spit "params.txt" (prn-str site-data))
    (->> (update
          params :tags
          #(map (fn [t] (assoc t :count (tag-count (:name t)))) %))
         (merge site-data))))


(comment

  (let [params {:a 1 :b 2}
        site-data {:x 3 :y 4}]
    (->> (update params :tags (fn [_] {:t 1 :z 2}))
         (merge site-data)))

  0)
