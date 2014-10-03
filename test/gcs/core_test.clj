(ns gcs.core-test
  (:require [clojure.test :refer :all]
            [gcs.core :refer :all]))

(deftest test-gcs-crud-ops
  (testing "Test the GCS crud operations.
    In order for the tests to pass you need to
     add a privatekey.pem file
     and configure the gcs.properties
    in the resources folder"
    (let [gcs-file-meta (->GcsMeta "testbucket003" "some_folder/blah.txt")
          test-content "This is a test content"]
      (is (= 200 (:status (upload gcs-file-meta test-content))))
      (is (= test-content (get-contents gcs-file-meta)))
      (is (= (:filename gcs-file-meta) ((get-metadata gcs-file-meta) "name")))
      (is (= 204 (:status (delete gcs-file-meta)))))))

(test-gcs-crud-ops)
