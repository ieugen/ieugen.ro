(ns cryogen.core
  (:require [cryogen-core.compiler :refer [compile-assets-timed]]
            [cryogen-core.plugins :refer [load-plugins]]))

(defn -main []
  (load-plugins)
  (compile-assets-timed {:extend-params-fn
                         (fn extend-params [params site-data]
                           (let [tag-count (->> (:posts-by-tag site-data)
                                                (map (fn [[k v]] [k (count v)]))
                                                (into {}))]
                             (update
                              params :tags
                              #(map (fn [t] (assoc t
                                                   :count (tag-count (:name t))))
                                    %))))})
  (System/exit 0))
