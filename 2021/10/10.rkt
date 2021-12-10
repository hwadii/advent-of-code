#lang racket

(define data
  (call-with-input-file "input.txt"
                        (lambda (in)
                          (for/list ([d (in-lines in)])
                            (filter (lambda (s) (not (string=? s ""))) (string-split d ""))))))

(define incorrect (make-hash (list (cons ")" 3) (cons "]" 57) (cons "}" 1197) (cons ">" 25137))))
(define mapping (make-hash (list (cons "(" ")") (cons "[" "]") (cons "{" "}") (cons "<" ">"))))

(define (incorrect-chars input)
  (define (opening? c)
    (hash-has-key? mapping c))
  (define (closing? c)
    (not (opening? c)))
  (define (corresponding? open close)
    (string=? (hash-ref mapping open) close))
  (define (check-corrupted line [stack '()])
    (cond
      [(empty? line) '()]
      [(opening? (car line)) (check-corrupted (cdr line) (cons (car line) stack))]
      [(and (closing? (car line))
            (corresponding? (car stack) (car line))
            (check-corrupted (cdr line) (cdr stack)))]
      [else (car line)]))
  (for/list ([line (in-list input)])
    (check-corrupted line)))

(define part1
  (for/sum ([c (in-list (incorrect-chars data))] #:when (not (null? c))) (hash-ref incorrect c)))

(define input-incomplete
  (for/list ([d data] [incorrect (incorrect-chars data)] #:when (null? incorrect))
    d))

(define completion-points (make-hash (list (cons ")" 1) (cons "]" 2) (cons "}" 3) (cons ">" 4))))
(define (completion-strings in)
  (define (to-closing c)
    (hash-ref mapping c))
  (define (make-completion line [stack '()])
    (cond
      [(empty? line) (map to-closing stack)]
      [(hash-has-key? mapping (car line)) (make-completion (cdr line) (cons (car line) stack))]
      [else (make-completion (cdr line) (cdr stack))]))
  (for/vector ([d in])
    (for/fold ([score 0]) ([char (make-completion d)])
      (+ (* 5 score) (hash-ref completion-points char)))))
(define part2
  (vector-ref (vector-sort (completion-strings input-incomplete) <)
              (floor (/ (length input-incomplete) 2))))

(module+ test
  (require rackunit)
  (check-eq? part1 392139)
  (check-eq? part2 4001832844))
