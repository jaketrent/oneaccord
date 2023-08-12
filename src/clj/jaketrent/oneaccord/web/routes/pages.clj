(ns jaketrent.oneaccord.web.routes.pages
  (:require
    [jaketrent.oneaccord.web.middleware.exception :as exception]
    [clojure.tools.logging :as log]
    [jaketrent.oneaccord.web.pages.layout :as layout]
    [integrant.core :as ig]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.ring.middleware.parameters :as parameters]
    [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
    [jaketrent.oneaccord.web.routes.utils :as utils]
    [jaketrent.oneaccord.web.controllers.meetings :as meetings]))

(defn wrap-page-defaults []
  (let [error-page (layout/error-page
                     {:status 403
                      :title "Invalid anti-forgery token"})]
    #(wrap-anti-forgery % {:error-response error-page})))

(defn home [{:keys [query-params] :as request}]
  (let [{:keys [query-fn]} (utils/route-data request)
        lang (get query-params "lang" "en")
        next-meeting-hymns (query-fn :find-next-meeting-hymns {})]
    (layout/render request "home.html" {:next-meeting-hymns next-meeting-hymns
                                        :lang lang})))

(defn admin [request]
  (layout/render request "admin.html"))

(defn hymns-list [{:keys [flash query-params] :as request}]
  (let [{:keys [query-fn]} (utils/route-data request)
        order-by-column (get query-params "sort" "english_num")
        hymns (query-fn :select-hymns {:order_by_column order-by-column})]
    (layout/render request "hymns/list.html" {:hymns hymns
                                              :errors (:errors flash)})))

(defn meetings-list [{:keys [flash query-params] :as request}]
  (let [{:keys [query-fn]} (utils/route-data request)
        order-by-column (get query-params "sort" "english_num")
        meetings (query-fn :select-meetings {})]
    (layout/render request "meetings/list.html" {:meetings meetings
                                                 :errors (:errors flash)})))

(defn meetings-edit  [{:keys [flash, path-params query-params] :as request}]
  (let [{:keys [query-fn]} (utils/route-data request)
        meeting-id (:meeting-id path-params)
        meeting (query-fn :find-meeting {:id meeting-id})
        order-by-column (get query-params "sort" "english_num")
        meeting-hymns (query-fn :select-meeting-hymns {:id meeting-id})
        hymns (query-fn :select-hymns {:order_by_column order-by-column})]
    (layout/render request "meetings/edit.html" {:meeting meeting
                                                 :meeting-hymns meeting-hymns
                                                 :hymns hymns
                                                 :errors (:errors flash)})))

(defn meeting-hymns-add  [{:keys [flash, path-params, form-params] :as request}]
  (let [{:keys [query-fn]} (utils/route-data request)
        {:strs [hymn-id]} form-params
        meeting-id (:meeting-id path-params)
        _ (query-fn :insert-meeting-hymn! {:meeting-id meeting-id :hymn-id hymn-id})
        meeting-hymns (query-fn :select-meeting-hymns {:id meeting-id})]
    (layout/render request "meetings/hymns.html" {:meeting-hymns meeting-hymns
                                                  :errors (:errors flash)})))

(defn meetings-create [{:keys [flash] :as request}]
  (layout/render request "meetings/create.html" {:errors (:errors flash)}))

(defn page-routes [_opts]
  [["/" {:get home}]
   ["/admin" {:get admin}]
   ["/hymns" {:get hymns-list}]
   ["/meetings" {:get meetings-list}]
   ["/meetings/create" {:get meetings-create :post meetings/create-meeting!}]
   ["/meetings/:meeting-id/edit" {:get meetings-edit :post meetings/update-meeting!}]
   ["/meetings/:meeting-id/edit/hymns" {:post meeting-hymns-add}]
   ["/meetings/:meeting-id/destroy" {:get meetings/delete-meeting!}]])

(defn route-data [opts]
  (merge
   opts
   {:middleware 
    [;; Default middleware for pages
     (wrap-page-defaults)
     ;; query-params & form-params
     parameters/parameters-middleware
     ;; encoding response body
     muuntaja/format-response-middleware
     ;; exception handling
     exception/wrap-exception]}))

(derive :reitit.routes/pages :reitit/routes)

(defmethod ig/init-key :reitit.routes/pages
  [_ {:keys [base-path]
      :or   {base-path ""}
      :as   opts}]
  (layout/init-selmer!)
  [base-path (route-data opts) (page-routes opts)])

