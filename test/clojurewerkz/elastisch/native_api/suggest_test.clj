;; Copyright (c) 2011-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns clojurewerkz.elastisch.native-api.suggest-test
  (:require [clojurewerkz.elastisch.native.document :as doc]
            [clojurewerkz.elastisch.native          :as es]
            [clojurewerkz.elastisch.native.index    :as idx]
            [clojurewerkz.elastisch.query           :as q]
            [clojurewerkz.elastisch.fixtures        :as fx]
            [clojurewerkz.elastisch.test.helpers    :as th]
            [clojurewerkz.elastisch.native.response :refer :all]
            [clojure.test :refer :all]))

(use-fixtures :each fx/reset-indexes fx/prepopulate-articles-index fx/prepopulate-people-index fx/prepopulate-tweets-index)

(let [conn (th/connect-native-client)]
  (deftest ^{:native true} test-term-suggest-on-tags
    (let [index-name   "articles"
          mapping-type "article"
          result       (doc/search conn index-name mapping-type
                                   :query (q/match-all)
                                   :suggest { :mysuggest {:text "foo" :term {:field "tags"}}})
          suggestions       (suggestions-from result)]
      (is (= 1 (count suggestions))))))
  ;;    (is (> (-> facets :tags :total) 25))
  ;;    ;; each term is a map with 2 keys: :term and :count
  ;;    (is (-> facets :tags :terms first :term))
  ;;    (is (-> facets :tags :terms first :count))))
