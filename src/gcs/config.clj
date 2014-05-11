(ns gcs.config
  (:use [clojure.java.io :only (reader resource)])
  (:require [clojure.string :as str])
  (:import (java.util Properties)))

(defn load-properties [src]
  (with-open [rdr (reader src)]
    (doto (Properties.)
      (.load rdr))))

(defn read-config
  []
  (load-properties "resources/gcs.properties"))

(defn client-email [props]
  (.get props "gcs.client_email"))

(defn permission-scope [props]
  (.get props "gcs.permissions.scope"))

(defrecord GcsConfig [client-email permission-scope])

(defn read-gcs-config 
  "Read the GCS configuration from the file system"
  []
  (let [props (read-config)]
    (->GcsConfig (client-email props) (permission-scope props))))

(comment

(load-file "src/gcs/config.clj")
(refer 'gcs.read-config)
(client-email (config))
(permission-scope (config))

(def gcs-config (read-gcs-config))
(client-email gcs-config)
(permission-scope gcs-config)

)