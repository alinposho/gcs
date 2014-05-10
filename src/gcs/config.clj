(ns gcs.config
  (:use [clojure.java.io :only (reader resource)])
  (:require [clojure.string :as str])
  (:import (java.util Properties)))

(defn- load-properties [src]
  (with-open [rdr (reader src)]
    (doto (Properties.)
      (.load rdr))))

(defn config
  []
  (load-properties "resources/gcs.properties"))

(defn client-email [props]
  (.get props "gcs.client_email"))

(defn gcs-permission-scope [props]
  (.get props "gcs.permissions.scope"))

(comment

(load-file "src/gcs/config.clj")
(refer 'gcs.config)
(client-email (config))
(gcs-permission-scope (config))

)