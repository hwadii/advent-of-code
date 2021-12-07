#lang racket

(define (parse-ns in) (map string->number (string-split in ",")))
(define input
  (call-with-input-file "./input.txt"
                        (lambda (in) (parse-ns (read-line in)))))
(define max-pos (apply max input))
(define (first-ns-sum n) (/ (* n (add1 n)) 2))

(define (cheapest-position method)
  (define (cost start current-min)
    (define costs (apply + (map (method start) input)))
    (if (eq? start max-pos)
      current-min
      (cost (add1 start) (min costs current-min))))
  (cost 0 +inf.0))

(define (constant-rate start) (lambda (x) (abs (- x start))))
(define (non-constant-rate start) (lambda (x) (first-ns-sum (abs (- x start)))))

(module+ test
  (require rackunit)
  (check-= (cheapest-position constant-rate) 331067 0)
  (check-= (cheapest-position non-constant-rate) 92881128 0))
