(ns metrics-test.core-test
    (:require [clojure.test :refer :all]
      [metrics-test.core :refer :all]
      [iapetos.export :as export]
      [iapetos.core :as prometheus]))


(deftest test-histogram-metric
         (testing "Histogram metric records durations correctly"
                  ;; Record some durations
                  (update-record-duration)

                  ;; Get the metrics as a string
                  (let [metrics (export/text-format registry)]
                       ;; Check if the histogram data is present in the metrics output
                       (println metrics)
                       (is (.contains metrics "app_request_duration_seconds_bucket"))
                       (is (.contains metrics "app_request_duration_seconds_count"))
                       (is (.contains metrics "app_request_duration_seconds_sum")))))
