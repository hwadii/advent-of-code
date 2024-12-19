package day19

import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashSet
import scala.collection.mutable.Stack
import scala.math.min
import scala.util.chaining._

@main def part1: Unit =
  println(part1("input.txt"))

@main def part2: Unit =
  println(part2("input.txt"))

def readFile(path: String): List[String] =
  Source.fromFile(path).mkString.split("\n\n").toList

class Pattern(val stripes: String):
  override def hashCode(): Int = stripes.hashCode()

  override def equals(any: Any): Boolean = any match
    case value: Pattern => value.stripes == stripes
    case _ => false

  override def toString: String = stripes

class Towel(val stripes: String):
  def arrangements(patterns: HashSet[Pattern]): Long =
    val max = patterns.maxBy(_.stripes.length()).stripes.length()
    var q = Stack.from(candidates(stripes, patterns, max).map((_, 1L)))
    var arrangements = 0L
    while (!q.isEmpty) {
      val cur = q.pop
      val remaining = stripes.drop(cur._1.length())
      q.pushAll(candidates(remaining, patterns, max).map(cur._1.concat(_)).map((_, cur._2)))
      q = Stack.from(
        q
          .removeAll
          .groupBy((s, _) => s)
          .map((s, group) => (s, group.map((_, c) => c).sum))
      )
      if (cur._1 == stripes) arrangements += cur._2
    }
    arrangements

  def candidates(value: String, patterns: HashSet[Pattern], max: Int): List[String] =
    (0 until max)
      .map(i => value.substring(0, min(i + 1, value.length)))
      .filter(s => patterns.contains(Pattern(s)))
      .distinct
      .toList

  override def toString: String = stripes

def part1(path: String): Int =
  val contents = readFile(path)
  val patterns = HashSet.from(contents(0).split(", ").map(Pattern(_)))
  val towels = contents(1).split("\n").map(Towel(_)).toList
  towels.filter(_.arrangements(patterns) != 0).length

def part2(path: String): Long =
  val contents = readFile(path)
  val patterns = HashSet.from(contents(0).split(", ").map(Pattern(_)))
  val towels = contents(1).split("\n").map(Towel(_)).toList
  towels.map(_.arrangements(patterns)).sum
