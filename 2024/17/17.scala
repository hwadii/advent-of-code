package day17

import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import scala.math.pow

@main def part1: Unit =
  println(part1("input.txt"))

@main def part2: Unit =
  println(part2("input.txt"))

def readFile(path: String): List[String] =
  Source.fromFile(path).mkString.split("\n\n").toList

enum Instruction:
  case Adv
  case Bxl
  case Bst
  case Jnz
  case Bxc
  case Out
  case Bdv
  case Cdv

enum Operand:
  case Literal(value: Long)
  case A
  case B
  case C

type Code = Instruction | Operand

class Program(val registers: ArrayBuffer[Register], val ops: List[Long]):
  def execute: String =
    var insns = 0
    var result = ArrayBuffer[Long]()
    while (insns < ops.length) {
      assert(insns % 2 == 0)
      val insn = Instruction.fromOrdinal(ops(insns).toInt)
      if (insn == Instruction.Adv) {
        registers(0).value = (registers(0).value / pow(2, resolveOp(ops(insns + 1)).toDouble)).toLong
        insns += 2
      } else if (insn == Instruction.Bxl) {
        registers(1).value = registers(1).value ^ ops(insns + 1)
        insns += 2
      } else if (insn == Instruction.Bst) {
        registers(1).value = resolveOp(ops(insns + 1)) % 8
        insns += 2
      } else if (insn == Instruction.Jnz) {
        if registers(0).value != 0 then insns = ops(insns + 1).toInt
        else insns += 2
      } else if (insn == Instruction.Bxc) {
        registers(1).value = registers(1).value ^ registers(2).value
        insns += 2
      } else if (insn == Instruction.Out) {
        result.addOne(resolveOp(ops(insns + 1)) % 8)
        insns += 2
      } else if (insn == Instruction.Bdv) {
        registers(1).value = (registers(0).value / pow(2, resolveOp(ops(insns + 1)).toDouble)).toLong
        insns += 2
      } else {
        registers(2).value = (registers(0).value / pow(2, resolveOp(ops(insns + 1)).toDouble)).toLong
        insns += 2
      }
    }
    result.mkString(",")

  def resolveOp(op: Long): Long =
    op match
      case value if 0 to 3 contains value => value
      case 4 => registers(0).value
      case 5 => registers(1).value
      case 6 => registers(2).value

  override def toString: String = s"$registers\n$ops"

object Program:
  def parse(registers: String, insns: String): Program =
    val code = insns
      .replace("Program: ", "")
      .trim
      .split(",")
      .toList
      .map(_.toLong)
    val regs = ArrayBuffer.from(registers.split("\n").map(Register.parse(_)))
    Program(regs, code)

class Register(var value: Long):
  override def toString: String = value.toString

object Register:
  val Pattern = """Register \w+: (\d+)""".r

  def parse(register: String): Register =
    val ms = Pattern.findAllMatchIn(register).flatMap(m =>
      List(m.group(1))
    ).toList
    Register(ms(0).toLong)

def part1(path: String): String =
  val contents = readFile(path)
  Program.parse(contents(0), contents(1)).execute

def part2(path: String): Int =
  val contents = readFile(path)
  val program = Program.parse(contents(0), contents(1))
  var start = program.registers(0).value
  var out = program.execute
  val expected = program.ops.mkString(",")
  println(start)
  while (out != expected) {
    start = start + 1
    out = Program(ArrayBuffer(Register(start), Register(0), Register(0)), program.ops).execute
  }
  println(start)
  0
