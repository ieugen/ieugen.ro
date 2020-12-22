(ns cryogen.core
  (:require [cryogen-core.compiler :refer [compile-assets-timed]]
            [cryogen-core.plugins :refer [load-plugins]]
            [cryogen.customization :as custom]))

(defn -main []
  (load-plugins)
  (compile-assets-timed {:extend-params-fn custom/extended-params})
  (System/exit 0))
