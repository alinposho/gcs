(ns gcs.simple-operations-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [gcs.oauth.token.request :refer :all]
            [gcs.simple-operations :refer :all]))


(deftest simple-operations-tests
  (testing "CRUD operations using the real Google Cloud storage API.
    In order for the tests to pass you need to
     add a privatekey.pem file
     and configure the gcs.properties
    in the resources folder"
  	(is (= "This is a test content"
	  		(let [bucket "testbucket003"
              filename "some_folder/blah.txt"
              url-encoded-filename (java.net.URLEncoder/encode filename "UTF-8")
              gcs-file {:bucket bucket
			                  :filename filename
			                  :content "This is a test content"}]
          (try
            (->> (get-access-token)
                 (upload gcs-file)
                 :body
                 (json/read-str))
            (Thread/sleep 500)
            (-> (get-object-contents {:bucket bucket
                                      :filename url-encoded-filename
                                      :access-token (get-access-token)})
                :body)
            (finally
              (delete {:bucket bucket
                       :filename url-encoded-filename
                       :access-token (get-access-token)}))))))))

