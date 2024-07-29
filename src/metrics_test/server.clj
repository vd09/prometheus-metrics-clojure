(ns metrics-test.server
    (:require [ring.adapter.jetty :refer [run-jetty]]
      [iapetos.collector.ring :as prometheus-ring]
      [iapetos.core :as prometheus]
      [ring.middleware.params :refer [wrap-params]]
      [ring.util.response :as response]
      [metrics-test.core :refer [registry update-record-duration]]))

(defn metrics-handler []
      (prometheus-ring/metrics-response registry))

(defn app []
      (-> (fn [request]
              (case (:uri request)
                    "/metrics" (metrics-handler)
                    (response/not-found "Not Found")))))

(defn -main []
      (update-record-duration) ;; Example of recording some metrics
      (run-jetty (app) {:port 3000 :join? false}))