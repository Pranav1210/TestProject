package server

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.{Keep, Sink, Source, Tcp}
import akka.util.ByteString

object TcpStreamClient {
    val as = ActorSystem.create("TestActor")
    def getListener(listener: ActorRef, endPoint: InetSocketAddress)
                   (implicit system: ActorSystem): ByteString => Unit = {
      implicit val mat: ActorMaterializer = ActorMaterializer()
      val source = Source.actorRef(2, OverflowStrategy.dropBuffer).recover {
        case _ => ByteString("")
      }
      val connection = Tcp().outgoingConnection(endPoint)
      val sink = Sink.actorRef(listener, "Done")
      val runningStream = connection.to(sink).runWith(source)

      def runner (message: ByteString): Unit = {
        Thread.sleep((Math.random()*100).toInt)
        // println("Called Runner")
        runningStream ! message
      }
      runner
    }

  def main(args: Array[String]): Unit = {
    implicit val localSystem = as
    val listener = as.actorOf(Props(new ListenerGuy()), "Client")
    val teller = getListener(listener, new InetSocketAddress("localhost", 9090))
    println("Calling Runner")
    while(true) {
      teller(ByteString(s"This is some Data"))
    }
  }

  class ListenerGuy extends Actor {
    override def receive: Receive = {
      case a : ByteString => println(a.utf8String)
    }
  }
}