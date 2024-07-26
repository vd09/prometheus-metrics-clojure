(ns prometheus-metrics-clojure.core
    (:import [io.prometheus.client Histogram]
      [io.prometheus.client CollectorRegistry]))

(defonce metrics-registry (CollectorRegistry/defaultRegistry))

(defonce participant-url-ack-delay-histogram
         (-> (Histogram/build)
             (.name "participant_url_based_acknowledgement_delay")
             (.help "Participant URL-based Acknowledgement Delay")
             (.buckets (double-array [1 2 3 4]))
             (.create)
             (.register metrics-registry)))

(defn report-participant-url-based-acknowledgement-delay [t]
      (.observe participant-url-ack-delay-histogram t))

(defn start-participant-url-ack-delay-timer []
      (.startTimer participant-url-ack-delay-histogram))

(defn stop-participant-url-ack-delay-timer [timer]
      (.observeDuration timer))