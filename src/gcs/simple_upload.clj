(ns gcs.simple-upload
  (:require 
    [clj-http.client :as client]
    [clj-jwt.key   :refer [private-key]]
    [clojure.data.json :as json]
    [gcs.oauth.token.request :refer :all]))

(defrecord GCS [bucket filename content])

(defn upload
  "Uploads a file that has a small content to the Google Cloud Storage 
   bucket using the access token"
  [{:keys [bucket filename content]} access_token]
  (let [url (str "https://www.googleapis.com/upload/storage/v1beta2/b/" bucket "/o?uploadType=media&name=" filename)]
  (client/post url
               {:body content
                :content-type "text/plain"
                :socket-timeout 5000  ;; in milliseconds
                :conn-timeout 5000    ;; in milliseconds
                :headers {"Authorization" (str "Bearer " access_token)}
                :throw-entire-message? true
                })))

(def gcs-file (GCS. "testbucket003" "some_folder/blah.txt" "This is a test content"))
(->> (get-access-token) (upload gcs-file) :body (json/read-str))


