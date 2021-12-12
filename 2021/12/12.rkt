#lang racket

(define data
  (call-with-input-file "input.txt"
                        (lambda (in)
                          (for/list ([d (in-lines in)])
                            (define l (string-split d "-"))
                            (cons (car l) (cadr l))))))
(define (has v pair)
  (or (string=? v (car pair)) (string=? v (cdr pair))))
(define (adjacent-vertices vertice)
  (let ([edges (filter (lambda (pair) (has vertice pair)) data)])
    (filter (lambda (v) (not (string=? v vertice))) (flatten edges))))
(define vertices (list->set (flatten data)))
(define ed
  (for/hash ([v (in-set vertices)])
    (values v (adjacent-vertices v))))

(define (small-cave? w)
  (andmap char-lower-case? (string->list w)))
(define (big-cave? w)
  (not (small-cave? w)))
(define (start? w)
  (string=? w "start"))
(define (end? w)
  (string=? w "end"))

(define (paths [allow-twice? false])
  (define (search [path (list "start")] [twice? (not allow-twice?)])
    (if (end? (first path))
      (list path)
      (let ([cave (first path)])
              (apply append
                     (for*/list ([c (in-list (hash-ref ed cave))]
                                 #:unless (start? c)
                                 [small? (in-value (small-cave? c))]
                                 [visited? (in-value (member c path))]
                                 #:when (or (not small?) (not visited?) (not twice?)))
                       (search (cons c path) (or twice? (and small? visited?))))))))
  (search))

(module+ test
  (require rackunit)
  (check-equal? (length (paths)) 3563)
  (check-equal? (length (paths true)) 105453))
