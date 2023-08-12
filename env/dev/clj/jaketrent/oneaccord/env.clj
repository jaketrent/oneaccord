(ns jaketrent.oneaccord.env
  (:require
    [clojure.tools.logging :as log]
    [jaketrent.oneaccord.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[oneaccord starting using the development or test profile]=-"))
   :start      (fn []
                 (log/info "\n-=[oneaccord started successfully using the development or test profile]=-"))
   :stop       (fn []
                 (log/info "\n-=[oneaccord has shut down successfully]=-"))
   :middleware wrap-dev
   :opts       {:profile       :dev
                :persist-data? true}})
