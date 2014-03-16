(ns gcs.oauth.token.request
  (:require 
    [clj-http.client :as client]
    [clj-jwt.key   :refer [private-key]]
    [clojure.data.json :as json]
    [gcs.oauth.token.jwt :as jwt]
    [gcs.config :refer [client-email gcs-permission-scope config]]))

(defn req-token 
  [jwt-assertion]
  (client/post "https://accounts.google.com/o/oauth2/token"
               {:body (str 
                        "grant_type=urn%3Aietf%3Aparams%3Aoauth%3Agrant-type%3Ajwt-bearer&assertion=" 
                        jwt-assertion)
                :content-type "application/x-www-form-urlencoded"
                :socket-timeout 5000  ;; in milliseconds
                :conn-timeout 5000    ;; in milliseconds
                :throw-entire-message? true
                }))

(defn get-token-from-json-string
  "Extracts the OAuth token from a JSON string that looks like this
{
  \"access_token\": \"ya29.1.AADtN_UebjMlWtlWqOCgkTVzss0LAPvNcriRzRnVEFrWvzHFdDFFa6qfQGufmTk\", 
  \"token_type\": \"Bearer\", 
  \"expires_in\": 3600
}"
  [json-string]
  ((json/read-str json-string) "access_token"))

(def claim (jwt/create-claim (client-email (config)) (gcs-permission-scope (config))))
(def rsa-prv-key (private-key "resources/privatekey.pem"))
(defn get-access-token [] (get-token-from-json-string (:body (req-token (jwt/create-gcs-jwt-assertion claim rsa-prv-key)))))


;(get-token-from-json-string "
;{
;  \"access_token\": \"ya29.1.AADtN_UebjMlWtlWqOCgkTVzss0LAPvNcriRzRnVEFrWvzHFdDFFa6qfQGufmTk\", 
;  \"token_type\": \"Bearer\", 
;  \"expires_in\": 3600
;}")

;(def claim (jwt/create-claim (client-email (config)) (gcs-permission-scope (config))))
;(def rsa-prv-key (private-key "resources/privatekey.pem"))
;(get-token-from-json-string (:body (req-token (jwt/create-gcs-jwt-assertion claim rsa-prv-key))))
;(req-token (jwt/create-gcs-jwt-assertion claim rsa-prv-key))






