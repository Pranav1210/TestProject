import akka.actor.{ Actor, ActorRefFactory, ActorSystem }
import akka.stream.{ ActorMaterializer, KillSwitches }
import akka.stream.scaladsl.{ Keep, Sink }
import akka.util.Timeout

import java.util.concurrent.BlockingQueue
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

//class Consume(queue: BlockingQueue[Integer]) extends Actor with RequiresMessageQueue[BoundedMessageQueueSemantics] {
class Consume(queue: BlockingQueue[Integer]) extends Actor {
  implicit def ec: ExecutionContext = context.dispatcher

  override def receive: Receive = {
    case m: Integer =>
      queue.put(m)
  }
}

object StreamFLow {
  implicit val actoySystem             = ActorSystem("GlobalSystem")
  implicit val materializer            = ActorMaterializer()
  val actorRefFactory: ActorRefFactory = actoySystem
  implicit val ec: ExecutionContext    = actoySystem.dispatcher

  def main(args: Array[String]): Unit = {

    implicit val timeout = Timeout(1 seconds)

    val allData = BlockingQueueSource
      .getSource[Integer]
      .viaMat(KillSwitches.single[Integer])(Keep.both)
      .toMat(Sink.foreach(println))(Keep.both)
      .run()
//    val actor = actoySystem.actorOf(Props(new Consume(s)))

    val s      = allData._1._1
    val sw     = allData._1._2
    val result = allData._2
    for {
      i <- 1 to 1000
    } yield {
      println(s"Sending value $i")
      s.put(i)
    }
    Thread.sleep(2000)
    println(s.isStreamRunning)
    sw.shutdown()
//    while (s.isStreamRunning) {
//      sw.shutdown()
//    }
    Thread.sleep(1000)
    s.put(12312)
    println(s.isStreamRunning)
//    result.onComplete(_ => println("Done dana done"))
    actoySystem.whenTerminated.onComplete {
      case _ => println("Terminated")
    }
    actoySystem.terminate()
    println("State")
  }
}
