import akka.actor.{ Actor, ActorRefFactory, ActorSystem, Props }
import akka.dispatch.{ BoundedMessageQueueSemantics, RequiresMessageQueue }
import akka.pattern._
import akka.stream.{ ActorMaterializer, KillSwitches, UniqueKillSwitch }
import akka.stream.scaladsl.{ Keep, Sink }
import akka.util.Timeout

import java.util.concurrent.BlockingQueue
import scala.concurrent.{ Await, ExecutionContext }
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext._

//class Consume(queue: BlockingQueue[Integer]) extends Actor with RequiresMessageQueue[BoundedMessageQueueSemantics] {
class Consume(queue: BlockingQueue[Integer]) extends Actor {

  var uniqueKillSwitch: Option[UniqueKillSwitch] = None

  implicit def ec: ExecutionContext = context.dispatcher

  var time = 0L

  override def receive: Receive = {
    case m: Integer =>
      queue.put(m)
    // time = time + System.currentTimeMillis() - startTime
//      println(s"Total time is ${System.currentTimeMillis() - startTime}")
    case _ => println(s"Total time is ${time}")
  }

  override def postStop(): Unit = println(s"Total time is ${time}")

}

object StreamFLow {
  implicit val ec: ExecutionContext    = global
  implicit val actoySystem             = ActorSystem("GlobalSystem")
  implicit val materializer            = ActorMaterializer()
  val actorRefFactory: ActorRefFactory = actoySystem

  def main(args: Array[String]): Unit = {

    implicit val timeout = Timeout(1 seconds)

    val (s, sw) = BlockingQueueSource
      .getSource[Integer]
      .viaMat(KillSwitches.single)(Keep.both)
      .to(Sink.foreach(println))
      .run()
//    val actor = actoySystem.actorOf(Props(new Consume(s)))

    for {
      i <- 1 to 1000
    } yield {
      println(s"Sending value $i")
      s.put(i)
    }
    println(s.isStreamRunning)
    Thread.sleep(2000)
    sw.shutdown()
    Thread.sleep(2000)
    println(s.isStreamRunning)
    //    actor ! "1"
  }
}
