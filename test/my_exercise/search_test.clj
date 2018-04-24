(ns my-exercise.search-test
  (:require [clojure.test :refer :all]
            [my-exercise.search :refer :all]))

(deftest search-test
  (testing "q is a function that can be executed"
    (is (function? q)))
  (testing "url is not a function that can be executed"
    (is (not (function? url))))
  (testing "q throws an error with wrong arguments"
    (is (thrown? Exception (q []))))
  (testing "q returns a string with argument"
    (is (not (empty? (q "hello" "world"))))))
