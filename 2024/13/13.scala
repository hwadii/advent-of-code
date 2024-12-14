package day13

import scala.io.Source

@main def part1: Unit =
  println(part1("sample.txt"))

// @main def part2: Unit =
//   println(part2("input.txt"))

def readFile(path: String): List[String] =
  Source.fromFile(path).mkString.split("\n\n").toList

class Point(val x: Int, val y: Int):
  def +(other: Point): Point = Point(x + other.x, y + other.y)

  override def toString: String =
    s"($x, $y)"

class Game(val a: Button, val b: Button, val prize: Prize):
  def solve: (Int, Int) =
    val x = (prize.target.x - b.offset.x) / b.offset.y
    val y = 
    (0, 0)

  override def toString: String = s"$a/$b => $target"

class Button(val letter: Char, val offset: Point):
  override def toString: String =
    s"$letter -> $offset"

object Button:
  val Pattern = """\w\+(\d+), \w\+(\d+)""".r

  def parse(kind: Char, value: String): Button =
    val ms = Pattern.findAllMatchIn(value)
      .flatMap(m => List(m.group(1), m.group(2)))
      .toList
    Button(kind, Point(ms(0).toInt, ms(1).toInt))

class Prize(val target: Point):
  override def toString: String = target.toString

object Prize:
  val Pattern = """\w=(\d+), \w=(\d+)""".r

  def parse(value: String): Prize =
    val ms = Pattern.findAllMatchIn(value)
      .flatMap(m => List(m.group(1), m.group(2)))
      .toList
    Prize(Point(ms(0).toInt, ms(1).toInt))

def part1(path: String): Long =
  val contents = readFile(path)
  val games = contents.map(c => {
    val lines = c.split("\n")
    Game(Button.parse('A', lines(0)), Button.parse('B', lines(1)), Prize.parse(lines(2)))
  })
  println(games)
  0
