#lang racket

(define input
  (map (compose1 string->number string-normalize-spaces)
       (string-split (port->string (open-input-file "./input.txt")) ",")))

; Part 1
(define (lanternfish-rec init max-days)
  (define (next-state current [next (list)] [to-add (list)])
    (if (null? current)
        (append (reverse next) to-add)
        (let ([head (first current)])
          (cond
            [(= head 0) (next-state (cdr current) (cons 6 next) (cons 8 to-add))]
            [else (next-state (cdr current) (cons (sub1 head) next) to-add)]))))
  (define (make-fish state [days 0])
    (if (= days max-days)
      state
      (make-fish (next-state state) (add1 days))))
  (make-fish init 0))
(length (lanternfish-rec input 80))

; Part 2
(define (lanternfish-iter input max-days)
  (define ages (make-vector 9 0))
  (for ([lf-age input])
    (vector-set! ages lf-age (add1 (vector-ref ages lf-age))))
  (for ([_ (in-range max-days)])
    (let ([new-fishes (vector-ref ages 0)])
      (set! ages (vector-drop ages 1))
      (set! ages (vector-append ages (vector new-fishes)))
      (vector-set! ages 6 (+ (vector-ref ages 6) new-fishes))))
  ages)
(apply + (vector->list (lanternfish-iter (list->vector input) 256)))
