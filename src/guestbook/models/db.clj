(ns guestbook.models.db)

(defrecord Message [author message])

(def messages (ref ()) )

(defn read-messages []
  (deref messages))

(defn insert-message [author message]
  (dosync (alter messages conj (Message. author message))))
