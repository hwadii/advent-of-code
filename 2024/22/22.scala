package day22

import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashSet
import scala.util.chaining.*
import scala.math.min

@main def part1: Unit =
  println(part1("input.txt"))

@main def part2: Unit =
  println(part2("input.txt"))

def readFile(path: String): List[Long] =
  Source.fromFile(path).getLines.map(_.toLong).toList

class Secret:
  def next(n: Long): Long =
    var result = n
    result = (result ^ (result * 64)) % 16777216
    result = (result ^ (result / 32)) % 16777216
    result = (result ^ (result * 2048)) % 16777216
    result

  def generate(start: Long, n: Int): List[Long] =
    val buf = ArrayBuffer[Long](start)
    var cur = start
    (0 until n).foreach(_ => {
      cur = next(cur)
      buf.addOne(cur)
    })
    buf.toList

  def prices(start: Long, n: Int): List[Long] =
    generate(start, n).map(_ % 10)

  def priceChanges(start: Long, n: Int): List[Long] =
    generate(start, n).map(_ % 10).sliding(2).map(s => s(1) - s(0)).toList

def part1(path: String): Long =
  val contents = readFile(path)
  contents.map(Secret().generate(_, 2000).last).sum

def part2(path: String): Long =
  val contents = readFile(path)
  val n = 2000
  val maps = contents.map(c => {
    val prices = Secret().prices(c, n)
    Secret().priceChanges(c, n)
      .sliding(4)
      .zipWithIndex
      .map((w, i) => (w, prices(4 + i)))
      .distinctBy(_._1)
      .toMap
  })
  maps.flatMap(_.keys).distinct.map(u => maps.map(_.getOrElse(u, 0L)).sum).max
