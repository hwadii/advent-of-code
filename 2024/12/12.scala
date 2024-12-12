package day12

import scala.io.Source

@main def part1: Unit =
  println(part1("sample.txt"))

// @main def part2: Unit =
//   println(part2("input.txt"))

def readFile(path: String): List[List[Char]] =
  Source.fromFile(path).getLines.map(_.toCharArray.toList).toList

class Point(val x: Int, val y: Int):
  def +(other: Point): Point = Point(x + other.x, y + other.y)

  def -(other: Point): Point = Point(x - other.x, y - other.y)

  override def toString: String =
    s"Point: ($x, $y)"

class Plot(val kind: Char, val position: Point)

def part1(path: String): Long =
  val contents = readFile(path)
  println(contents)
  0
