package day05

import scala.io.Source

@main def part1: Unit =
  println(part1("input.txt"))

@main def part2: Unit =
  println(part2("input.txt"))

def readFile(path: String): Array[String] =
  Source.fromFile(path).mkString.split("\n\n")

def part1(path: String): Int =
  def parseOrder(nums: String): Map[(Int, Int), Int] =
    nums.split("\n").flatMap(xs => {
      val ns = xs.split("\\|")
      val left = ns(0).toInt
      val right = ns(1).toInt
      List(((left, right), -1), ((right, left), 1))
    }).to(Map)
  def parseUpdates(rows: String): List[List[Int]] =
    rows.split("\n").map(_.split(",").map(_.toInt).toList).toList
  val contents = readFile(path)
  val order = parseOrder(contents(0))
  def compare(left: Int, right: Int): Int =
    order.getOrElse((left, right), 0)
  val updates = parseUpdates(contents(1)).filter(ns => ns.sorted(compare) == ns)
  val middle = updates.map(ns => ns(ns.length / 2))
  middle.sum

def part2(path: String): Int =
  def parseOrder(nums: String): Map[(Int, Int), Int] =
    nums.split("\n").flatMap(xs => {
      val ns = xs.split("\\|")
      val left = ns(0).toInt
      val right = ns(1).toInt
      List(((left, right), -1), ((right, left), 1))
    }).to(Map)
  def parseUpdates(rows: String): List[List[Int]] =
    rows.split("\n").map(_.split(",").map(_.toInt).toList).toList
  val contents = readFile(path)
  val order = parseOrder(contents(0))
  def compare(left: Int, right: Int): Int =
    order.getOrElse((left, right), 0)
  val updates = parseUpdates(contents(1)).filter(ns => ns.sorted(compare) != ns).map(ns => ns.sorted(compare))
  val middle = updates.map(ns => ns(ns.length / 2))
  middle.sum
