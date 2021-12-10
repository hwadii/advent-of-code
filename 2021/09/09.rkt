#lang racket

(define data
  (call-with-input-file "input.txt"
                        (lambda (in)
                          (for/vector ([line (in-lines in)])
                            (list->vector (filter-map string->number (string-split line "")))))))
(define width (vector-length (vector-ref data 1)))
(define height (vector-length data))

(define (value-at mat x y)
  (let ([x (cond
             [(< x 0) 0]
             [(>= x width) (sub1 width)]
             [else x])]
        [y (cond
             [(< y 0) 0]
             [(>= y height) (sub1 height)]
             [else y])])
    (vector-ref (vector-ref mat y) x)))

(define (minimum? mat x y)
  (let ([value (value-at mat x y)])
    (define left? (or (= x 0) (< value (value-at mat (sub1 x) y))))
    (define right? (or (= (add1 x) width) (< value (value-at mat (add1 x) y))))
    (define top? (or (= y 0) (< value (value-at mat x (sub1 y)))))
    (define bottom? (or (= (add1 y) height) (< value (value-at mat x (add1 y)))))
    (and left? right? top? bottom?)))

; Part 1
(define part1
  (for*/sum ([i (in-range width)] [j (in-range height)] #:when (minimum? data i j))
            (add1 (value-at data i j))))

; Part 2
(define css
  (for*/list ([i (in-range width)] [j (in-range height)] #:when (minimum? data i j))
    (cons i j)))

(define (basin-sz mat x y)
  (define != (compose1 not eq?))
  (define seen (mutable-set))
  (define sz 0)
  (define (calc-sz x y)
    (define left (if (= x 0) null (cons (sub1 x) y)))
    (define top (if (= y 0) null (cons x (sub1 y))))
    (define right (if (= (add1 x) width) null (cons (add1 x) y)))
    (define bottom (if (= (add1 y) height) null (cons x (add1 y))))
    (set-add! seen (cons x y))
    (for ([pos (in-list (list left top right bottom))])
      (when (and (not (null? pos))
                 (!= (value-at mat (car pos) (cdr pos)) 9)
                 (not (set-member? seen pos)))
        (set! sz (add1 sz))
        (calc-sz (car pos) (cdr pos))))
    (add1 sz))
  (calc-sz x y))

(define part2
  (let* ([bassin-sz (for/list ([cs (in-list css)])
                      (basin-sz data (car cs) (cdr cs)))]
         [largest (apply * (take (sort bassin-sz >) 3))])
    largest))

(module+ test
  (require rackunit)
  (check-equal? part1 591)
  (check-equal? part2 1113424))
