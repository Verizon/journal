package journal

// A simplistic benchmark for Journal latency
// against a naked SLF4J appender.
//
// Both are logging to the console.
// Since Journal uses an Actor which is backed by a dedicated thread,
// its latency is a couple of orders of magnitude less.
//
// If a log level is turned off at compile-time, the runtime overhead
// of that log level in Journal will be zero.

object Benchmark {
  val j: journal.Logger = journal.Logger("Journal")

  val l: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger("SLF4J")

  val length = 1000000

  case class Stats(min: Option[Long], max: Option[Long], sum: Long, count: Int)

  def time(log: String => Unit): Stats = {
    var n = 3
    // Best out of three
    List.fill(3) {
      var s = Stats(None, None, 0, 0)
      var m = length
      while (m > 0) {
        m = m - 1
        val t = System.nanoTime
        log("bench")
        val tt = System.nanoTime - t
        s = Stats(s.min.map(_ min tt) orElse Some(tt),
                  s.max.map(_ max tt) orElse Some(tt),
                  s.sum + tt,
                  s.count + 1)
      }
      s
    }.minBy(_.sum)
  }

  def reportStats(s: Stats, name: String) = {
    println(s"$name:")
    println(s"  min: ${s.min.get} ns")
    println(s"  max: ${s.max.get} ns")
    println(s"  avg: ${s.sum.toDouble / s.count} ns")
  }

  def main(args: Array[String]): Unit = {
    val x = time(j.info(_))
    // Allow the actor to catch up
    Thread.sleep(30000)
    val y = time(l.info(_))
    reportStats(x, "Journal")
    reportStats(y, "SLF4J")
  }
}

