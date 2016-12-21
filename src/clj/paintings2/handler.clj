(ns paintings2.handler
  (:require [compojure
             [core :refer [routes wrap-routes]]
             [route :as route]]
            [mount.core :as mount]
            [paintings2
             [env :refer [defaults]]
             [layout :refer [error-page]]
             [middleware :as middleware]]
            [paintings2.routes.home :refer [home-routes]]
            [ring.middleware.session :refer [wrap-session]]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
   (-> #'home-routes
       (wrap-routes middleware/wrap-csrf)
       (wrap-routes middleware/wrap-formats))
   (route/not-found
    (:body
     (error-page {:status 404
                  :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
