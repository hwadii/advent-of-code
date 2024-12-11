package day11

import scala.io.Source
import scala.math.{log10, floor}

@main def part1: Unit =
  println(part1("sample.txt"))

// @main def part2: Unit =
//   println(part2("input.txt"))

def readFile(path: String): String =
  Source.fromFile(path).mkString

class Stone(val n: Long):
  def split: List[Stone] =
    val ns = digits
    if n == 0 then
      List(Stone(1))
    else if (ns % 2 == 0) then
      val (left, right) = n.toString().splitAt((ns / 2).toInt)
      List(Stone(left.toLong), Stone(right.toLong))
    else
      List(Stone(n * 2024))

  def digits: Int =
    floor(log10(n.toDouble) + 1).toInt

  override def toString: String = s"Stone: $n"

def part1(path: String): Int =
  var contents = readFile(path).trim.split(" ").map(c => Stone(c.toLong)).toList
  var prev = contents.length
  (1 to 15).foreach(_ => {
    prev = contents.length
    contents = contents.flatMap(_.split)
    println(s"$contents -> ${contents.length} -> ${contents.length - prev}")
  })
  contents.length

// def part2(path: String): Int =
//   val contents = readFile(path)
//   TopographicMap(contents).sumRatings
