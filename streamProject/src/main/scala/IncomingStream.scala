package com.cisco.ccc.cats

import akka.actor._
import akka.stream._
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.concurrent.ExecutionContext._
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

case class IncomingMessages(id: BigInt, message: String)

object StreamingApp {
  implicit val ec: ExecutionContext = global
  implicit val actoySystem = ActorSystem("GlobalSystem")
  implicit val materializer = ActorMaterializer()
  val actorRefFactory: ActorRefFactory = actoySystem
  val actorRef = actorRefFactory.actorOf(Props[MyActor])
  val sink = Sink.actorRefWithAck(actorRef, Init, Ack, Complete)
  val myFlow = Flow[IncomingMessages].collect {
    case msg : IncomingMessages => Future.successful(msg)
  }
    .mapAsync(1)(identity)
    .groupedWithin(1000, 1 second)
    .alsoTo(sink)
    .map(increment => increment)

  def main(args: Array[String]): Unit = {
    var bigInt = BigInt.apply(0)
    val queue =
  Source.queue[Long](5000, OverflowStrategy.backpressure)
    .throttle(5, 1 seconds, 100, ThrottleMode.Shaping)
    .to(Sink.foreach(println))
    .run()

//val path = Paths.get(new URI("/Users/pranarai/Downloads/ideaIC-2018.3.4.dmg"))
    
//FileIO.fromPath(path).runForeach(x => x.toString())
Source(1 to 1000)
  .mapAsync(1){x =>
    Thread.sleep((Math.random()*100).toLong)
    println(s"Offering $x")
    queue.offer(x)}
  .runWith(Sink.ignore)
//    while(true) {
//      bigInt = bigInt + 1
//      Thread.sleep((Math.random() * 10).toLong)
//      println(bigInt)
//      val number = (Math.random()*100).toInt
//      val value = IncomingMessages(bigInt, ((Math.random()*number).toInt).toString)
////      Source.single(value).via(myFlow).throttle(5, 10 seconds, 10, ThrottleMode.Shaping).runForeach { x =>
////        Thread.sleep((Math.random() * 10).toLong)
////        println(x) }
//
//
//
//    }
  }
}

case object Hello
case object Ack
case object Init
case object Complete

class MyActor extends Actor {
  def receive: Receive = {
    case Hello => println("Hello")
    case Ack => println("Ack")
    case Init => println("Init")
    case Complete => println("Complete")
  }
}