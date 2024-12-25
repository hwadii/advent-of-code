package day24

import scala.io.Source
import scala.collection.mutable.Map
import scala.collection.mutable.Queue
import scala.util.chaining.*
import java.math.BigInteger

enum Op:
  case And, Or, Xor

class Operation(val left: String, val right: String, val op: Op, val out: String):
  override def toString: String =
    s"$left $op $right -> $out"

object Operation:
  val Pattern = """(\w+) (AND|OR|XOR) (\w+) -> (\w+)""".r

  def parse(expr: String): Operation =
    val ms = Pattern.findAllMatchIn(expr)
      .flatMap(m => List(m.group(1), m.group(2), m.group(3), m.group(4)))
      .toList
    val op = ms(1) match
      case "AND" => Op.And
      case "OR" => Op.Or
      case "XOR" => Op.Xor
    Operation(ms(0), ms(2), op, ms(3))

class Wires(val known: Map[String, Int], val wires: List[String]):
  def evaluate(operation: Operation): Int =
    val left = known.get(operation.left).get
    val right = known.get(operation.right).get
    operation.op match
      case Op.And => left & right
      case Op.Or => left | right
      case Op.Xor => left ^ right

  def compute: Int =
    val q = Queue.from(wires)
    def isPosisble(operation: Operation): Boolean =
      known.contains(operation.left) && known.contains(operation.right)
    while !q.isEmpty do
      val cur = q.dequeue
      val operation = Operation.parse(cur)
      if isPosisble(operation) then
        val result = evaluate(operation)
        known.addOne((operation.out, result))
      else
        q.enqueue(cur)
    println(
      BigInteger( known.filter(_._1.startsWith("z")).toList.sortBy(_._1).reverse.map(_._2).mkString, 2)
    )
    0

  override def toString: String = wires.toString


@main def part1: Unit =
  println(part1("input.txt"))

// @main def part2: Unit =
//   println(part2("input.txt"))

def readFile(path: String): List[String] =
  Source.fromFile(path).mkString.split("\n\n").toList

def part1(path: String): Int =
  val contents = readFile(path)
  val known = Map.from(
    contents(0).split("\n").map(_.split(": ") pipe (c => (c(0), c(1).toInt)))
  )
  val wires = contents(1).split("\n").toList
  println(Wires(known, wires).compute)
  0

// def part2(path: String): String =
//   val contents = readFile(path)
//   val groups = contents.groupMap(_.src)(_.dst)
//   Lan(groups).largest
