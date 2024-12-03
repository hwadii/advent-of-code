package day03

import scala.io.Source
import scala.math.abs

@main def part1: Unit =
  println(part1("input.txt"))

@main def part2: Unit =
  println(part2("input.txt"))

def readFile(path: String) =
  Source.fromFile(path).getLines

def part1(path: String): Int =
  val contents = readFile(path).mkString
  val pattern = """mul\((\d+),(\d+)\)""".r
  pattern.findAllMatchIn(contents).map((m) => m.group(1).toInt * m.group(2).toInt).sum

def part2(path: String): Int =
  val contents = readFile(path).mkString
  val pattern = """(mul\((\d+),(\d+)\)|do\(\)|don't\(\))""".r
  var enabled = true
  var count = 0
  for (m <- pattern.findAllMatchIn(contents)) {
    if (m.group(1) == "do()") enabled = true
    else if (m.group(1) == "don't()") enabled = false
    else if (enabled) count += m.group(2).toInt * m.group(3).toInt
  }
  count
