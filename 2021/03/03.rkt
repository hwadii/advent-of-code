#lang racket

(define input (string-split (port->string (open-input-file "./input.txt")) "\n"))

(define (input-half in)
  (/ (length in) 2))

(define (make-counts input)
  (define (count-zero value n)
    (match value
      [#\0 (add1 n)]
      [#\1 n]
      [_ assert-unreachable]))
  (define (make-line-count line counts)
    (map count-zero line counts))
  (define (build-counts in counts)
    (if (null? in)
        counts
        (build-counts (rest in) (make-line-count (string->list (first in)) counts))))
  (build-counts input (make-list (string-length (first input)) 0)))

(define (kind-common kind counts in-half)
  (map (lambda (count) (if (kind count in-half) #\0 #\1)) counts))
(define (most-common counts [in-half (input-half input)])
  (kind-common > counts in-half))
(define (least-common counts [in-half (input-half input)])
  (kind-common <= counts in-half))

; Part 1
(define (power-consumption input)
  (define (mult-binary as bs)
    (* (string->number (list->string as) 2) (string->number (list->string bs) 2)))
  (define counts (make-counts input))
  (mult-binary (most-common counts) (least-common counts)))
(power-consumption input)

; Part 2
(define (life-support input)
  (define (one? xs)
    (= (length xs) 1))
  (define (find-last in commonness [pos 0])
    (define counts (make-counts in))
    (define commons (commonness counts (input-half in)))
    (define (keep? line)
      (eq? (list-ref (string->list line) pos) (list-ref commons pos)))
    (if (one? in)
      (string->number (first in) 2)
      (find-last (filter keep? in) commonness (add1 pos)))
    )
  (* (find-last input most-common) (find-last input least-common)))
(life-support input)
