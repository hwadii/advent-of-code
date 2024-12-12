package day11

import scala.io.Source
import scala.math.{log10, floor}

@main def part1: Unit =
  println(part("input.txt", 25))

@main def part2: Unit =
  println(part("input.txt", 75))

def readFile(path: String): String =
  Source.fromFile(path).mkString

class Stone(val n: Long):
  def split: List[Stone] =
    val ns = digits
    if n == 0 then
      List(Stone(1))
    else if (ns % 2 == 0) then
      val (left, right) = n.toString().splitAt(ns / 2)
      List(Stone(left.toLong), Stone(right.toLong))
    else
      List(Stone(n * 2024))

  def digits: Int =
    n.toString().length()

  override def toString: String = s"$n"

  override def equals(any: Any): Boolean = any match
    case value: Stone => n == value.n
    case _ => false

  override def hashCode(): Int = n.hashCode

def part(path: String, n: Int): Long =
  var contents = readFile(path).trim.split(" ").map(c => Stone(c.toLong)).toList
  var map = Map.from(contents.groupBy(identity).view.mapValues(_.length.toLong))
  (0 until n).foreach(i => {
    map = List.from(map)
      .flatMap((s, v) => s.split.map((_, v)))
      .groupBy(_._1)
      .view
      .mapValues(_.map(_._2).sum)
      .toMap
  })
  map.values.sum
