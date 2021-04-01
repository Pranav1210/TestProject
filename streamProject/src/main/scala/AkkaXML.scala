import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.{ ActorMaterializer, OverflowStrategy }
import akka.stream.scaladsl.{ Sink, Source }

import java.util.concurrent.atomic.AtomicLong
import scala.concurrent.duration.DurationInt
import scala.concurrent.Promise
import scala.util.{ Failure, Success }

object AkkaXML {
  implicit val system           = ActorSystem("YoYo")
  implicit val materializer     = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  // case class DataPack(name: String, age: Int)

  def main(args: Array[String]): Unit = {
    val atomicLong = new AtomicLong()
    val connectionPool = Http()
      .cachedHostConnectionPoolHttps[Promise[HttpResponse]]("vmm.loadus1.ciscoccservice.com")
    val queue = Source
      .actorRef[(HttpRequest, Promise[HttpResponse])](Integer.MAX_VALUE, OverflowStrategy.dropNew)
      .via(connectionPool)
      .map {
        case (value, itr) =>
          atomicLong.addAndGet(1)
          itr.complete(value)
        // value.entity.dataBytes.runReduce(_ ++ _).map(s => println(itr + " " + s.utf8String))
      }
      .to(Sink.ignore)
      .run()

    (1 to 1024).toIterator.foreach { i =>
      val request1 = HttpRequest(uri = Uri("https://vmm.loadus1.ciscoccservice.com/vmm/v1/ping"))
      val request2 = HttpRequest(uri = Uri("https://vmm.loadus1.ciscoccservice.com/build_info"))

      val promise1 = Promise[HttpResponse]
      val promise2 = Promise[HttpResponse]

      queue ! (request1, promise1)
      queue ! (request2, promise2)
      promise1.future.flatMap(resp => resp.entity.dataBytes.runReduce(_ ++ _)).map(_.utf8String).foreach(println)
      promise2.future.flatMap(resp => resp.entity.dataBytes.runReduce(_ ++ _)).map(_.utf8String).foreach(println)
    }

    system.scheduler.schedule(1 seconds, 1 seconds) {
      println(atomicLong.get())
    }

    Thread.sleep(10000)
  }
}
