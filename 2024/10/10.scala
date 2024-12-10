package day10

import scala.io.Source
import scala.collection.mutable.{Stack, HashSet}

@main def part1: Unit =
  println(part1("input.txt"))

@main def part2: Unit =
  println(part2("input.txt"))

def readFile(path: String): String =
  Source.fromFile(path).mkString

enum Direction(val point: Point):
  case Left extends Direction(Point(-1, 0))
  case Right extends Direction(Point(1, 0))
  case Up extends Direction(Point(0, -1))
  case Down extends Direction(Point(0, 1))

class TopographicMap(val contents: String):
  val elevations = contents.replaceAll("\n", "")
  val grid = contents.split("\n").map(_.split("").map(_.toInt).toList).toList
  val bounds = Point(grid(0).length, grid.length)

  def contains(p: Point): Boolean = 0 <= p.x && p.x < bounds.x && 0 <= p.y && p.y < bounds.y

  def get(p: Point): Option[Int] =
    if contains(p) then Some(grid(p.y)(p.x)) else None

  def isValid(src: Point, dst: Point): Boolean =
    (get(dst), get(src)) match
      case (Some(d: Int), Some(s: Int)) => d - s == 1
      case _ => false

  def pathBetween(start: Point, end: Point): Boolean =
    val stack = Stack(start)
    val visited = HashSet[Point]()
    var current: Point | Null = null
    while (!stack.isEmpty && current != end) {
      current = stack.pop
      visited.addOne(current)
      val left = current + Direction.Left
      if (!visited.contains(left) && isValid(current, left)) stack.push(left)
      val right = current + Direction.Right
      if (!visited.contains(right) && isValid(current, right)) stack.push(right)
      val up = current + Direction.Up
      if (!visited.contains(up) && isValid(current, up)) stack.push(up)
      val down = current + Direction.Down
      if (!visited.contains(down) && isValid(current, down)) stack.push(down)
    }
    current == end

  def rating(start: Point, end: Point): Int =
    val stack = Stack(start)
    val visited = HashSet[Point]()
    var current: Point | Null = null
    var nbPaths = 0
    while (!stack.isEmpty) {
      current = stack.pop
      visited.addOne(current)
      val left = current + Direction.Left
      if (!visited.contains(left) && isValid(current, left)) stack.push(left)
      val right = current + Direction.Right
      if (!visited.contains(right) && isValid(current, right)) stack.push(right)
      val up = current + Direction.Up
      if (!visited.contains(up) && isValid(current, up)) stack.push(up)
      val down = current + Direction.Down
      if (!visited.contains(down) && isValid(current, down)) stack.push(down)
      if current == end then nbPaths += 1
    }
    nbPaths

  def paths: Int =
    val (starts, ends) = lowHighs
    starts.flatMap(s => ends.map(e => pathBetween(s, e))).count(_ == true)

  def sumRatings: Int =
    val (starts, ends) = lowHighs
    starts.flatMap(s => ends.map(e => rating(s, e))).sum

  def lowHighs: (IndexedSeq[Point], IndexedSeq[Point]) =
    elevations
      .zipWithIndex
      .map((e, i) => (e.asDigit, Point(i % bounds.x, i / bounds.y)))
      .filter((e, _) => e == 0 || e == 9)
      .partitionMap((e, p) => if e == 0 then Left(p) else Right(p))

class Point(val x: Int, val y: Int):
  def +(other: Point): Point = Point(x + other.x, y + other.y)

  def +(other: Direction): Point = this + other.point

  override def equals(any: Any): Boolean =
    any match
      case other: Point => x == other.x && y == other.y
      case _ => false

  override def toString: String =
    s"Point: ($x, $y)"

def part1(path: String): Int =
  val contents = readFile(path)
  TopographicMap(contents).paths

def part2(path: String): Int =
  val contents = readFile(path)
  TopographicMap(contents).sumRatings
