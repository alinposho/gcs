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
	(upload [this content] "Uploads a file that has a small(i.e. no streaming involved) content to the Google Cloud Storage")
	(get-contents [this] "Gets the contents of the specified object from the Google Cloud Storage.
		Please note that the filename should be URL encoded")
	(get-metadata [this] "Gets the metadata of the specified object from the Google Cloud Storage")
	(delete [this] "Deletes an object and its metadata. 
		Deletions are permanent if versioning is not enabled for the bucket, or if the generation parameter is used.")
	)

(defrecord GcsMeta [bucket filename])

(extend-type GcsMeta
	GcsOps
	(upload [this content]  
 		 (ops/upload (assoc this :content content) (get-access-token)))
	(get-contents [this]
		(let [url-encoded-filename (URLEncoder/encode (:filename this) "UTF-8")
          access-token (get-access-token)
          req-params (assoc this :filename url-encoded-filename :access-token access-token)]
		{:content (-> (ops/get-object-contents req-params) 
                  :body)}))
  (delete [this]
    (let [url-encoded-filename (URLEncoder/encode (:filename this) "UTF-8")
          access-token (get-access-token)
          req-params (assoc this :filename url-encoded-filename :access-token access-token)]
      (ops/delete req-params))))

(comment

(load-file "src/gcs/core.clj")
(refer 'gcs.core)

(def gcs-file-meta (->GcsMeta "testbucket003" "some_folder/blah.txt"))

(upload gcs-file-meta "This is a test content")

(get-contents gcs-file-meta)

(delete {:bucket "testbucket003" 
         :filename "some_folder%2Fblah.txt" 
         :access-token (get-access-token)}) 

)