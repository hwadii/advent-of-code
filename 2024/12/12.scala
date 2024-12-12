package day12

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashSet
import scala.collection.mutable.Stack
import scala.io.Source

@main def part1: Unit =
  println(part1("input.txt"))

@main def part2: Unit =
  println(part2("input.txt"))

def readFile(path: String): List[List[Char]] =
  Source.fromFile(path).getLines.map(_.toCharArray.toList).toList

enum Direction(val point: Point):
  case W extends Direction(Point(-1, 0))
  case S extends Direction(Point(0, 1))
  case E extends Direction(Point(1, 0))
  case N extends Direction(Point(0, -1))

class Point(val x: Int, val y: Int):
  def +(other: Point): Point = Point(x + other.x, y + other.y)

  def +(other: Direction): Point = this + other.point

  def -(other: Point): Point = Point(x - other.x, y - other.y)

  def -(other: Direction): Point = this + other.point

  override def equals(any: Any): Boolean =
    any match
      case value: Point => x == value.x && y == value.y
      case _ => false

  override def toString: String =
    s"Point: ($x, $y)"

  override def hashCode: Int = (x, y).hashCode
class Land(val plots: List[List[Char]]):
  def get(point: Point): Option[Char] =
    if contains(point) then Some(plots(point.y)(point.x)) else None

  def contains(point: Point): Boolean =
    0 <= point.x && point.x < plots(0).length && 0 <= point.y && point.y < plots.length

  def sameRegion(point: Point, other: Point): Boolean =
    get(other) match
      case Some(value: Char) => get(point).get == value
      case None => false

  def corners(point: Point): Int =
    var count = 0
    // convex
    if (sameRegion(point, point + Direction.E) && sameRegion(point, point + Direction.S) && !sameRegion(point, point + Direction.S + Direction.E)) count += 1
    if (sameRegion(point, point + Direction.W) && sameRegion(point, point + Direction.S) && !sameRegion(point, point + Direction.S + Direction.W)) count += 1
    if (sameRegion(point, point + Direction.E) && sameRegion(point, point + Direction.N) && !sameRegion(point, point + Direction.N + Direction.E)) count += 1
    if (sameRegion(point, point + Direction.W) && sameRegion(point, point + Direction.N) && !sameRegion(point, point + Direction.W + Direction.N)) count += 1
    // concave
    if (!sameRegion(point, point + Direction.N) && !sameRegion(point, point + Direction.E)) count += 1
    if (!sameRegion(point, point + Direction.N) && !sameRegion(point, point + Direction.W)) count += 1
    if (!sameRegion(point, point + Direction.S) && !sameRegion(point, point + Direction.E)) count += 1
    if (!sameRegion(point, point + Direction.S) && !sameRegion(point, point + Direction.W)) count += 1
    count

  def regions: ArrayBuffer[Region] =
    val nextStart = Stack[Point](Point(0, 0))
    val visited = HashSet[Point]()
    val regions = ArrayBuffer[Region]()
    while (!nextStart.isEmpty) {
      val start = nextStart.pop
      val positions = ArrayBuffer[Point]()
      if (!visited.contains(start)) {
        val next = Stack[Point](start)
        var area = 0
        var perimeter = 0
        while (!next.isEmpty) {
          val current = next.pop
          if (!visited.contains(current)) {
            area += 1
            positions.addOne(current)
          }
          val west = current + Direction.W
          if (!visited.contains(west) && sameRegion(current, west)) next.push(west)
          else {
            if contains(west) then nextStart.push(west)
            if (!sameRegion(current, west) && !visited.contains(current)) perimeter += 1
          }
          val east = current + Direction.E
          if (!visited.contains(east) && sameRegion(current, east)) next.push(east)
          else {
            if contains(east) then nextStart.push(east)
            if (!sameRegion(current, east) && !visited.contains(current)) perimeter += 1
          }
          val north = current + Direction.N
          if (!visited.contains(north) && sameRegion(current, north)) next.push(north)
          else {
            if contains(north) then nextStart.push(north)
            if (!sameRegion(current, north) && !visited.contains(current)) perimeter += 1
          }
          val south = current + Direction.S
          if (!visited.contains(south) && sameRegion(current, south)) next.push(south)
          else {
            if contains(south) then nextStart.push(south)
            if (!sameRegion(current, south) && !visited.contains(current)) perimeter += 1
          }
          visited.addOne(current)
        }
        regions.addOne(Region(get(start).get, area, perimeter, positions))
      }
    }
    regions

  def price: Long =
    regions.map(r => r.area * r.perimeter).sum

  def priceWithSides: Long =
    regions.map(region => region.area * region.positions.map(p => corners(p)).sum).sum

  override def toString: String = plots.map(_.mkString).mkString("\n")

class Region(val kind: Char, val area: Long, val perimeter: Long, val positions: ArrayBuffer[Point]):
  override def toString: String = s"$kind ($area, $perimeter) -> $positions"

def part1(path: String): Long =
  Land(readFile(path)).price

def part2(path: String): Long =
  Land(readFile(path)).priceWithSides
