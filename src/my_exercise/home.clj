(ns my-exercise.home
  (:require [hiccup.page :refer [html5]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [my-exercise.us-state :as us-state]))

;;;; Updating home page to have some sort of theme, so you can read the output better.
;;;   Keeping the original form information here, as it is a good start.
;;; @date 2018-04-22

(defn address-form [_]
  [:div {:class "address-form"}
   [:h4 "Please enter the address in which you are registered to vote."]
   [:form {:novalidate true :id "address-search"}
    (anti-forgery-field)
    [:div {:class "form-row"}
     [:div {:class "form-group col-md-6 required"}
      [:label {:for "street-field"} "Address"]
      [:input {:id "street-field"
               :class "form-control"
               :type "text"
               :name "street"
               :required true}]]
     [:div {:class "form-group col-md-6"}
      [:label {:for "street-2-field"} "Address Cont."]
      [:input {:id "street-2-field"
               :class "form-control"
               :type "text"
               :name "street-2"}]]
     [:div {:class "form-group col-md-4 required"}
      [:label {:for "city-field"} "City"]
      [:input {:id "city-field"
               :class "form-control"
               :type "text"
               :name "city"
               :required true}]]
     [:div {:class "form-group col-md-4 required"}
      [:label {:for "state-field"} "State"]
      [:select {:id "state-field"
                :class "form-control"
                :name "state"
                :required true}
       [:option "Select Your State"]
       (for [state us-state/postal-abbreviations]
         [:option {:value state} state])]]
     [:div {:class "form-group col-md-4 required"}
      [:label {:for "zip-field"} "Postal Code"]
      [:input {:id "zip-field"
               :class "form-control"
               :type "text"
               :name "zip"
               :required true}]]
     [:div {:class "form-group col-md-12 text-right"}
      [:a {:href "/" :class "btn btn-default"} "Reset"]
      [:button {:type "submit" :class "btn btn-primary"} "Search"]]]]])

;;;; Adding in boostrap container and navbar, to give some better feel to the page and cleaning up
;;;   content area.

(defn header [_]
  [:head
   [:meta {:charset "UTF-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1, shrink-to-fit=no"}]
   [:title "Election Finder: Your vote counts!"]
   [:link {:rel "stylesheet" :href "https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css"}]
   [:link {:rel "stylesheet" :href "https://fonts.googleapis.com/css?family=Oxygen"}]
   [:link {:rel "stylesheet" :href "default.css"}]])

(defn navbar [request]
  [:nav {:class "navbar navbar-expand-md navbar-dark fixed-top bg-election-finder-nav"}
   [:a {:class "navbar-brand" :href "#"} "Election Finder"]])

(defn instructions [request]
  [:div {:class "content row instructions"}
   [:div {:class "col-12"}
    [:h3 "Find your next election fast and easily."]
    [:p "Election finder will accept your current address and find the closest elections to your location."]
    [:p "Please fill out the form below and press \"Search\" to continue. The results will be displayed below."]]])

(defn search [request]
  [:div {:class "content row search"}
   [:div {:class "col-12"}
    (address-form request)]])

(defn search-results [request]
  [:div {:class "content row"}
   [:div {:class "col-12"}
    [:h4 "Search Results"]
    [:div {:id "search-results"}]]])

(defn container [request]
  [:body
   [:div {:class "container-fluid"}
    (instructions request)
    (search request)
    (search-results request)]])

(defn footer [request]
  [:script {:type "text/javascript" :src "main.js"}])

;;;; Pull everything into the page, instead of pieces.
;;;   TODO - this really needs to be in separate components. Everything in one file - eek!

(defn page [request]
  (html5 {:lang "en"}
         (header request)
         (navbar request)
         (container request)
         (footer request)))
