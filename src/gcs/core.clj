(ns gcs.core
  (:require 
    [clj-http.client :as client]
    [clj-jwt.key   :refer [private-key]]
    [clojure.data.json :as json]
    [gcs.oauth.token.request :refer [get-access-token]]
    [gcs.simple-operations :as ops])
   (:import [java.net URLEncoder]))

(defprotocol GcsOps
	"A protocol for performing upload, get and delete operations"
	(upload [this] "Uploads a file that has a small(i.e. no streaming involved) content to the Google Cloud Storage")
	(get-contents [this] "Gets the contents of the specified object from the Google Cloud Storage.
		Please note that the filename should be URL encoded")
	(get-metadata [this] "Gets the metadata of the specified object from the Google Cloud Storage")
	(delete [this] "Deletes an object and its metadata. 
		Deletions are permanent if versioning is not enabled for the bucket, or if the generation parameter is used.")
	)

(defrecord GCS [bucket filename content])

(extend-type GCS
	GcsOps
	(upload [this]  
 		 (ops/upload this (get-access-token)))
	(get-contents [this]
		(let [content (-> (ops/get-object-contents {:bucket (:bucket this)
							                        :filename (URLEncoder/encode (:filename this) "UTF-8")
							                        :access-token (get-access-token)}) 
						  :body)]
		(assoc this :content content)))

(comment

(load-file "src/gcs/core.clj")
(refer 'gcs.core)

(def gcs-file (->GCS "testbucket003" "some_folder/blah.txt" "This is a test content"))

(upload gcs-file)
(get-contents (assoc gcs-file :content "nothing"))

(->> (get-access-token) 
     (upload gcs-file) 
     :body 
     (json/read-str))
(-> (ops/get-object-contents {:bucket "testbucket003" 
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