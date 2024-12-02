package day02

import scala.io.Source
import scala.math.abs

@main def part1: Unit =
  println(part1("input.txt"))

@main def part2: Unit =
  println(part2("input.txt"))

def readFile(path: String): List[String] =
  Source.fromFile(path).getLines.toList

def part1(path: String): Int =
  readFile(path).map(_.split(" ").map(_.toInt).toList)
    .map(row => row.sliding(2).map(_.foldRight(0)(_ - _)).toList)
    .count(xs => xs.forall(n => (1 to 3).contains(n)) || xs.forall(n => (-3 to -1).contains(n)))

def part2(path: String): Int =
  def withoutOne(row: List[Int]): List[List[Int]] =
    row.zipWithIndex.map((_, i) => row.zipWithIndex.filter((_, j) => i != j).map((n, _) => n))
  readFile(path).map(_.split(" ").map(_.toInt).toList)
    .map(withoutOne)
    .map(rows => rows.map(_.sliding(2).map(_.foldRight(0)(_ - _)).toList))
    .count(xxs => xxs.exists(xs => xs.forall(n => (1 to 3).contains(n)) || xs.forall(n => (-3 to -1).contains(n))))
