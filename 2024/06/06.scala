package day06

import scala.io.Source
import scala.util.control.Breaks._
import java.lang.IndexOutOfBoundsException

@main def part1: Unit =
  println(part1("input.txt"))

@main def part2: Unit =
  println(part2("sample.txt"))

def readFile(path: String): String =
  Source.fromFile(path).mkString

class Point(val x: Int, val y: Int):
  def *(scalar: Int): Point = Point(scalar * x, scalar * y)

  def +(other: Point): Point = Point(x + other.x, y + other.y)

  def within(bounds: Point) = 0 <= x && x < bounds.x && 0 <= y && y < bounds.y

  override def equals(other: Any): Boolean =
    other match
      case that: Point => that.x == x && that.y == y
      case _ => false

  override def toString: String =
    s"($x, $y)"

class Guard(var position: Point, var direction: Direction):
  def move(direction: Direction) =
    this.position += direction.point
    this.direction = direction

  def next: Direction =
    this.direction match
      case Direction.E => Direction.S
      case Direction.S => Direction.W
      case Direction.N => Direction.E
      case Direction.W => Direction.N

  def prev: Direction =
    this.direction match
      case Direction.E => Direction.N
      case Direction.S => Direction.E
      case Direction.N => Direction.W
      case Direction.W => Direction.S

  def cycled(other: Guard): Boolean =
    position == other.position && direction == other.prev

  override def toString: String =
    s"$position: $direction"

  override def equals(other: Any): Boolean =
    other match
      case that: Guard => that.position == position && that.direction == direction
      case _ => false

enum Direction(val point: Point):
  case W extends Direction(Point(-1, 0))
  case S extends Direction(Point(0, 1))
  case E extends Direction(Point(1, 0))
  case N extends Direction(Point(0, -1))

def part1(path: String): Int =
  val contents = readFile(path)
  val start = contents.replaceAll("\n", "").zipWithIndex.find((c, _) => c == '^').get
  val map = contents.split("\n").map(_.split("").toList).toList
  val bounds = Point(map(0).length, map.length)
  var position = Point(start._2 % bounds.x, start._2 / bounds.y)
  var direction = Direction.N
  var positions = List[Point](position)
  def safe(position: Point, direction: Direction): Boolean =
    (position + direction.point).within(bounds)
  val next = (direction: Direction) =>
    direction match
      case Direction.E => Direction.S
      case Direction.S => Direction.W
      case Direction.N => Direction.E
      case Direction.W => Direction.N
  try {
    while (true) {
      val tentative = position + direction.point
      if (map(tentative.y)(tentative.x) == "#") {
        direction = next(direction)
        position = position + direction.point
      } else {
        position = position + direction.point
      }
      positions = positions.appended(position)
    }
    positions.distinctBy((p) => (p.x, p.y)).length
  } catch {
    case ex: IndexOutOfBoundsException => positions.distinctBy((p) => (p.x, p.y)).length
  }

def part2(path: String): Int =
  val contents = readFile(path)
  val start = contents.replaceAll("\n", "").zipWithIndex.find((c, _) => c == '^').get
  def parseMap(idx: Int): List[List[String]] =
    contents.patch(idx, "#", 1).split("\n").map(_.split("").toList).toList
  def isCycle(idx: Int): Boolean =
    val map = parseMap(idx)
    println(map.map(_.mkString).mkString("\n"))
    println(map)
    val bounds = Point(map(0).length, map.length)
    val initial = Guard(Point(start._2 % bounds.x, start._2 / bounds.y), Direction.N)
    var moving: Guard | Null = null
    try {
      while (moving.cycled(initial)) {
        if (moving == null)
          moving = Guard(initial.position, initial.direction)
        val tentative = moving.position + moving.direction.point
        if (map(tentative.y)(tentative.x) == "#") {
          moving.direction = moving.next
          moving.position = moving.position + moving.direction.point
        } else {
          moving.position = moving.position + moving.direction.point
        }
        println(s"$initial\t$moving")
      }
      true
    } catch {
      case ex: IndexOutOfBoundsException => false
    }
  println(isCycle(69))
  0
