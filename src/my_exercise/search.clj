(ns my-exercise.search
  (:require [clojure.string :as str]
            [ring.util.response :refer [response, content-type]]
            [clj-http.client :as client]))

;;;; 'https://jsonplaceholder.typicode.com/posts/1' - This works no problem.
;;;; ?district-divisions=ocd-division/country:us/state:va/place:bristol

;;;; https produces: sun.security.provider.certpath.SunCertPathBuilderException
;;;     Without using insecure? true. This is a rabbit hole one can go down. :)

(def url "https://api.turbovote.org/elections/upcoming")

(defn q [state city]
  "Returns the OCD string for the proper API search"
  (str "ocd-division/country:us"
       "/state:" (str/lower-case state)
       "/place:" (str/lower-case city)))

(defn page [state city]
  (content-type
   (response {:result (client/get url {:debug true
                                       :insecure? true
                                       :accept :json
                                       :as :json
                                       :query-params {:district-divisions [(q state city)]}})})
   "application/json"))
