#lang racket

(define-values
  (positions folds)
  (call-with-input-file
    "./input.txt"
    (lambda (in)
      (define input (string-split (port->string in) "\n\n"))
      (values
        (for/set ([d (in-list (string-split (car input) "\n"))])
               (let ([pos (string-split d ",")])
                 (cons (string->number (car pos)) (string->number (cadr pos)))))
        (for/list ([d (in-list (string-split (cadr input) "\n"))])
          (match (string-split d)
            [(list _ _ fold) (let ([f (string-split fold "=")])
                               (cons (car f) (string->number (cadr f))))]))))))

(define (part1 poss folds)
  (define (do-fold poss fold)
    (define horizontal? (string=? (car fold) "y"))
    (define vertical? (not horizontal?))
    (define threshold (cdr fold))
    (for*/set ([p (in-set poss)] [x (in-value (car p))] [y (in-value (cdr p))])
              (cond
                [(and horizontal? (> y threshold)) (cons x (- y (* 2 (- y threshold))))]
                [(and vertical? (> x threshold)) (cons (- x (* 2 (- x threshold))) y)]
                [else (cons x y)])))
  (define (make-fold poss folds)
    (if (null? folds) poss (make-fold (do-fold poss (car folds)) (cdr folds))))
  (make-fold poss folds))

(define (part2 poss folds)
  (define result (part1 (set-copy poss) folds))
  (define-values (width height)
    (values (apply max (set-map result car)) (apply max (set-map result cdr))))
  (for/list ([i (in-range (add1 height))])
    (string-join (map (lambda (j) (if (set-member? result (cons j i)) "#" "."))
                      (range (add1 width))))))

(module+ test
  (require rackunit)
  (check-equal? (length (set->list (part1 (set-copy positions) (list (first folds))))) 671)
  (check-equal? (part2 positions folds)
                '("# # # . . . # # . . # # # . . # . . # . . # # . . # # # . . # . . # . # . . ."
                  "# . . # . # . . # . # . . # . # . . # . # . . # . # . . # . # . # . . # . . ."
                  "# . . # . # . . . . # . . # . # # # # . # . . # . # . . # . # # . . . # . . ."
                  "# # # . . # . . . . # # # . . # . . # . # # # # . # # # . . # . # . . # . . ."
                  "# . . . . # . . # . # . . . . # . . # . # . . # . # . # . . # . # . . # . . ."
                  "# . . . . . # # . . # . . . . # . . # . # . . # . # . . # . # . . # . # # # #")))
