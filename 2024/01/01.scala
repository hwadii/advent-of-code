package day01

import scala.io.Source
import scala.math.abs

@main def part1: Unit =
  println(part1("input.txt"))

@main def part2: Unit =
  println(part2("input.txt"))

def readFile(path: String): List[String] =
  Source.fromFile(path).getLines.toList

def parse(contents: List[String]): List[List[Int]] =
  contents.map(_.split("\\s+").map(_.toInt).toList)

def part1(path: String): Int =
  val contents = parse(readFile(path))
  val left = contents.map(_.head).sorted
  val right = contents.map(_.last).sorted
  left.zip(right).map((l, r) => abs(l - r)).sum

def part2(path: String) =
  val contents = parse(readFile(path))
  val left = contents.map(_.head)
  val right = contents.map(_.last)
  val occurences = right.groupBy(identity).view.mapValues(_.length)
  left.map((n) => n * occurences.getOrElse(n, 0)).sum
