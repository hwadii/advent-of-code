package day08

import scala.io.Source

@main def part1: Unit =
  println(part1("input.txt"))

@main def part2: Unit =
  println(part2("input.txt"))

class Point(val x: Int, val y: Int):
  def within(bounds: Point): Boolean = 0 <= x && x < bounds.x && 0 <= y && y < bounds.y

  def +(other: Point): Point = Point(x + other.x, y + other.y)

  def -(other: Point): Point = Point(x - other.x, y - other.y)

  override def toString: String =
    s"Point: ($x, $y)"

class Antenna(val x: Int, val y: Int):
  def +(other: Antenna): Antenna = Antenna(x + other.x, y + other.y)

  def -(other: Antenna): Antenna = Antenna(x - other.x, y - other.y)

  def toPoint: Point = Point(x, y)

  override def toString: String =
    s"($x, $y)"

class Frequency(val name: Char, val locations: List[Antenna]):
  def antinodes: List[Point] =
    locations.zipWithIndex.flatMap((ref, i) => {
      locations.drop(i + 1).flatMap(other => {
        val distance = ref - other
        List(ref + distance, other - distance).map(_.toPoint)
      })
    })

  def antinodesHarmonics(bounds: Point): List[Point] =
    locations.zipWithIndex.flatMap((ref, i) => {
      locations.drop(i + 1).flatMap(other => {
        val distance = ref - other
        var nodes = List(ref, other).map(_.toPoint)
        var current = (ref + distance).toPoint
        while (current.within(bounds)) {
          nodes :+= current
          current = current + distance.toPoint
        }
        current = (other - distance).toPoint
        while (current.within(bounds)) {
          nodes :+= current
          current = current - distance.toPoint
        }
        nodes
      })
    })

  override def toString: String =
    s"Frequency: ($name) -> $locations"

def readFile(path: String): List[String] =
  Source.fromFile(path).getLines.toList

def part1(path: String): Int =
  val contents = readFile(path)
  val bounds = Point(contents(0).length, contents.length)
  val dots = contents.mkString.replaceAll("\n", "")
  val freqs = dots
    .zipWithIndex
    .groupBy((c, _) => c)
    .filter((c, _) => c != '.')
    .toList
    .map((c, locs) => Frequency(c, locs.map(l => Antenna(l._2 % bounds.x, l._2 / bounds.y)).toList))
  freqs.flatMap(_.antinodes).filter(_.within(bounds)).distinctBy(p => (p.x, p.y)).length

def part2(path: String): Int =
  val contents = readFile(path)
  val bounds = Point(contents(0).length, contents.length)
  val dots = contents.mkString.replaceAll("\n", "")
  val freqs = dots
    .zipWithIndex
    .groupBy((c, _) => c)
    .filter((c, _) => c != '.')
    .toList
    .map((c, locs) => Frequency(c, locs.map(l => Antenna(l._2 % bounds.x, l._2 / bounds.y)).toList))
  freqs.flatMap(_.antinodesHarmonics(bounds)).distinctBy(p => (p.x, p.y)).length

