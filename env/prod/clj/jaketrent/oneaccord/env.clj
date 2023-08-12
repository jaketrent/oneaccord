(ns jaketrent.oneaccord.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[oneaccord starting]=-"))
   :start      (fn []
                 (log/info "\n-=[oneaccord started successfully]=-"))
   :stop       (fn []
                 (log/info "\n-=[oneaccord has shut down successfully]=-"))
   :middleware (fn [handler _] handler)
   :opts       {:profile :prod}})
