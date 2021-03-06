;; Copyright (c) 2011-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns clojurewerkz.elastisch.rest-api.aggregations.range-aggregation-test
  (:require [clojurewerkz.elastisch.rest.document :as doc]
            [clojurewerkz.elastisch.rest :as rest]
            [clojurewerkz.elastisch.query         :as q]
            [clojurewerkz.elastisch.aggregation   :as a]
            [clojurewerkz.elastisch.fixtures :as fx]
            [clojure.test :refer :all]
            [clojurewerkz.elastisch.rest.response :refer :all]))

(use-fixtures :each fx/reset-indexes fx/prepopulate-people-index)

(let [conn (rest/connect)]
  (deftest ^{:rest true :aggregation true} test-range-aggregation
  (let [index-name   "people"
        mapping-type "person"
        response     (doc/search conn index-name mapping-type
                                 :query (q/match-all)
                                 :aggregations {:age_ranges (a/range "age" [{:from 15 :to 20}
                                                                            {:from 21 :to 25}
                                                                            {:from 26 :to 30}
                                                                            {:from 31}])})
        agg          (aggregation-from response :age_ranges)]
    (is (= {:buckets [{:key "15.0-20.0", :from 15.0, :from_as_string "15.0", :to 20.0, :to_as_string "20.0", :doc_count 0}
           {:key "21.0-25.0", :from 21.0, :from_as_string "21.0", :to 25.0, :to_as_string "25.0", :doc_count 1}
           {:key "26.0-30.0", :from 26.0, :from_as_string "26.0", :to 30.0, :to_as_string "30.0", :doc_count 2}
           {:key "31.0-*", :from 31.0, :from_as_string "31.0", :doc_count 1}]}
           agg)))))
