#lang racket

(define input (string-split (port->string (open-input-file "./input.txt")) "\n\n"))

(define (parse-ns ns [sep #px"\\s+"])
  (map string->number (string-split ns sep)))
(define (parse-boards boards)
  (define (split-board board)
    (string-split board "\n"))
  (define (parse-line ns)
    (parse-ns ns))
  (define (parse to-parse parsed)
    (if (null? to-parse)
        (reverse parsed)
        (let ([board (split-board (car to-parse))])
          (parse (cdr to-parse) (cons (map parse-line board) parsed)))))
  (parse boards null))

(define boards (parse-boards (rest input)))
(define numbers (parse-ns (first input) ","))

(define n-boards (length boards))
(define board-width (length (first (first boards))))
(define board-height (length (first boards)))

(define markers (make-list (length boards) (make-list board-width (make-list board-height false))))

(define (transpose board)
  (apply map list board))

(define (get-pos boards board-num x y)
  (list-ref (list-ref (list-ref boards board-num) y) x))
(define (set-pos boards board-num x y v)
  (let* ([new-line (list-set (list-ref (list-ref boards board-num) y) x v)]
         [new-board (list-set (list-ref boards board-num) y new-line)]
         [new-boards (list-set boards board-num new-board)])
    new-boards))

(define (true? v) (eq? v true))

(define (mark-ns ns boards markers markers-indices)
  (for* ([i n-boards] [j board-height] [k board-width])
    (when (= (get-pos boards i k j) (car ns))
      (set! markers (set-pos markers i k j true))))
  (define (checked? markers)
    (define (checked-markers? mks)
      ; check that every value in row is true
      (define (checked-row? row)
        (andmap true? row))
      (ormap checked-row? mks))
    (define (one-checked? markers)
      (ormap checked-markers? (list markers (transpose markers))))
    (map one-checked? markers))
  (define (fill-indices checked-list)
    (define new-indices
      (filter-map (lambda (value idx)
                    (if (and (true? value) (not (member idx markers-indices))) idx false))
                  checked-list
                  (range (length checked-list))))
    (append markers-indices new-indices))
  (let* ([checked-list (checked? markers)]
         [is-checked (andmap true? checked-list)]
         [new-indices (fill-indices checked-list)])
    (if is-checked
        (values (list-ref boards (last new-indices)) (list-ref markers (last new-indices)) (car ns))
        (mark-ns (cdr ns) boards markers new-indices))))

(define (score checked-board checked-markers last-value)
  (define (position board x y)
    (list-ref (list-ref board y) x))
  (* (for*/fold ([sum 0]) ([i board-height] [j board-width])
       (if (not (position checked-markers j i)) (+ sum (position checked-board j i)) sum))
     last-value))

(let-values ([(checked-board checked-markers last-value) (mark-ns numbers boards markers '())])
  (score checked-board checked-markers last-value))
