(ns guestbook.routes.home
  (:require [compojure.core :refer :all]
            [guestbook.views.layout :as layout]
            [guestbook.models.db :as db]
            [hiccup.form :refer :all]))

(defn format-time [timestamp]
  (-> "dd/MM/yyyy"
    (java.text.SimpleDateFormat.)
    (.format timestamp)))

(defn show-guests []
  [:ul.guests
    (for [message (db/read-messages)]
    [:li
      [:bloquote (:message message)]
      [:p "-" [:cite (:author message)]]])])

(defn home [& [author message error]]
  (layout/common
    [:h1 "Hello World!"]
    [:p "welcome"]
    [:p error]
    (show-guests)
    [:hr]
    (form-to [:post "/"]
      [:p "Author:"]
      (text-field "author" author)
      [:p "Message:"]
      (text-area {:rows 10 :cold 40} "message" message)
      [:br]
      (submit-button "Submit"))))

(defn ping []
  (layout/common
    [:h1 "pinged!"]))

(defn save-message [author message]
  (cond
    (empty? author)
    (home author message "Forgot author")
    (empty? message)
    (home author message "No message")
    :else
      (do
        (println author message)
        (db/insert-message author message)
        (home))))

(defroutes home-routes
  (GET "/" [] (home))
  (POST "/" [author message] (save-message author message)))

