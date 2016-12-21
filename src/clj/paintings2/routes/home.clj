(ns paintings2.routes.home
  (:require [paintings2.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [paintings2.api-get :as api]
            [compojure.route :refer [resources]]
            [environ.core :refer [env]]
            [paintings2.api-get :as api]
            [clj-http.client :as client]))

(defn home-page [page-num]
  (let [page (or page-num 1)
        prev-page (dec page)
        prev? (pos? prev-page)
        next-page (inc page)
        next? (< page 470)]
    (println "next? = " next? ", next-page = " next-page)
    (layout/render
     "home.html" {:paintings (api/fetch-paintings-and-images-front-page (api/get-page-of-ids page))
                  :page-num page
                  :prev? prev?
                  :prev-page prev-page
                  :next? next?
                  :next-page next-page})))

(defn detail-page [id]
  (layout/render
    "detail.html" {:paintings (first (api/fetch-paintings-and-images-detail-page [id]))}))


(defroutes home-routes
  (GET "/" [page-num] (home-page (read-string page-num)))
  (GET "/detail/:id" [id] (detail-page id))
  (resources "/"))
