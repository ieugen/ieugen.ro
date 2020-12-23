(ns cryogen.core
  (:require [cryogen-core.compiler :refer [compile-assets-timed]]
            [cryogen-core.plugins :refer [load-plugins]]
            [cryogen.customization :as c]))

(defn -main []
  (load-plugins)
  (compile-assets-timed {:extend-params-fn c/extend-params})
  (System/exit 0))


(comment

  ;; This does not print hellprrr
  ;; (compile-assets-timed {:extend-params-fn extend-params})

  ;; This does print hellp
  (compile-assets-timed {:extend-params-fn  (fn extend-params [params site-data]
                                              (println "hellp")
                                              params)})

  (-main)

  0)
