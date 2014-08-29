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
    (for [message
      (db/read-messages)]
    [:li
      [:bloquote message]])])
      ; [:p "-" [:cite name]]
      ; [:time (format-time timestamp)]])])

(defn home [& [name message error]]
  (layout/common
    [:h1 "Hello World!"]
    [:p "welcome"]
    [:p error]
    (show-guests)
    [:hr]
    ; [:form
    (form-to [:post "/"]
      [:p "Name:"]
      (text-field "name" name)
      [:p "Message:"]
      (text-area {:rows 10 :cold 40} "message" message)
      [:br]
      (submit-button "Submit"))))

(defn ping []
  (layout/common
    [:h1 "pinged!"]))

(defn save-message [name message]
  (cond
    ; (empty? name)
    ; (home name message "Forgot name")
    (empty? message)
    (home name message "No message")
    :else
      (do
        ; (println name message)
        (db/create-message message)
        (home))))

(defroutes home-routes
  (GET "/" [] (home))
  (POST "/" [name message] (save-message name message)))

