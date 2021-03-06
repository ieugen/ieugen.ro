(defproject cryogen "0.1.0"
            :description "Simple static site generator"
            :url "https://github.com/lacarmen/cryogen"
            :license {:name "Eclipse Public License"
                      :url "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.10.1"]
                           [ring/ring-devel "1.8.2"]
                           [compojure "1.6.2"]
                           [http-kit "2.5.0"]
                           [ring-server "0.5.0"]
                           [cryogen-asciidoc "0.3.3"]
                           [cryogen-core "0.4.0"]
                           [clj-livereload "0.2.0"]
                           [org.slf4j/slf4j-simple "1.7.30"]
                           ]
            :plugins [[lein-ring "0.12.5"]]
            :main cryogen.core
            :ring {:init cryogen.server/init
                   :handler cryogen.server/handler})



