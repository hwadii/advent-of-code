package day16

import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Stack
import scala.collection.mutable.PriorityQueue
import scala.collection.mutable.HashSet
import scala.collection.mutable.Map

@main def part1: Unit =
  println(part1("input.txt"))

@main def part2: Unit =
  println(part2("input.txt"))

def readFile(path: String): List[String] =
  Source.fromFile(path).mkString.split("\n\n").toList

class Maze(val points: ArrayBuffer[ArrayBuffer[Char]]):
  var reindeer = Reindeer(Point(1, points.length - 2), Direction.Right)
  val end = Point(points(0).length - 2, 1)

  def get(point: Point): Option[Char] =
    if contains(point) then Some(points(point.y)(point.x)) else None

  def isValid(point: Point): Boolean =
    get(point) match
      case Some(value: Char) => value != '#'
      case None => false

  def contains(point: Point) =
    0 <= point.x && point.x < points(0).length && 0 <= point.y && point.y < points.length

  def paths: Int =
    val distances = Map[Reindeer, Int]((reindeer, 0))
    val predecessors = Map[Reindeer, ArrayBuffer[Reindeer]]()
    val pq = PriorityQueue[(Reindeer, Int)]((reindeer, 0))(
      Ordering.by((_: (Reindeer, Int))._2).reverse
    )
    while (!pq.isEmpty) {
      val (cur, curDistance) = pq.dequeue
      cur.costs.filter((r, _) => isValid(r.point)).foreach((r, cost) => {
        val distance = curDistance + cost
        if distance < distances.getOrElse(r, Int.MaxValue) then
          distances.addOne((r, distance))
          predecessors.addOne((r, ArrayBuffer(cur)))
          pq.enqueue((r, distance))
        else if (distance == distances.getOrElse(r, Int.MaxValue)) then
          val prev = predecessors.getOrElse(r, ArrayBuffer())
          prev.addOne(cur)
          predecessors.addOne((r, prev))
        end if
      })
    }
    distances.filter((r, d) => r.point == end).map(_._2).min


  def bestSeats: Int =
    val distances = Map[Reindeer, Int]((reindeer, 0))
    val predecessors = Map[Reindeer, ArrayBuffer[Reindeer]]()
    val pq = PriorityQueue[(Reindeer, Int)]((reindeer, 0))(
      Ordering.by((_: (Reindeer, Int))._2).reverse
    )
    while (!pq.isEmpty) {
      val (cur, curDistance) = pq.dequeue
      cur.costs.filter((r, _) => isValid(r.point)).foreach((r, cost) => {
        val distance = curDistance + cost
        if distance < distances.getOrElse(r, Int.MaxValue) then
          distances.addOne((r, distance))
          predecessors.addOne((r, ArrayBuffer(cur)))
          pq.enqueue((r, distance))
        else if (distance == distances.getOrElse(r, Int.MaxValue)) then
          val prev = predecessors.getOrElse(r, ArrayBuffer())
          prev.addOne(cur)
          predecessors.addOne((r, prev))
        end if
      })
    }
    def findPaths(start: Reindeer): ArrayBuffer[ArrayBuffer[Reindeer]] =
      if start == reindeer then
        return ArrayBuffer(ArrayBuffer(reindeer))
      val paths = ArrayBuffer[ArrayBuffer[Reindeer]]()
      predecessors(start).foreach(p => {
        findPaths(p).foreach(path => {
          paths.addOne(path.appended(start))
        })
      })
      paths
    findPaths(
      distances.filter((r, d) => r.point == end).minBy((_, d) => d)._1
    )
      .flatten
      .distinctBy(_.point)
      .length

  override def toString: String =
    points.map(_.mkString).mkString("\n")

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
      case _ => false

class Reindeer(val point: Point, val direction: Direction):
  def cost(to: Direction): Int =
    if to == direction then 1 else 1001

  def costs: List[(Reindeer, Int)] =
    val dirs = direction match
      case Direction.Right => List(Direction.Up, Direction.Down, Direction.Right)
      case Direction.Left => List(Direction.Up, Direction.Down, Direction.Left)
      case Direction.Up => List(Direction.Up, Direction.Left, Direction.Right)
      case Direction.Down => List(Direction.Left, Direction.Down, Direction.Right)
    dirs.map(dir => (Reindeer(point + dir, dir), cost(dir)))

  override def toString: String = s"$point: $direction"

  override def hashCode(): Int = (point, direction).hashCode

  override def equals(any: Any): Boolean =
    any match
      case value: Reindeer => point == value.point && direction == value.direction
      case _ => false

enum Direction(val point: Point):
  case Up extends Direction(Point(0, -1))
  case Down extends Direction(Point(0, 1))
  case Left extends Direction(Point(-1, 0))
  case Right extends Direction(Point(1, 0))

def part1(path: String): Int =
  val contents = readFile(path)
  val maze = Maze(
    ArrayBuffer.from(contents(0).split("\n").map(c =>
      ArrayBuffer.from(c.toCharArray)
    ))
  )
  maze.paths

def part2(path: String): Int =
  val contents = readFile(path)
  val maze = Maze(
    ArrayBuffer.from(contents(0).split("\n").map(c =>
      ArrayBuffer.from(c.toCharArray)
    ))
  )
  maze.bestSeats
