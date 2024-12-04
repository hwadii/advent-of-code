package day04

import scala.io.Source

@main def part1: Unit =
  println(part1("input.txt"))

@main def part2: Unit =
  println(part2("input.txt"))

class Point(val x: Int, val y: Int):
  def *(scalar: Int): Point = Point(scalar * x, scalar * y)

  def +(other: Point): Point = Point(x + other.x, y + other.y)

  def within(bounds: Point) = 0 <= x && x < bounds.x && 0 <= y && y < bounds.y

  override def toString: String =
    s"($x, $y)"

enum Direction(val point: Point):
  case W extends Direction(Point(0, -1))
  case Sw extends Direction(Point(1, -1))
  case S extends Direction(Point(1, 0))
  case Se extends Direction(Point(1, 1))
  case E extends Direction(Point(0, 1))
  case Ne extends Direction(Point(-1, 1))
  case N extends Direction(Point(-1, 0))
  case Nw extends Direction(Point(-1, -1))

def readFile(path: String) =
  Source.fromFile(path).getLines.map(_.split("").toList).toList

def part1(path: String): Int =
  val contents = readFile(path)
  var count = 0
  val bounds = Point(contents(0).length, contents.length)
  def xmas(p: Point): Int =
    Direction.values
      .map(d =>
        List(p)
          .concat((1 to 3).map(p + d.point * _))
          .filter(_.within(bounds))
          .map(p => contents(p.x)(p.y))
          .mkString
      )
      .count(_ == "XMAS")
  contents.zipWithIndex.foreach { (row, i) =>
    row.zipWithIndex.foreach { (column, j) =>
      count += (if column == "X" then xmas(Point(i, j)) else 0)
    }
  }
  count

def part2(path: String): Int =
  val contents = readFile(path)
  var count = 0
  val bounds = Point(contents(0).length, contents.length)
  def xmas(p: Point): Int =
    val slash = List(p + Direction.Nw.point, p + Direction.Se.point)
      .filter(_.within(bounds))
      .map(p => contents(p.x)(p.y))
      .sorted
      .mkString
    val backslash = List(p + Direction.Ne.point, p + Direction.Sw.point)
      .filter(_.within(bounds))
      .map(p => contents(p.x)(p.y))
      .sorted
      .mkString
    if slash == "MS" && backslash == "MS" then 1 else 0
  contents.zipWithIndex.foreach { (row, i) =>
    row.zipWithIndex.foreach { (column, j) =>
      count += (if column == "A" then xmas(Point(i, j)) else 0)
    }
  }
  count
