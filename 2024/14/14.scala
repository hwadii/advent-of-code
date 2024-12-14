package day14

import scala.io.Source
import scala.math.abs
import scala.collection.mutable.ArrayBuffer
import scala.util.control.Breaks._
import scala.util.boundary

@main def part1: Unit =
  println(part1("input.txt", Point(101, 103)))

@main def part2: Unit =
  println(part2("input.txt", Point(101, 103)))

enum Quadrant:
  case Nw
  case Ne
  case Sw
  case Se

class Robot(val point: Point, val speed: Point):
  def wrap(value: Int, limit: Int): Int =
    if (value < 0) then value % limit + limit
    else value % limit

  def step(bounds: Point): Robot =
    Robot(Point(wrap(point.x + speed.x, bounds.x), wrap(point.y + speed.y, bounds.y)), speed)

  def quadrant(bounds: Point): Option[Quadrant] =
    if (point.x < (bounds.x - 1) / 2 && point.y < (bounds.y - 1) / 2) then Option(Quadrant.Nw)
    else if (point.x > (bounds.x - 1) / 2 && point.y < (bounds.y - 1) / 2) then Option(Quadrant.Ne)
    else if (point.x < (bounds.x - 1) / 2 && point.y > (bounds.y - 1) / 2) then
      Option(Quadrant.Sw)
    else if (point.x > (bounds.x - 1) / 2 && point.y > (bounds.y - 1) / 2) then
      Option(Quadrant.Se)
    else
      None

  override def toString: String =
    s"$point at $speed"

class Point(val x: Int, val y: Int):
  def +(other: Point): Point = Point(x + other.x, y + other.y)

  def -(other: Point): Point = Point(x - other.x, y - other.y)

  override def toString: String =
    s"($x, $y)"

object Robot:
  val Pattern = """p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""".r

  def parse(input: String): Robot =
    val ms = Pattern.findAllMatchIn(input)
      .flatMap(m => List(m.group(1), m.group(2), m.group(3), m.group(4)))
      .toList
    Robot(Point(ms(0).toInt, ms(1).toInt), Point(ms(2).toInt, ms(3).toInt))

  def safetyFactor(robots: List[Robot], bounds: Point): Int =
    robots
      .map(_.quadrant(bounds))
      .filter(_.isDefined)
      .map(_.get)
      .groupBy(identity)
      .view
      .mapValues(_.length)
      .values
      .foldLeft(1)(_ * _)

  def highConcentration(robots: List[Robot]): Boolean =
    var res = false
    breakable:
     robots.foreach(robot => {
       robots.foreach(robot =>
         val count = robots.count(r => abs(r.point.x - robot.point.x) <= 1 && abs(r.point.y - robot.point.y) <= 1)
         res = count > 8
         if res then break
       )
     })
    res

def readFile(path: String): List[String] =
  Source.fromFile(path).getLines.toList

def part1(path: String, bounds: Point): Int =
  val contents = readFile(path)
  var robots = contents.map(Robot.parse(_))
  (0 until 100).foreach(_ => {
    robots = robots.map(_.step(bounds))
  })
  robots
    .map(_.quadrant(bounds))
    .filter(_.isDefined)
    .map(_.get)
    .groupBy(identity)
    .view
    .mapValues(_.length)
    .values
    .foldLeft(1)(_ * _)

def part2(path: String, bounds: Point): Int =
  val contents = readFile(path)
  var robots = contents.map(Robot.parse(_))
  var step = 0
  var min = Double.PositiveInfinity
  (0 until 1_000_000_000).foreach(_ => {
    val s = Robot.safetyFactor(robots, bounds)
    if (s < min) {
      min = s.toDouble
      val board = ArrayBuffer.fill(bounds.y)(ArrayBuffer.fill(bounds.x)("."))
      robots.foreach(robot => board(robot.point.y)(robot.point.x) = "#")
      println(board.map(_.mkString).mkString("\n"))
      println(step)
      println()
    }
    robots = robots.map(_.step(bounds))
    step += 1
  })
  0
