#lang racket

(define input (string-split (port->string (open-input-file "./input.txt")) "\n"))
(define != (compose1 not equal?))
(define (parse-pairs-of-pairs input)
  (define (parse-pair-of-pair pair)
    (string-split pair " -> "))
  (map parse-pair-of-pair input))
(define (parse-pairs input)
  (define parsed-pairs-of-pairs (parse-pairs-of-pairs input))
  (define (parse-pair pair)
    (map string->number (string-split pair ",")))
  (define (make-pair input)
    (cons (car input) (cadr input)))
  (define (split-pairs pairs)
    (map (compose1 make-pair parse-pair) pairs))
  (define (straight-line? line)
    (or (= (caar line) (caadr line)) (= (cdar line) (cdadr line))))
  (map split-pairs parsed-pairs-of-pairs))

(define lines (parse-pairs input))

; '(((0 . 9) (5 . 9))
;   ((9 . 4) (3 . 4))
;   ((2 . 2) (2 . 1))
;   ((7 . 0) (7 . 4))
;   ((0 . 9) (2 . 9))
;   ((3 . 4) (1 . 4)))

(define (covered-points lines)
  (define (make-points a b)
    (define (make-points-from-range range-and-fixed)
      (match range-and-fixed
        [(list fixed (list some-range ...)) (map (lambda (value) (cons fixed value)) some-range)]
        [(list (list some-range ...) fixed) (map (lambda (value) (cons value fixed)) some-range)]
        [_ assert-unreachable]))
    (define (determine-moving-range a b)
      (define (make-range pair)
        (let ([a (car pair)]
              [b (cdr pair)])
          (inclusive-range a b (if (> a b) -1 1))))
      (cond
        [(and (= (car a) (car b)) (!= (cdr a) (cdr b))) (make-points-from-range (list (car a) (make-range (cons (cdr a) (cdr b)))))]
        [(and (= (cdr a) (cdr b)) (!= (car a) (car b))) (make-points-from-range (list (make-range (cons (car a) (car b))) (cdr a)))]
        [else (map (lambda (x y) (cons x y)) (make-range (cons (car a) (car b))) (make-range (cons (cdr a) (cdr b))))]))
    (determine-moving-range a b))
  (append-map (lambda (pair) (make-points (car pair) (cadr pair))) lines))

(define (tally points)
  (define (make-tally points counts)
    (if (null? points)
      counts
      (make-tally (cdr points) (hash-update counts (car points) add1 0))))
  (make-tally points (hash)))
(define (count-two-or-more tally)
  (for/fold ([count 0]) ([(point count-covered) tally])
    (if (> count-covered 1) (add1 count) count)))

(count-two-or-more (tally (covered-points lines)))
