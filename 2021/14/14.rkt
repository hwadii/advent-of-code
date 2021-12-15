#lang racket

(define-values (template insertion-rules)
  (call-with-input-file "input.txt"
                        (lambda (in)
                          (define template (string->list (read-line in)))
                          (void (read-line in))
                          (define rules
                            (for/hash ([d (in-lines in)])
                              (let ([p (string-split d " -> ")])
                                (values (string->list (car p)) (first (string->list (cadr p)))))))
                          (values template rules))))

(define (part1 max-step)
  (define (polymerize current [polymer '()])
    (if (= (length current) 1)
        (append polymer current)
        (polymerize (cdr current)
                    (append polymer
                            (list (car current)
                                  (hash-ref insertion-rules (list (car current) (cadr current))))))))
  (define polymers
    (for*/fold ([result template]) ([step (in-range max-step)] [p (in-value (polymerize result))])
      p))
  (let ([cs (for/fold ([counts (hash)]) ([letter (in-list polymers)])
              (hash-update counts letter add1 0))])
    (- (apply max (hash-values cs)) (apply min (hash-values cs)))))

(module+ test
  (require rackunit)
  (check-equal? (part1 10) 2375)
  (check-equal? (part1 40) 1976896901756))
