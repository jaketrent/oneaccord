(ns jaketrent.oneaccord.web.controllers.meetings
 (:require
   [clojure.tools.logging :as log]
   [jaketrent.oneaccord.web.routes.utils :as utils]
   [ring.util.http-response :as http-response]))

(defn create-meeting!
  [{{:strs [date name details]} :form-params :as request}]
  (log/debug "saving meeting" date name details)
  (let [{:keys [query-fn]} (utils/route-data request)]
    (try
      (if (or (empty? date))
        (cond-> (http-response/found "/meetings")
          (empty? name)
          (assoc-in [:flash :errors :date] "date is required"))
        (do
          (query-fn :insert-meeting! {:date date :name name :details details})
          (http-response/found "/meetings")))
      (catch Exception e
        (log/error e "failed to create meeting")
        (-> (http-response/found "/meetings/create")
            (assoc :flash {:errors {:unknown (.getMessage e)}}))))))

(defn update-meeting!
  [{{:strs [date name details]} :form-params :as request}]
  (let [{:keys [query-fn]} (utils/route-data request)
        meeting-id (:meeting-id (:path-params request))]
    (log/debug "updating meeting" meeting-id date name details)
    (try
      (if (or (empty? date))
        (cond-> (http-response/found "/meetings")
          (empty? name)
          (assoc-in [:flash :errors :date] "date is required"))
        (do
          (query-fn :update-meeting! {:id meeting-id :date date :name name :details details})
          (http-response/found "/meetings")))
      (catch Exception e
        (log/error e "failed to update meeting")
        (-> (http-response/found (str "/meetings/" meeting-id "/edit"))
            (assoc :flash {:errors {:unknown (.getMessage e)}}))))))

(defn delete-meeting! [request]
  (let [{:keys [query-fn]} (utils/route-data request)
        meeting-id (:meeting-id (:path-params request))
        redir-url "/meetings"]
    (log/debug "deleting meeting" meeting-id)
    (try
        (query-fn :delete-meeting! {:id meeting-id})
        (http-response/found redir-url)
      (catch Exception e
        (log/error e "failed to delete meeting")
        (-> (http-response/found redir-url)
            (assoc :flash {:errors {:unknown (.getMessage e)}}))))))
