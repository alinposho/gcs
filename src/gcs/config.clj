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

; (defn client-email [props]
;   (.get props "gcs.client_email"))

; (defn gcs-permission-scope [props]
;   (.get props "gcs.permissions.scope"))

(defprotocol GcsConfig
  "A protocol for accessing application configurations"
  (client-email [this] "Get the client email property")
  (permission-scope [this] "Get the Google Cloud Storage permissions property"))

(defrecord GcsConfig [props])
(extend-type GcsConfig 
  Config
  (client-email [this]
    (.get (:props this) "gcs.client_email"))
  (permission-scope [this]
    (.get (:props this) "gcs.permissions.scope")))

(comment

(load-file "src/gcs/config.clj")
(refer 'gcs.read-config)
; (client-email (config))
; (gcs-permission-scope (config))

(def gcs-config (->GcsConfig (read-config)))
(client-email gcs-config)
(permission-scope gcs-config)

)