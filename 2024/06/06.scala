package day06

import scala.io.Source
import scala.util.control.Breaks._
import java.lang.IndexOutOfBoundsException

@main def part1: Unit =
  println(part1("input.txt"))

@main def part2: Unit =
  println(part2("input.txt"))

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
  def nextDirection: Direction =
    this.direction match
      case Direction.E => Direction.S
      case Direction.S => Direction.W
      case Direction.N => Direction.E
      case Direction.W => Direction.N

  def move(predicate: Point => Boolean): Guard =
    var direction = this.direction
    var tentative = this.position + this.direction.point
    while (predicate(tentative)) {
      direction = direction match
        case Direction.E => Direction.S
        case Direction.S => Direction.W
        case Direction.N => Direction.E
        case Direction.W => Direction.N
      tentative = this.position + direction.point
    }
    Guard(tentative, direction)

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

def positions(map: List[List[String]], guard: Guard): List[Point] =
  var positions = List[Point](guard.position)
  var moving = guard
  try {
    while (true) {
      moving = moving.move(t => map(t.y)(t.x) == "#")
      positions = positions.appended(moving.position)
    }
    positions.distinctBy((p) => (p.x, p.y))
  } catch {
    case ex: IndexOutOfBoundsException => positions.distinctBy((p) => (p.x, p.y))
  }

def part1(path: String): Int =
  val contents = readFile(path)
  val start = contents.replaceAll("\n", "").zipWithIndex.find((c, _) => c == '^').get
  val map = contents.split("\n").map(_.split("").toList).toList
  val position = Point(start._2 % map(0).length, start._2 / map.length)
  val guard = Guard(position, Direction.N)
  positions(map, guard).length

def part2(path: String): Int =
  val contents = readFile(path)
  val start = contents.replaceAll("\n", "").zipWithIndex.find((c, _) => c == '^').get
  val map = contents.split("\n").map(_.split("").toList).toList
  val position = Point(start._2 % map(0).length, start._2 / map.length)
  val initial = Guard(position, Direction.N)
  def isCycle(obstacle: Point): Boolean =
    var moving: Guard | Null = null
    var iter = 0
    try {
      while (iter < 25_000) {
        if (moving == null)
          moving = Guard(initial.position, initial.direction)
        moving = moving.move(t => t == obstacle || map(t.y)(t.x) == "#")
        iter += 1
      }
      iter == 25_000
    } catch {
      case ex: IndexOutOfBoundsException => false
    }
  positions(map, initial).filter(_ != position).filter(isCycle(_)).length
