(ns gcs.token.jwt.generate
  (:require
    [clj-jwt.core  :refer :all]
    [clj-jwt.key   :refer [private-key]]
    [clj-time.core :refer [now plus days]]))


(def claim
  {:iss "534154772173-cmpe0j9ae4tl8pd50av8f891lhhftqdf@developer.gserviceaccount.com"
   :scope "https://www.googleapis.com/auth/devstorage.readonly"
   :aud "https://accounts.google.com/o/oauth2/token"
   :exp (plus (now) (days 1))
   :iat (now)})


;(def sample-claim 
;  {:iss "761326798069-r5mljlln1rd4lrbhg75efgigp36m78j5@developer.gserviceaccount.com",
;   :scope "https://www.googleapis.com/auth/prediction",
;   :aud "https://accounts.google.com/o/oauth2/token",
;   :exp "1328554385"
;   :iat "1328550785"})

(-> claim jwt to-str)
