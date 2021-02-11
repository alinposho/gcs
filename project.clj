(defproject gcs "0.1.0-SNAPSHOT"
  :description "An exploration of the Google Cloud Storage API"
  :url "https://github.com/alinposho/gcs"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [clj-jwt "0.1.1"]
                 [clj-http "3.12.1"]
                 [org.clojure/data.json "1.0.0"]
                 [base64-clj "0.1.1"]]
  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "1.1.0"]]}
             :test {:source-paths ["test" "resources-test"]
                    :dependencies [[org.clojure/tools.namespace "1.1.0"]]}})
