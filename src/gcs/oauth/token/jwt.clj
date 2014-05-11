(ns gcs.oauth.token.jwt
  (:require
    [clj-jwt.core  :refer :all]
    [clj-jwt.key   :refer [private-key]]
    [clj-time.core :refer [now plus hours]]))

(defn create-claim
  "Create a Google API OAuth JWT claim"
  [client-email scope]
  {:iss client-email
   :scope (str "https://www.googleapis.com/auth/devstorage." scope)
   :aud "https://accounts.google.com/o/oauth2/token"
   :exp (plus (now) (hours 1))
   :iat (now)})

(defn create-gcs-jwt-assertion 
  "Create a RSA signed JWT"
  [claim rsa-prv-key]
  (-> claim jwt (sign :RS256 rsa-prv-key) to-str))

(comment
  
(load-file "src/gcs/oauth/token/jwt.clj")
(refer 'gcs.oauth.token.jwt)

(def rsa-prv-key (private-key "resources/privatekey.pem"))
(create-gcs-jwt-assertion (create-claim "smth@developer.gserviceaccount.com" "read_write") rsa-prv-key)
(def claim (create-claim "smth@developer.gserviceaccount.com" "read_write"))
(-> claim jwt to-str)
(-> claim jwt (sign :RS256 rsa-prv-key) to-str)

)
