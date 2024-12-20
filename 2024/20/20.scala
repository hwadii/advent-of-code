package day20

import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashSet
import scala.util.chaining.*

@main def part1: Unit =
  println(part1("input.txt"))

@main def part2: Unit =
  println(part2("input.txt"))

def readFile(path: String): ArrayBuffer[ArrayBuffer[Char]] =
  ArrayBuffer.from(Source.fromFile(path).getLines).map(s => ArrayBuffer.from(s.toCharArray()))

class Racetrack(val points: ArrayBuffer[ArrayBuffer[Char]]):
  val start = find('S')
  val end = find('E')
  val distance = points.map(row => row.filter(_ != '#').length).sum - 1

  def get(point: Point): Option[Char] =
    if contains(point) then Some(points(point.y)(point.x)) else None

  def find(c: Char): Point =
    var position = Point(0, 0)
    points.zipWithIndex.foreach((rows, j) =>
      rows.zipWithIndex.foreach((char, i) => if char == c then position = Point(i, j))
    )
    position

  def isWall(point: Point): Boolean =
    points(point.y)(point.x) match
      case '#' => true
      case _   => false

  def contains(point: Point) =
    0 <= point.x && point.x < points(0).length && 0 <= point.y && point.y < points.length

  def manhattanDistanceBetween(point: Point, other: Point): Int =
    scala.math.abs(point.x - other.x) + scala.math.abs(point.y - other.y)

  def distanceBetween(point: Point, other: Point, path: ArrayBuffer[Point]): Int =
    scala.math.abs(path.indexOf(point) - path.indexOf(other))

  def possibleEnds(point: Point, length: Int): List[Point] =
    val left = -length
    val right = length
    val up = -length
    val down = length
    val ys = (up to down)
    val xs = (left to right)
    ys
      .flatMap(j =>
        xs.map { i =>
          val next = Point(point.x + i, point.y + j)
          if manhattanDistanceBetween(point, next) <= length then Some(next)
          else None
        }
      )
      .filter(_.isDefined)
      .map(_.get)
      .filter(p => contains(p) && !isWall(p))
      .toList

  override def toString: String = points.map(_.mkString).mkString("\n")

  def shortcuts(count: Int, time: Int): List[Shortcut] =
    var cur = start
    val visited = HashSet[Point]()
    val shortcuts = HashSet[Shortcut]()
    val path = ArrayBuffer[Point](cur)
    while cur != end do
      visited.addOne(cur)
      val next =
        List(
          cur + Direction.Left,
          cur + Direction.Right,
          cur + Direction.Up,
          cur + Direction.Down
        )
          .filter(next => contains(next) && !isWall(next) && !visited.contains(next))
          .head
      shortcuts.addAll(possibleEnds(cur, count).map(end => Shortcut(cur, end)))
      path.addOne(next)
      cur = next
    shortcuts
      .filter(s =>
        distance - (distance + manhattanDistanceBetween(s.start, s.end) - distanceBetween(
          s.start,
          s.end,
          path
        )) >= time
      )
      .toList

class Shortcut(val start: Point, val end: Point):
  override def toString: String =
    s"$start -> $end"

  override def hashCode(): Int = start.hashCode + end.hashCode

  override def equals(any: Any): Boolean =
    any match
      case value: Shortcut =>
        (start == value.start && end == value.end) || (start == value.end && end == value.start)
      case _ => false

class Point(val x: Int, val y: Int):
  def +(other: Point): Point = Point(x + other.x, y + other.y)

  def +(other: Direction): Point = this + other.point

  def -(other: Point): Point = Point(x - other.x, y - other.y)

  override def toString: String =
    s"($x, $y)"

  override def hashCode(): Int = (x, y).hashCode

  override def equals(any: Any): Boolean =
    any match
      case value: Point => x == value.x && y == value.y
      case _            => false

class Program(val point: Point):
  override def toString: String = s"$point"

  override def hashCode(): Int = point.hashCode

  override def equals(any: Any): Boolean =
    any match
      case value: Program => point == value.point
      case _              => false

enum Direction(val point: Point):
  case Up extends Direction(Point(0, -1))
  case Down extends Direction(Point(0, 1))
  case Left extends Direction(Point(-1, 0))
  case Right extends Direction(Point(1, 0))

def part1(path: String): Int =
  val contents = readFile(path)
  Racetrack(contents).shortcuts(2, 100).length

def part2(path: String): Int =
  val contents = readFile(path)
  Racetrack(contents).shortcuts(20, 100).length
