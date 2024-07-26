(ns metrics-test.core
    (:require [iapetos.core :as prometheus]))

(defonce registry
         (-> (prometheus/collector-registry)
             (prometheus/register
               (prometheus/histogram :app/request-duration-seconds
                                     {:description "Request duration"
                                      :buckets [0.1 0.5 1 2 5]}))))

(defn record-duration [duration]
      (prometheus/observe registry :app/request-duration-seconds duration))

(defn update-record-duration []
      (record-duration 0.3)
      (record-duration 0.7)
      (record-duration 1.5)
      (record-duration 3.0))