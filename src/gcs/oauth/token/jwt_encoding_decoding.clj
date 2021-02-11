(ns gcs.oauth.token.jwt-encoding-decoding
  (:require [base64-clj.core :as base64]
             [clojure.string :as str]))

(defn decodeAssertion [a]
  (map base64/decode
       (drop-last
         (str/split a #"\."))))

(comment
  (decodeAssertion "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiI1MzQxNTQ3NzIxNzMtb3A5ZTBvZzU0ZjA4cjJxdWhzNnFsOWdlNmFibHBuamJAZGV2ZWxvcGVyLmdzZXJ2aWNlYWNjb3VudC5jb20iLCJzY29wZSI6Imh0dHBzOi8vd3d3Lmdvb2dsZWFwaXMuY29tL2F1dGgvZGV2c3RvcmFnZS5yZWFkX3dyaXRlIiwiYXVkIjoiaHR0cHM6Ly9hY2NvdW50cy5nb29nbGUuY29tL28vb2F1dGgyL3Rva2VuIiwiZXhwIjoxNDI5MTgyNzk0LCJpYXQiOjE0MjkxNzkxOTR9.GmvNRPu0AeqqMry0oEwyUmS44TFfuM2vdppQzVY75v8ixYvpcz1wmgQaoKyM9Ux9mjuUVRchrDqlL4yjIr14s2jg_Sg1X5RpwzQ6YaodXOsSnoj3jN_b72TqxwhhBsinTlEDjUYh2wz8KV3YVd9jf94KClkVr9ThXBvv4_wBgiA"))