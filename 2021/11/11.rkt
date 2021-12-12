#lang racket

(define data
  (call-with-input-file "input.txt"
                        (lambda (in)
                          (for/vector ([d (in-lines in)])
                            (list->vector (filter-map string->number (string-split d "")))))))
(define width (vector-length (vector-ref data 1)))
(define height (vector-length data))

(define mapping
  (let ([in-bounds? (lambda (x y) (and (>= x 0) (< x width) (>= y 0) (< y height)))])
    (for*/hash ([i (in-range width)] [j (in-range height)])
      (define adjacent-positions
        (for*/vector ([ii (in-inclusive-range (sub1 i) (add1 i))]
                      [jj (in-inclusive-range (sub1 j) (add1 j))]
                      #:when (in-bounds? ii jj)
                      #:unless (and (= ii i) (= jj j)))
          (cons ii jj)))
      (values (cons i j) adjacent-positions))))
(define energy
  (for*/hash ([i (in-range width)] [j (in-range height)])
    (values (cons i j) (vector-ref (vector-ref data j) i))))

(define (octopi energy max-step)
  (define (charge-octopus pos)
    (let ([inc (lambda (value) (if (= value 9) 0 (add1 value)))])
      (hash-update! energy pos inc)
      (hash-ref energy pos)))
  (define (do-step energy)
    (define flashed (mutable-set))
    (define (visit-adjacents pos)
      (set-add! flashed pos)
      (let ([adjacents (hash-ref mapping pos)])
        (for* ([position (in-vector adjacents)]
               [value (in-value (hash-ref energy position))]
               #:unless (set-member? flashed position)
               #:when (zero? (charge-octopus position)))
          (visit-adjacents position))))
    (for ([(pos en) (in-hash energy)]
          #:unless (set-member? flashed pos)
          #:when (zero? (charge-octopus pos)))
      (visit-adjacents pos))
    energy)
  (define (make-flashes energy [step 0] [c 0])
    (if (or (= step max-step) (= (count zero? (hash-values energy)) 100))
        (cons c step)
        (make-flashes (do-step energy) (add1 step) (+ c (count zero? (hash-values energy))))))
  (make-flashes energy))

(module+ test
  (require rackunit)
  (check-equal? (car (octopi (hash-copy energy) 100)) 1649)
  (check-equal? (cdr (octopi (hash-copy energy) 1000)) 256))
