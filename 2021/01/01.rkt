#lang racket

(define (parse-ns input)
  (map string->number input))
(define input (parse-ns (string-split (port->string (open-input-file "./input.txt")) "\n")))

; Part 1
(define (n-increase ns n)
  (define (inc-if-increased a b)
    (if (< a b) (add1 n) n))
  (define (one? l)
    (= (length l) 1))
  (if (one? ns) n (n-increase (cdr ns) (inc-if-increased (car ns) (cadr ns)))))
(n-increase input 0)

; Part 2
(define (three-ns ns moving-ns as out)
  (if (empty? moving-ns)
      (reverse (cons (reverse as) out))
      (cond
        [(= (length as) 3) (three-ns (cdr ns) (cdr ns) '() (cons (reverse as) out))]
        [else (three-ns ns (cdr moving-ns) (cons (car moving-ns) as) out)])))
(define (n-increase-three input)
  (define (sum l)
    (foldl + 0 l))
  (define ns (three-ns input input '() '()))
  (n-increase (map sum ns) 0))
(n-increase-three input)
