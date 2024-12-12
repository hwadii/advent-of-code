package day11

import scala.io.Source
import scala.math.{log10, floor}
import scala.collection.mutable.Map
import scala.collection.mutable.ArrayBuffer

@main def part1: Unit =
  println(part1("sample.txt"))

@main def part2: Unit =
  println(part2("input.txt"))

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

  override def toString: String = s"$n"

  override def equals(any: Any): Boolean = any match
    case value: Stone => n == value.n
    case _ => false

  override def hashCode(): Int = n.hashCode

class Cache(val map: Map[Stone, List[Stone]]):
  def take(stone: Stone, n: Int): Int =
    var i = 0
    var hops = 0
    var cur = stone
    while (i < n) {
      
    }
    def dig(cur: Stone, hops: Int, i: Int): Int =
      if (i == 0)
        return hops
      val next = map.get(cur).get
      if (next.length == 2) {
        return dig(next(0), hops + 2, i - 1) + dig(next(1), hops + 2, i - 1)
      } else {
        return dig(next(0), hops + 1, i - 1)
      }
    dig(stone, 0, n)

def buildMap(path: String): (Int, Map[Stone, List[Stone]]) =
  var contents = readFile(path).trim.split(" ").map(c => Stone(c.toLong)).toList
  val map = Map[Stone, List[Stone]]()
  (0 until 40).foreach(_ => {
    contents = contents.flatMap(s => {
      if (map.contains(s)) {
        map.get(s).get
      } else {
        val sides = s.split
        map.addOne((s, sides))
        sides
      }
    })
  })
  (contents.length, map)

def part1(path: String): Int =
  val stones = buildMap(path)
  stones._1

def part2(path: String): Int =
  val (_, map) = buildMap(path)
  var contents = readFile(path).trim.split(" ").map(c => Stone(c.toLong)).toList
  println(map.keys.size)
  // val cache = Cache(map)
  // println(cache.take(Stone(125), 24) + cache.take(Stone(17), 24))
  // (0 until 75).foreach(i => {
  //   println(i)
  //   contents = contents.flatMap(s => map.get(s).get)
  // })
  // contents.length
  0
