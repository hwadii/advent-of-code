package day25

import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Queue
import scala.util.chaining.*

@main def part1: Unit =
  println(part1("input.txt"))

// @main def part2: Unit =
//   println(part2("input.txt"))

def readFile(path: String): List[String] =
  Source.fromFile(path).mkString.split("\n\n").toList

enum Kind:
  case Lock, Key

class FivePin(val grid: List[List[Char]]):
  def kind: Kind =
    if grid(0).forall(_ == '#') then Kind.Lock else Kind.Key

  def parse: List[Int] =
    val buf = ArrayBuffer.fill(5)(-1)
    grid.foreach(row => {
      row.zipWithIndex.foreach((column, i) => {
        if column == '#' then buf(i) += 1
      })
    })
    buf.toList

  override def toString: String = grid.toString

def part1(path: String): Int =
  val contents = readFile(path)
  val pins = contents.map(_.split("\n").map(_.toCharArray.toList).toList).map(FivePin(_))
  val (locks, keys) = pins.partitionMap(p =>
    if p.kind == Kind.Lock then Left(p.parse) else Right(p.parse)
  )
  locks.map(l =>
    keys
      .map(k =>
        l.zipWithIndex.forall((_, i) => l(i) + k(i) <= 5)
      )
      .count(_ == true)
  ).sum

// def part2(path: String): String =
//   val contents = readFile(path)
//   val groups = contents.groupMap(_.src)(_.dst)
//   Lan(groups).largest
