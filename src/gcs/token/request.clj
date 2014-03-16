(ns gcs.token.request
  (:require 
    [clj-http.client :as client]
    [clj-jwt.core  :refer :all]
    [clj-jwt.key   :refer [private-key]]
    [clj-time.core :refer [now plus hours]]))

(def claim
  {:iss "534154772173-cmpe0j9ae4tl8pd50av8f891lhhftqdf@developer.gserviceaccount.com"
   :scope "https://www.googleapis.com/auth/devstorage.read_write"
   :aud "https://accounts.google.com/o/oauth2/token"
   :exp (plus (now) (hours 1))
   :iat (now)})

(def rsa-prv-key (private-key "resources/226c3a26c8ab392813eb0a1ac4522798233d376c-privatekey.pem"))

(defn req-token 
  [claim rsa-prv-key]
  (client/post "https://accounts.google.com/o/oauth2/token"
    {:body (str "grant_type=urn%3Aietf%3Aparams%3Aoauth%3Agrant-type%3Ajwt-bearer&assertion=" (-> claim jwt (sign :RS256 rsa-prv-key) to-str))
     :content-type "application/x-www-form-urlencoded"
     :socket-timeout 5000  ;; in milliseconds
     :conn-timeout 5000    ;; in milliseconds
;     :throw-entire-message? true
     }))

(req-token claim rsa-prv-key)