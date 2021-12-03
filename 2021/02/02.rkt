#lang racket

(define (parse-commands input)
  (map (lambda (x) (string-split x " ")) input))
(define input (parse-commands (string-split (port->string (open-input-file "./input.txt")) "\n")))

(define (depth? command)
  (let ([direction (car command)]) (or (equal? "up" direction) (equal? "down" direction))))
(define (forward? command)
  (equal? "forward" (car command)))
(define (depth-operation command)
  (match (car command)
    ["up" -]
    ["down" +]
    [_ assert-unreachable]))

(define (calculate-position input x y)
  (define (move-x command)
    (+ x (string->number (cadr command))))
  (define (move-y command)
    ((depth-operation command) y (string->number (cadr command))))
  (if (empty? input)
      (* x y)
      (let ([head (car input)] [tail (cdr input)])
        (cond
          [(depth? head) (calculate-position tail x (move-y head))]
          [(forward? head) (calculate-position tail (move-x head) y)]
          [else assert-unreachable]))))
(calculate-position input 0 0)

(define (calculate-position-with-aim input x y aim)
  (define (move-x command)
    (+ x (string->number (cadr command))))
  (define (move-y command)
    (+ y (* aim (string->number (cadr command)))))
  (define (move-aim command)
    ((depth-operation command) aim (string->number (cadr command))))
  (if (empty? input)
      (* x y)
      (let ([head (car input)] [tail (cdr input)])
        (cond
          [(depth? head) (calculate-position-with-aim tail x y (move-aim head))]
          [(forward? head) (calculate-position-with-aim tail (move-x head) (move-y head) aim)]
          [else assert-unreachable]))))
(calculate-position-with-aim input 0 0 0)
