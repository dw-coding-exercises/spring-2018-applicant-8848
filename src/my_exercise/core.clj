(ns my-exercise.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
            [my-exercise.home :as home]
            [my-exercise.search :as search]))

(defroutes site-routes
           (GET "/" [] home/page)
           (POST "/search" [state city] (search/page state city))
           (route/resources "/")
           (route/not-found "<h2>Sorry, page not found</h2>"))

(def app
  (-> site-routes
      (wrap-defaults site-defaults)
      wrap-reload
      wrap-params
      wrap-json-params
      wrap-json-response))
