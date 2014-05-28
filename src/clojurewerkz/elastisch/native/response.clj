;; Copyright (c) 2011-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns clojurewerkz.elastisch.native.response)

;;
;; API
;;

(defn created?
  [m]
  (:created m))

(defn ok?
  [m]
  (created? m))

(defn found?
  [m]
  (true? (get m :found)))

(defn not-found?
  [m]
  (false? (:found m)))

(defn acknowledged?
  [m]
  (:acknowledged m))

(defn created-or-acknowledged?
  [m]
  (or (created? m)
      (acknowledged? m)))

(defn accepted?
  [m]
  (:accepted m))

(defn valid?
  "Returns true if a validation query response indicates valid query, false otherwise"
  [m]
  (:valid m))

(defn timed-out?
  [m]
  (:timed_out m))

(defn total-hits
  "Returns number of search hits from a response"
  [m]
  (get-in m [:hits :total]))

(defn count-from
  "Returns total number of search hits from a response"
  [m]
  (get m :count))

(defn any-hits?
  "Returns true if a response has any search hits, false otherwise"
  [m]
  (> (total-hits m) 0))

(def no-hits? (complement any-hits?))

(defn hits-from
  "Returns search hits from a response as a collection. To retrieve hits overview, get the :hits
   key from the response"
  [m]
  (get-in m [:hits :hits]))

(defn facets-from
  "Returns facets information (overview and actual facets) from a response as a map. Keys in the map depend on
   the facets query performed"
  [m]
  (get m :facets {}))

(defn suggestions-from
  "Returns suggestion information from a response as a map. Keys in the map depend on
   the suggestions requested"
  [m]
  (get m :suggestions {}))

(defn ids-from
  "Returns search hit ids from a response"
  [m]
  (if (any-hits? m)
    (set (map :_id (hits-from m)))
    #{}))

(defn matches-from
  "Returns matches from a percolation response as a collection."
  [m]
  (get m :matches []))
