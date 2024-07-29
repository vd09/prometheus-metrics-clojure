(ns metrics-test.core
    (:require [iapetos.core :as prometheus]))


(def registerable-metrics
  (prometheus/histogram :app/request-duration-seconds
                        {:description "Request duration"
                         :labels [:endpoint]
                         :buckets [0.1 0.5 1 2 5]}))

(def registerable-metrics1
  (prometheus/histogram :app/request-duration-seconds1
                        {:description "Request duration"
                         :labels [:endpoint]
                         :buckets [0.1 0.5 1 2 5]}))

(defn job-latency-histogram [r]
      (prometheus/register r (prometheus/histogram
                               :app/job-latency-seconds
                               {:description "job execution latency by job type"
                                :labels [:job-type]
                                :buckets [1.0 5.0 7.5 10.0 12.5 15.0]})
                           (prometheus/histogram
                               :app/job-latency-seconds1
                               {:description "job execution latency by job type"
                                :labels [:job-type]
                                :buckets [1.0 5.0 7.5 10.0 12.5 15.0]})))

(defn vdji [r]
      (-> r
          (prometheus/register registerable-metrics)
          (job-latency-histogram)
          (prometheus/register registerable-metrics1)))

(defonce registry
         (-> (prometheus/collector-registry)
             vdji))

(defn record-duration [tags duration]
      (prometheus/observe (registry :app/request-duration-seconds tags) duration))

(defn update-record-duration []
      (record-duration {:endpoint "/home"} 0.3)
      (record-duration {:endpoint "/login"} 0.7)
      (record-duration {:endpoint "/signup"} 1.5)
      (record-duration {:endpoint "/home"} 3.0)
      (prometheus/observe (registry :app/job-latency-seconds {:job-type "pull"}) 14.2)
      (prometheus/observe (registry :app/job-latency-seconds {:job-type "push"}) 8.7)
      (prometheus/observe (registry :app/job-latency-seconds1 {:job-type "pull"}) 14.2)
      (prometheus/observe (registry :app/job-latency-seconds1 {:job-type "push"}) 8.7)
      (prometheus/observe (registry :app/request-duration-seconds1 {:endpoint "/home"}) 0.3)
      (prometheus/observe (registry :app/request-duration-seconds1 {:endpoint "/home"}) 0.3))