(ns gcs.simple-operations
  (:require 
    [clj-http.client :as client]
    [clj-jwt.key   :refer [private-key]]
    [clojure.data.json :as json]
    [gcs.oauth.token.request :refer :all]))

(defn upload
  "Uploads a file that has a small content to the Google Cloud Storage 
   bucket using the access token"
  [{:keys [bucket filename content]} access-token]
  (let [url (str "https://www.googleapis.com/upload/storage/v1/b/" bucket "/o?uploadType=media&name=" filename)]
  (client/post url
               {:body content
                :content-type "text/plain"
                :socket-timeout 5000  ;; in milliseconds
                :conn-timeout 5000    ;; in milliseconds
                :headers {"Authorization" (str "Bearer " access-token)}
                :throw-entire-message? true
                })))

(defn get-object
  "Gets the Google Cloud Storage object content or metadata depending whether 
params is set to ?alt=media or not, respectively.
Please note that the filename should be URL encoded"
  [{:keys [bucket filename access-token params]}]
  (let [url (str "https://www.googleapis.com/storage/v1/b/" bucket "/o/" filename params)]
    (client/get url
                {:headers {"Authorization" (str "Bearer " access-token)}
                 :throw-entire-message? true})))


(defn get-object-contents
  "Gets the contents of the specified object from the Google Cloud Storage"
  [gcs-object-info]
  (get-object (assoc gcs-object-info :params "?alt=media")))

(defn get-object-metadata
  "Gets the metadata of the specified object from the Google Cloud Storage"
  [gcs-object-info]
  (get-object gcs-object-info))

(defn delete 
  [{:keys [bucket filename access-token params]}]
  (let [url (str "https://www.googleapis.com/storage/v1/b/" bucket "/o/" filename)]
    (client/delete url
                {:headers {"Authorization" (str "Bearer " access-token)}
                 :throw-entire-message? true})))

(comment

(load-file "src/gcs/simple_operations.clj")
(refer 'gcs.simple-operations)

(def gcs-file {:bucket "testbucket003" 
               :filename "some_folder/blah.txt" 
               :content "This is a test content"})
(->> (get-access-token) 
     (upload gcs-file) 
     :body 
     (json/read-str))
(-> (get-object-contents {:bucket "testbucket003" 
                       :filename "some_folder%2Fblah.txt" 
                       :access-token (get-access-token)}) 
    :body)
(-> (get-object-metadata 
      {:bucket "testbucket003" 
       :filename "some_folder%2Fblah.txt" 
       :access-token (get-access-token)}) 
    :body)

(delete {:bucket "testbucket003" 
         :filename "some_folder%2Fblah.txt" 
         :access-token (get-access-token)}) 

)
