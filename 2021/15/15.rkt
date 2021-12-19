#lang racket

(define-values (risks last-pair)
  (call-with-input-file
   "input.txt"
   (lambda (in)
     (for*/fold ([risks null] [l 0] #:result (values (make-hash risks) (car (last risks))))
                ([d (in-lines in)]
                 [levels (in-value (filter-map string->number (string-split d "")))])
       (values
        (append risks (map (lambda (risk x) (cons (cons x l) risk)) levels (range (string-length d))))
        (+ l 1))))))

(define (dijkstra risks [start (cons 0 0)])
  (define Q (list->set (hash-keys risks)))
  (define distances
    (hash-copy (for/hash ([posn (in-hash-keys risks)])
                 (values posn (if (equal? posn start) 0 +inf.0)))))
  (define predecessors
    (hash-copy (for/hash ([posn (in-hash-keys risks)])
                 (values posn null))))
  (define (find-min q)
    (for/fold ([mini +inf.0] [vert -1] #:result vert)
              ([v (in-set q)] #:when (< (hash-ref distances v) mini))
      (values (hash-ref distances v) v)))
  (define (next-pos q curr)
    (define right (cons (add1 (car curr)) (cdr curr)))
    (define bottom (cons (car curr) (add1 (cdr curr))))
    (define neighbors (filter (lambda (posn) (set-member? q posn)) (list right bottom)))
    (for* ([other (in-list neighbors)]
           [alt (in-value (+ (hash-ref distances curr) (hash-ref risks other)))]
           #:when (< alt (hash-ref distances other)))
      (hash-set! distances other alt)
      (hash-set! predecessors other curr)))
  (define (make-prevs q)
    (if (set-empty? q)
        predecessors
        (let ([u (find-min q)])
          (if (equal? u last-pair)
              predecessors
              (begin
                (next-pos q u)
                (make-prevs (set-remove q u)))))))
  (define (make-path prevs [S null] [u last-pair] [risk 0])
    (if (and (not (hash-has-key? prevs u)) (not (equal? u start)))
      (- risk (hash-ref risks start))
      (make-path prevs (cons u S) (hash-ref prevs u) (+ risk (hash-ref risks u)))))
  (make-path (make-prevs Q)))
(dijkstra risks)
