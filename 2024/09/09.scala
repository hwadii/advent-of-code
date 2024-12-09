package day09

import scala.io.Source

@main def part1: Unit =
  println(part1("sample.txt"))

// @main def part2: Unit =
//   println(part2("input.txt"))

def readFile(path: String): String =
  Source.fromFile(path).mkString

def part1(path: String): Int =
  val contents = readFile(path)
  println(contents)
  0

// def part2(path: String): Int =

