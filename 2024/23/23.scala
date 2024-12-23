package day23

import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashSet
import scala.collection.mutable.Stack
import scala.collection.mutable.Map
import scala.collection.immutable.{Map => IMap}
import scala.util.chaining.*
import scala.util.boundary, boundary.break
import scala.math.min

@main def part1: Unit =
  println(part1("input.txt"))

@main def part2: Unit =
  println(part2("input.txt"))

def readFile(path: String): List[Connection] =
  Source.fromFile(path).getLines
    .flatMap(_.split("-") pipe (w => Connection(w(0), w(1))) pipe (c => List(c, c.reverse)))
    .toList

class Connection(val src: String, val dst: String):
  def reverse: Connection = Connection(dst, src)

  override def toString: String = s"$src -> $dst"

class Lan(val connections: IMap[String, List[String]]):
  def largest: String =
    def search(sets: HashSet[HashSet[String]], node: String, req: HashSet[String]): HashSet[HashSet[String]] =
      if sets.contains(req) then return sets
      else sets.addOne(req)
      connections.get(node).get.foreach(n => {
        boundary:
          if req.contains(n) then boundary.break()
          if !req.forall(nn => connections.get(nn).get.contains(n)) then boundary.break()
          search(sets, n, req.addOne(n))
      })
      sets
    connections.flatMap((n, _) => search(HashSet[HashSet[String]](), n, HashSet(n)))
      .maxBy(_.size)
      .toList
      .sorted
      .mkString(",")

  def fancy: String =
    def bronKerbosch(r: HashSet[String], p: HashSet[String], x: HashSet[String]): ArrayBuffer[ArrayBuffer[String]] =
      if p.isEmpty && x.isEmpty then
        return ArrayBuffer(ArrayBuffer.from(r))
      val cliques = ArrayBuffer[ArrayBuffer[String]]()
      p.foreach(v => {
        val newR = r.union(HashSet(v))
        val newP = p.intersect(HashSet.from(connections.get(v).get))
        val newX = x.intersect(HashSet.from(connections.get(v).get))
        cliques.addAll(bronKerbosch(newR, newP, newX))
        p.remove(v)
        x.addOne(v)
      })
      cliques
    bronKerbosch(HashSet(), HashSet.from(connections.keys), HashSet())
      .maxBy(_.length)
      .sorted
      .mkString(",")

  def interconnected(subgraph: List[String]): Boolean =
    subgraph.forall(s => {
      val cs = connections.get(s).get
      subgraph.filter(_ != s).forall(cs.contains(_))
    })
  override def toString: String = connections.toString()

def part1(path: String): Int =
  val contents = readFile(path)
  val groups = contents.groupMap(_.src)(_.dst)
  val interconnected = HashSet[HashSet[String]]()
  groups.foreach((src, dsts) => {
    dsts.foreach(dst => {
      groups.get(dst).get.filter(_ != src).foreach(last => {
        if groups.get(last).get.contains(src) then
          interconnected.addOne(HashSet(src, dst, last))
      })
    })
  })
  interconnected.filter(_.exists(_.startsWith("t"))).size

def part2(path: String): String =
  val contents = readFile(path)
  val groups = contents.groupMap(_.src)(_.dst)
  Lan(groups).largest
