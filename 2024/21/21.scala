package day21

import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map
import scala.collection.mutable.HashSet
import scala.util.chaining.*

@main def part1: Unit =
  println(part1("input.txt"))

// @main def part2: Unit =
//   println(part2("input.txt"))

def readFile(path: String): List[String] =
  Source.fromFile(path).getLines.toList

class Point(val x: Int, val y: Int):
  def +(other: Point): Point = Point(x + other.x, y + other.y)

  def -(other: Point): Point = Point(x - other.x, y - other.y)

  override def toString: String =
    s"($x, $y)"

  override def hashCode(): Int = (x, y).hashCode

  override def equals(any: Any): Boolean =
    any match
      case value: Point => x == value.x && y == value.y
      case _            => false

class NumericKeypad:
  def row(key: Char): Int =
    key match
      case '0' | 'A' => 0
      case _ => if key.asDigit % 3 == 0 then key.asDigit / 3 else key.asDigit / 3 + 1

  def column(key: Char): Int =
    key match
      case '0' => 1
      case 'A' => 2
      case _ => if key.asDigit % 3 == 0 then 2 else key.asDigit % 3 - 1

  def location(key: Char): Point =
    Point(column(key), row(key))

  def path(key: Char, other: Char): List[List[Button]] =
    val src = location(key)
    val dst = location(other)
    val move = dst - src
    val ys =
      if move.y.sign == -1 then
        Button.Down.times(move.y)
      else
        Button.Up.times(move.y)
    val xs =
      if move.x.sign == -1 then
        Button.Left.times(move.x)
      else
        Button.Right.times(move.x)
    xs.concat(ys).permutations.map(_.appended(Button.A)).filter(ms => {
      if src.x == 0 then
        !ms.take(src.y).forall(_ == Button.Down)
      else if src.y == 0 then
        !ms.take(src.x).forall(_ == Button.Left)
      else true
    }).toList

  def possibleDoorCombinations(code: String): List[List[Button]] =
    val combs = code
      .prepended('A')
      .sliding(2)
      .map(s => path(s(0), s(1)))
      .toList
    def combine(cur: List[List[Button]], xs: List[List[List[Button]]]): List[List[Button]] =
      if xs.isEmpty then
        return cur
      val first = xs.head
      // first: [['^', 'v'], ['v', '^']]
      // cur: [['^', '^', '^'], ['^', 'v']]]
      combine(first.flatMap(cs => cur.map(_.concat(cs))), xs.tail)
    combine(combs.head, combs.tail)

class DirectionalKeypad:
  def row(key: Button): Int =
    key match
      case Button.Down | Button.Left | Button.Right => 0
      case Button.Up | Button.A => 1

  def column(key: Button): Int =
    key match
      case Button.Left => 0
      case Button.Up | Button.Down => 1
      case Button.A | Button.Right => 2

  def location(key: Button): Point =
    Point(column(key), row(key))

  def complexity(code: String, depth: Int): Int =
    val n = NumericKeypad()
    val moves = Map[Path, List[Button]]()
    var next = List[Button]()
    n.possibleDoorCombinations(code).take(1).map(first => {
      var last = Map[Path, Int]()
      var i = 0
      while i < depth do
        if last.isEmpty then
          val paths = Map.from(first.prepended(Button.A)
            .sliding(2)
            .map(s => Path(s(0), s(1)))
            .toList
            .groupBy(identity)
            .view
            .mapValues(p => p.length)
            .view)
          last = paths
          moves.addAll(paths.keys.map(p => (p, path(p.key, p.other))))
          // println(paths)
          // println(moves)
        else
          var updated = Map[Path, Int]()
          val reduced = last
            .keys
            .zipWithIndex.foreach((p, i) => {
              val exploded = moves.get(p).get
              if i == 0 then
                updated.addAll(
                  exploded
                    .prepended(Button.A)
                    .sliding(2)
                    .map(s => Path(s(0), s(1)))
                    .toList
                    .groupBy(identity)
                    .view
                    .mapValues(s => last.get(p).get * s.length)
                )
              else
                updated.addAll(
                  exploded
                    .prepended(moves.get(last.keys.toList(i - 1)).get.last)
                    .sliding(2)
                    .map(s => Path(s(0), s(1)))
                    .toList
                    .groupBy(identity)
                    .view
                    .mapValues(s => last.get(p).get + s.length)
                )
            })
          println(updated)
          moves.addAll(updated.keys.map(p => (p, path(p.key, p.other))))
          last = updated
        i += 1
      println(last)
      last.map(_._2).sum
      // println(first.mkString)
      // println(first)
      // println(second.mkString)
      // println(third.mkString)
      // println(s"${code.filter(_.isDigit).toInt} * ${last.length}")
      // code.filter(_.isDigit).toInt * last.length
    }).min

  def path(key: Button, other: Button): List[Button] =
    val src = location(key)
    val dst = location(other)
    val move = dst - src
    val ys =
      if move.y.sign == -1 then
        Button.Down.times(move.y)
      else
        Button.Up.times(move.y)
    val xs =
      if move.x.sign == -1 then
        Button.Left.times(move.x)
      else
        Button.Right.times(move.x)
    if src.x == 0 && dst.y == 1 then
      xs.concat(ys).appended(Button.A).toList
    else
      ys.concat(xs).appended(Button.A).toList

class Path(val key: Button, val other: Button):
  override def toString: String = s"($key,$other)"

  override def hashCode: Int = (key, other).hashCode

  override def equals(any: Any): Boolean = any match
    case value: Path => key == value.key && other == value.other
    case _ => false

enum Button:
  case Up, Down, Left, Right, A

  override def toString: String = this match
    case Button.Up => "^"
    case Button.Down => "v"
    case Button.Left => "<"
    case Button.Right => ">"
    case Button.A => "A"

  def times(n: Int): List[Button] = (0 until scala.math.abs(n)).map(_ => this).toList

object Button:
  def fromString(value: String): List[Button] =
    List.from(
      value.map(_ match
        case '^' => Button.Up
        case 'v' => Button.Down
        case '<' => Button.Left
        case '>' => Button.Right
        case 'A' => Button.A
      )
    )

def part1(path: String): Int =
  val contents = readFile(path)
  val k = DirectionalKeypad()
  val n = NumericKeypad()
  // println(contents.map(k.complexity(_, 2)).sum)
  println(k.complexity(contents(0), 3))
  // println(k.complexity(contents(4)))
  0

// def part2(path: String): Int =
//   val contents = readFile(path)
//   Racetrack(contents).shortcuts(20, 100).length
