(ns cryogen.server
  (:require
   [clojure.string :as string]
   [compojure.core :refer [GET defroutes]]
   [compojure.route :as route]
   [ring.util.response :refer [redirect file-response]]
   [ring.util.codec :refer [url-decode]]
   [cryogen-core.watcher :refer [start-watcher!]]
   [cryogen-core.plugins :refer [load-plugins]]
   [cryogen-core.compiler :refer [compile-assets-timed]]
   [cryogen-core.config :refer [resolve-config]]
   [cryogen-core.io :refer [path]]
   [cryogen.customization :as c]
   [clj-livereload.server :as live-reload]))




(defn init []
  (load-plugins)
  (let [ignored-files (-> (resolve-config) :ignored-files)
        public-dest (-> (resolve-config) :public-dest)
        cate (fn compile-assets-timed-extend []
               (compile-assets-timed {:extend-params-fn c/extend-params}))]
    (cate)
    (start-watcher! "content" ignored-files cate)
    (start-watcher! "themes" ignored-files cate)
    (println (str "Start Live Reload " public-dest))
    (live-reload/start! {:paths [public-dest]
                         :debug? true})))

(defn wrap-subdirectories
  [handler]
  (fn [request]
    (let [{:keys [clean-urls blog-prefix public-dest]} (resolve-config)
          req-uri (.substring (url-decode (:uri request)) 1)
          res-path (if (or (.endsWith req-uri "/")
                           (.endsWith req-uri ".html")
                           (-> (string/split req-uri #"/")
                               last
                               (string/includes? ".")
                               not))
                     (condp = clean-urls
                       :trailing-slash (path req-uri "index.html")
                       :no-trailing-slash (if (or (= req-uri "")
                                                  (= req-uri "/")
                                                  (= req-uri
                                                     (if (string/blank? blog-prefix)
                                                       blog-prefix
                                                       (.substring blog-prefix 1))))
                                            (path req-uri "index.html")
                                            (path (str req-uri ".html")))
                       :dirty (path (str req-uri ".html")))
                     req-uri)]
      (or (file-response res-path {:root public-dest})
          (handler request)))))

(defroutes routes
  (GET "/" [] (redirect (let [config (resolve-config)]
                          (path (:blog-prefix config)
                                (when (= (:clean-urls config) :dirty)
                                  "index.html")))))
  (route/files "/")
  (route/not-found "Page not found"))

(def handler (wrap-subdirectories routes))
