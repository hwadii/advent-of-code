package day07

import scala.io.Source
import scala.math._

@main def part1: Unit =
  println(part1("input.txt"))

@main def part2: Unit =
  println(part2("input.txt"))

def readFile(path: String): List[String] =
  Source.fromFile(path).getLines.toList

enum Op:
  case Add, Mul, Concat

val SimpleOps = List(Op.Add, Op.Mul)
val FullOps = List(Op.Add, Op.Mul, Op.Concat)

def arrangements(ops: List[Op], size: Int, xs: List[Op] = List[Op]()): List[List[Op]] =
  if (xs.length == size)
    List(xs)
  else
    ops.flatMap(op => arrangements(ops, size, xs.appended(op))).toList

class Equation(val operands: List[Long], val result: Long):
  def isValid(kinds: List[Op]): Boolean =
    arrangements(kinds, operands.length)
      .map(ops => ops.zipWithIndex.foldLeft(operands(0))((left, op) => {
        val right = operands.lift(op._2 + 1)
        op._1 match
          case Op.Add => left + right.getOrElse(0L)
          case Op.Mul => left * right.getOrElse(1L)
          case Op.Concat => left * pow(10, floor(log10(right.getOrElse(-1L).toDouble)) + 1).longValue + right.getOrElse(0L)
      }))
      .exists(_ == result)

  override def toString: String =
    s"$result = $operands"

def part1(path: String): Long =
  val contents = readFile(path)
  val equations = contents
    .map(eq => {
      val sides = eq.split(": ")
      List(sides(0).toLong).concat(sides(1).split(" ").map(_.toLong))
    })
    .map(eq => Equation(eq.drop(1), eq(0)))
  equations.filter(_.isValid(SimpleOps)).map(_.result).sum

def part2(path: String): Long =
  val contents = readFile(path)
  val equations = contents
    .map(eq => {
      val sides = eq.split(": ")
      List(sides(0).toLong).concat(sides(1).split(" ").map(_.toLong))
    })
    .map(eq => Equation(eq.drop(1), eq(0)))
  equations.filter(_.isValid(FullOps)).map(_.result).sum
