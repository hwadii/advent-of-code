#lang racket

(define data
  (call-with-input-file
   "input.txt"
   (lambda (in)
     (for/list ([line (in-lines in)])
       (match (string-split line " | ")
         [(list input output)
          (list (map (compose1 list->set string->list) (string-split input))
                (map (compose1 list->set string->list) (string-split output)))])))))

(define digits-unique
  (let ([outputs (flatten (map cadr data))])
    (count (lambda (xs) (memv (set-count xs) '(2 3 4 7))) outputs)))

(define != (compose1 not eqv?))
(define (deduce css)
  (define (first-where valid?)
    (for/first ([cs (in-list css)] #:when (valid? cs))
      cs))
  (define n1 (first-where (lambda (cs) (= (set-count cs) 2))))
  (define n4 (first-where (lambda (cs) (= (set-count cs) 4))))
  (define n7 (first-where (lambda (cs) (= (set-count cs) 3))))
  (define n8 (first-where (lambda (cs) (= (set-count cs) 7))))
  (define n9 (first-where (lambda (cs) (and (= (set-count cs) 6) (subset? n4 cs)))))
  (define n0 (first-where (lambda (cs) (and (= (set-count cs) 6) (subset? n1 cs) (!= cs n9)))))
  (define n6 (first-where (lambda (cs) (and (= (set-count cs) 6) (not (member cs (list n0 n9)))))))
  (define n3 (first-where (lambda (cs) (and (= (set-count cs) 5) (subset? n1 cs)))))
  (define n5 (first-where (lambda (cs) (and (= (set-count cs) 5) (subset? cs n9) (!= cs n3)))))
  (define n2 (first-where (lambda (cs) (and (= (set-count cs) 5) (not (member cs (list n3 n5)))))))
  (list n0 n1 n2 n3 n4 n5 n6 n7 n8 n9))

(define (xs-to-x xs)
  (string->number (string-join (map number->string xs) "")))
(define digits-sum
  (for/sum ([d (in-list data)])
           (define mapping (deduce (car d)))
           (define digits
             (for/list ([xs (in-list (cadr d))])
               (index-of mapping xs)))
           (xs-to-x digits)))

(module+ test
  (require rackunit)
  (check-= digits-unique 342 0)
  (check-= digits-sum 1068933 0))
