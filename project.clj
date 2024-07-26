(defproject prometheus-metrics-clojure "0.1.0-SNAPSHOT"
  :description "A project to demonstrate Prometheus metrics in Clojure"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [io.prometheus/simpleclient "0.12.0"]
                 [io.prometheus/simpleclient_common "0.12.0"]
                 [io.prometheus/simpleclient_hotspot "0.12.0"]
                 [io.prometheus/simpleclient_pushgateway "0.12.0"]]
  :profiles {:dev {:dependencies [[org.clojure/test.check "1.1.0"]]}})