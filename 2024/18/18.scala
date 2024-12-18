package day18

import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashSet
import scala.collection.mutable.Stack
import scala.util.chaining._

@main def part1: Unit =
  println(part1("sample.txt", Point(6, 6)))

@main def part2: Unit =
  println(part2("input.txt", Point(70, 70)))

def readFile(path: String): List[String] =
  Source.fromFile(path).getLines.toList

class Point(val x: Int, val y: Int):
  def +(other: Point): Point = Point(x + other.x, y + other.y)

  def +(other: Direction): Point = this + other.point

  override def toString: String = s"($x, $y)"

  override def equals(other: Any): Boolean =
    other match
      case that: Point => that.x == x && that.y == y
      case _ => false

  override def hashCode(): Int = (x, y).hashCode()

enum Direction(val point: Point):
  case Left extends Direction(Point(-1, 0))
  case Right extends Direction(Point(1, 0))
  case Up extends Direction(Point(0, -1))
  case Down extends Direction(Point(0, 1))

class Map(val points: ArrayBuffer[ArrayBuffer[Char]]):
  def contains(p: Point): Boolean = 0 <= p.x && p.x < points(0).length && 0 <= p.y && p.y < points.length

  def get(p: Point): Option[Char] =
    if contains(p) then Some(points(p.y)(p.x)) else None

  def isValid(p: Point): Boolean =
    get(p) match
      case Some(value: Char) if value != '#' => true
      case _ => false

  def validHops(p: Point): List[Point] =
    List(
      p + Direction.Left,
      p + Direction.Right,
      p + Direction.Up,
      p + Direction.Down
    )
      .filter(isValid(_))

  def exit: Option[Int] =
    val end = Point(points(0).length - 1, points.length - 1)
    val stack = Stack[Point](Point(0, 0))
    var cur: Point | Null = null
    val distances = scala.collection.mutable.Map[Point, Int]((Point(0, 0), 0))
    while (!stack.isEmpty) {
      cur = stack.pop
      val curDistance = distances.get(cur).get
      validHops(cur).foreach(p => {
        val distance = curDistance + 1
        if distance < distances.getOrElse(p, Int.MaxValue) then
          distances.addOne((p, curDistance + 1))
          stack.push(p)
      })
    }
    distances.get(end)

  override def toString: String = points.map(_.mkString).mkString("\n")

object Map:
  def parse(bounds: Point, bytes: List[Point]): Map =
    Map(
      ArrayBuffer.from((0 to bounds.y).map(y =>
        ArrayBuffer.from((0 to bounds.x).map(x =>
          if bytes.contains(Point(x, y)) then '#' else '.'
        ))
      ))
    )


def part1(path: String, bounds: Point): Int =
  val contents = readFile(path).map(_.split(",") pipe (c => Point(c(0).toInt, c(1).toInt)))
  Map.parse(bounds, contents.take(1024)).exit.get

def part2(p: String, bounds: Point): Point =
  val contents = readFile(p).map(_.split(",") pipe (c => Point(c(0).toInt, c(1).toInt)))
  var min = 1024
  var max = 16384
  var start = (min + max) / 2
  var path = Map.parse(bounds, contents).exit
  var next = Map.parse(bounds, contents.take(start + 1)).exit
  while (!path.isDefined || !next.isEmpty) {
    if (path.isDefined && next.isDefined) {
      min = start
      start = (min + max) / 2
    } else if (path.isEmpty && next.isEmpty) {
      max = start
      start = (min + max) / 2
    }
    path = Map.parse(bounds, contents.take(start)).exit
    next = Map.parse(bounds, contents.take(start + 1)).exit
  }
  contents(start)
