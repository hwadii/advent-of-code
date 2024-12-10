package day09

import scala.io.Source
import scala.collection.mutable.ArrayBuffer

@main def part1: Unit =
  println(part1("input.txt"))

@main def part2: Unit =
  println(part2("input.txt"))

def readFile(path: String): String =
  Source.fromFile(path).mkString

enum Sector:
  case File(val id: Long, val size: Int)
  case FreeSpace(val size: Int)

def part1(path: String): Long =
  val contents = readFile(path).trim
  val rawSectors = contents.zipWithIndex.partitionMap((c, i) => if i % 2 == 0 then Left(c.asDigit) else Right(c.asDigit))
  val sectors = rawSectors._1.zip(rawSectors._2 :+ 0)
    .zipWithIndex
    .flatMap((s, i) => List(Sector.File(i, s._1), Sector.FreeSpace(s._2)))
    .toList
  val unrolled = ArrayBuffer.from(sectors.flatMap(s => {
    s match
      case Sector.File(id, size) => List.fill(size)(id)
      case Sector.FreeSpace(size) => List.fill(size)(null)
  }))
  val spots = unrolled.count(_ == null)
  val files = sectors.filter(_.isInstanceOf[Sector.File]).reverse.asInstanceOf[List[Sector.File]]
  var filled = 0
  var index = 0
  var fileIndex = 0
  var fileRemainingSize = files(fileIndex).size
  while (filled < spots && index < unrolled.length) {
    if (unrolled(index) == null) {
      if (fileRemainingSize == 0) {
        fileIndex += 1
        fileRemainingSize = files(fileIndex).size
      }
      unrolled(index) = files(fileIndex).id
      fileRemainingSize -= 1
      index += 1
      filled += 1
    } else {
      index += 1
    }
  }
  unrolled.dropRight(spots).asInstanceOf[ArrayBuffer[Long]].zipWithIndex.foldLeft(0L)((acc, elem) => acc + elem._1 * elem._2)

def part2(path: String): Long =
  val contents = readFile(path).trim
  val rawSectors = contents.zipWithIndex.partitionMap((c, i) => if i % 2 == 0 then Left(c.asDigit) else Right(c.asDigit))
  val sectors = ArrayBuffer.from(rawSectors._1.zip(rawSectors._2 :+ 0)
    .zipWithIndex
    .flatMap((s, i) => List(Sector.File(i, s._1), Sector.FreeSpace(s._2))))
  val files = sectors.filter(_.isInstanceOf[Sector.File]).reverse.asInstanceOf[ArrayBuffer[Sector.File]]
  files.foreach(f => {
    val index = sectors.indexWhere(s =>
      s match
        case Sector.FreeSpace(size) if f.size <= size => true
        case _ => false
    )
    if (index != -1) {
      val previous = sectors(index).asInstanceOf[Sector.FreeSpace]
      var insert = List(Sector.File(f.id, f.size))
      if (previous.size != insert.size) insert :+= Sector.FreeSpace(previous.size - f.size)
      sectors.patchInPlace(index, insert, 1)
      val previousLoc = sectors.lastIndexWhere(s =>
        s match
          case Sector.File(id, _) if id == f.id => true
          case _ => false
        )
      sectors.patchInPlace(previousLoc, List(Sector.FreeSpace(f.size)), 1)
    }
  })
  val unrolled = sectors.flatMap(s => {
    s match
      case Sector.File(id, size) => List.fill(size)(id)
      case Sector.FreeSpace(size) => List.fill(size)(null)
  })
  unrolled.zipWithIndex.foldLeft(0L)((acc, elem) =>
    elem._1 match
      case null => acc
      case _ => acc + elem._1.asInstanceOf[Long] * elem._2
    )
