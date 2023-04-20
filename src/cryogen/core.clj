(ns cryogen.core
  (:require [cryogen-core.compiler :refer [compile-assets-timed]]
            [cryogen-core.plugins :refer [load-plugins]]))

(defn build-site
  "Allow the build to be called from other functions."
  []
  (load-plugins)
  (compile-assets-timed))

(defn -main []
  (build-site)
  (System/exit 0))
