(ns prometheus-metrics-clojure.core-test
    (:require [clojure.test :refer :all]
      [prometheus-metrics-clojure.core :refer :all])
    (:import [io.prometheus.client Histogram CollectorRegistry]
      [io.prometheus.client.exporter.common TextFormat]
      [java.io StringWriter]))

;(defn setup []
;      ;; Clear the default registry and re-register the histogram before each test
;      (.clear metrics-registry)
;      (-> (Histogram/build)
;          (.name "participant_url_based_acknowledgement_delay")
;          (.help "Participant URL-based Acknowledgement Delay")
;          (.buckets (double-array [1 2 3 4]))
;          (.create)
;          (.register metrics-registry)))
;
;(use-fixtures :each (fn [f]
;                        (setup)
;                        (print `f)
;                        (f)))

(deftest test-participant-url-based-acknowledgement-delay-histogram
         (report-participant-url-based-acknowledgement-delay 42)
         (report-participant-url-based-acknowledgement-delay 4)
         (report-participant-url-based-acknowledgement-delay 2)
         (let [writer (StringWriter.)
               _ (TextFormat/write004 writer (.metricFamilySamples metrics-registry))
               output (.toString writer)]
              (print output)
              (is (.contains output "participant_url_based_acknowledgement_delay_sum 48.0"))
              (is (.contains output "participant_url_based_acknowledgement_delay_count 3"))
              (is (.contains output "participant_url_based_acknowledgement_delay_bucket{le=\"1.0\",} 0.0"))
              (is (.contains output "participant_url_based_acknowledgement_delay_bucket{le=\"2.0\",} 1.0"))
              (is (.contains output "participant_url_based_acknowledgement_delay_bucket{le=\"3.0\",} 1.0"))
              (is (.contains output "participant_url_based_acknowledgement_delay_bucket{le=\"4.0\",} 2.0"))))

;(deftest test-participant-url-ack-delay-timer
;         (let [start-time (System/nanoTime)
;               timer (start-participant-url-ack-delay-timer)
;               _ (Thread/sleep 50)
;               _ (stop-participant-url-ack-delay-timer timer)
;               delta (/ (- (System/nanoTime) start-time) 1e9)
;               writer (StringWriter.)
;               _ (TextFormat/write004 writer (.metricFamilySamples metrics-registry))
;               output (.toString writer)]
;              (print output)
;              (is (.contains output "participant_url_based_acknowledgement_delay_count 1"))
;              (let [sum (Double/parseDouble (second (re-find #".*participant_url_based_acknowledgement_delay_sum (\d+\.\d+).*" output)))]
;                   (is (<= 0.05 sum delta)))))