(ns metrics-test.server
    (:require [ring.adapter.jetty :refer [run-jetty]]
      [iapetos.collector.ring :as prometheus-ring]
      [iapetos.core :as prometheus]
      [ring.middleware.params :refer [wrap-params]]
      [ring.util.response :as response]
      [metrics-test.core :refer [registry update-record-duration]]))

(defonce exception-counter
         (prometheus/counter :app/exceptions
                             {:description "Number of exceptions"}))

(prometheus/register registry exception-counter)

(defn metrics-handler []
      (prometheus-ring/metrics-response registry))

(defn wrap-exception-handler [handler]
      (fn [request]
          (try
            (handler request)
            (catch Exception e
              (prometheus/inc exception-counter)
              (throw e)))))

(defn app []
      (-> (fn [request]
              (case (:uri request)
                    "/metrics" (metrics-handler)
                    (response/not-found "Not Found")))
          ;(wrap-exception-handler)
          ;(prometheus-ring/wrap-metrics registry)
          ))

(defn -main []
      (update-record-duration) ;; Example of recording some metrics
      (run-jetty (app) {:port 3000 :join? false}))