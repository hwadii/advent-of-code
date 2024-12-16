package day15

import scala.io.Source
import scala.collection.mutable.ArrayBuffer

@main def part1: Unit =
  println(part1("input.txt"))

@main def part2: Unit =
  println(part2("sample.txt"))

def readFile(path: String): List[String] =
  Source.fromFile(path).mkString.split("\n\n").toList

class Warehouse(var points: ArrayBuffer[ArrayBuffer[Char]]):
  var robot = start

  def get(point: Point): Char =
    points(point.y)(point.x)

  def update(point: Point, to: Point): Unit =
    points(to.y)(to.x) = points(point.y)(point.x)
    points(point.y)(point.x) = '.'

  def resize: Warehouse =
    this.points = this.points.zipWithIndex.map((row, j) => {
      row.flatMap(point => {
        point match
          case '.' => List('.', '.')
          case 'O' => List('[', ']')
          case '#' => List('#', '#')
          case '@' => List('@', '.')
      })
    })
    this

  def start: Robot =
    val point = points
      .zipWithIndex.flatMap((rows, j) =>
        rows
          .zipWithIndex
          .map((point, i) => if point == '@' then Some(Point(i, j)) else None)
      )
    Robot(point.find(p => p.isDefined).get.get)

  def step(direction: Direction): Warehouse =
    val next = robot + direction.point
    if (get(next.point) == '#')
      this
    else if (get(next.point) == '.')
      update(robot.point, next.point)
      robot = next
      this
    else
      var toShift = ArrayBuffer[Point](robot.point)
      var nextEmpty = next.point
      while (get(nextEmpty) == 'O') {
        toShift.addOne(nextEmpty)
        nextEmpty = nextEmpty + direction.point
      }
      if (get(nextEmpty) == '#')
        this
      else
        toShift.reverse.foreach(p => update(p, p + direction.point))
        robot = next
        this
      end if
    end if

  def stepWide(direction: Direction): Warehouse =
    val next = robot + direction.point
    if (get(next.point) == '#')
      this
    else if (get(next.point) == '.')
      update(robot.point, next.point)
      robot = next
      this
    else
      var toShift = ArrayBuffer[Point](robot.point)
      var nextEmpty = next.point
      while (get(nextEmpty) == '[' || get(nextEmpty) == ']') {
        toShift.addOne(nextEmpty)
        nextEmpty = nextEmpty + direction.point
      }
      if (get(nextEmpty) == '#')
        this
      else
        toShift.reverse.foreach(p => update(p, p + direction.point))
        robot = next
        this
      end if
    end if

  def sumGps: Int =
    var sum = 0
    points.zipWithIndex.foreach((rows, j) =>
      rows.zipWithIndex.foreach((point, i) => {
        if point == 'O' then sum += 100 * j + i
      })
    )
    sum

  override def toString: String =
    points.map(_.mkString).mkString("\n")


class Point(val x: Int, val y: Int):
  def *(other: Int): Point = Point(x * other, y * other)

  def +(other: Point): Point = Point(x + other.x, y + other.y)

  def -(other: Point): Point = Point(x - other.x, y - other.y)

  override def toString: String =
    s"($x, $y)"

class Robot(val point: Point):
  def +(other: Point): Robot = Robot(point + other)

  override def toString: String = point.toString

enum Direction(val point: Point):
  case Up extends Direction(Point(0, -1))
  case Down extends Direction(Point(0, 1))
  case Left extends Direction(Point(-1, 0))
  case Right extends Direction(Point(1, 0))

object Direction:
  def parse(move: Char): Direction = move match
    case '>' => Direction.Right
    case '<' => Direction.Left
    case 'v' => Direction.Down
    case '^' => Direction.Up

def part1(path: String): Int =
  val contents = readFile(path)
  val warehouse = Warehouse(
    ArrayBuffer.from(contents(0).split("\n").map(c =>
      ArrayBuffer.from(c.toCharArray)
    ))
  )
  contents(1)
    .toCharArray
    .filter(_ != '\n')
    .map(Direction.parse(_))
    .toList
    .foreach(warehouse.step(_))
  warehouse.sumGps

def part2(path: String): Int =
  val contents = readFile(path)
  val warehouse = Warehouse(
    ArrayBuffer.from(contents(0).split("\n").map(c =>
      ArrayBuffer.from(c.toCharArray)
    ))
  )
  // contents(1)
  //   .toCharArray
  //   .filter(_ != '\n')
  //   .map(Direction.parse(_))
  //   .toList
  //   .foreach(warehouse.step(_))
  println(warehouse)
  println(warehouse.resize)
  0
