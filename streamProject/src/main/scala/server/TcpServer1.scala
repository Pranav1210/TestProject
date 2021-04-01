package server

import akka.actor.{ Actor, ActorRef, ActorSystem, PoisonPill, Props }
import akka.io.{ IO, Tcp }
import java.net.InetSocketAddress

import scala.concurrent.duration._
import Tcp._
import akka.util.ByteString
//import server.SimplisticHandler.{Done, Publish}

import scala.collection.mutable

object TcpServer {

  def main(args: Array[String]): Unit = {
    implicit val as = ActorSystem.create("tcp")
//    as.actorOf(Props[Server1])
  }
}
//
//class Server1 extends Actor {
//
//  import context.system
//
//  IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 9090))
//
//  def receive = {
//    case b @ Bound(localAddress) =>
//      context.parent ! b
//
//    case CommandFailed(_: Bind) => context.stop(self)
//
//    case c @ Connected(remote, local) =>
//      println(s"Remote is ${remote}")
//      println(s"Local is ${local}")
//      val connection = sender()
//      val handler    = context.actorOf(Props(new SimplisticHandler1(connection)))
//      connection ! Register(handler)
//  }
//}
//
//object SimplisticHandler1 {
//  case object Done
//  case object Publish
//}
//
//class SimplisticHandler1(connection: ActorRef) extends Actor {
//  import Tcp._
//  implicit val ec        = context.dispatcher
//  val actorref: ActorRef = connection
//  val dataStream         = mutable.MutableList.empty[ByteString]
//  context.watch(connection)
//
//  def receive = {
//    case Received(data) => {
//      println(s"Data received is ${data.utf8String}")
//      dataStream += data
//      if (dataStream.length > 8196) {
//        actorref ! Write(dataStream.fold(ByteString.empty)((acc, cur) => acc ++ cur))
//        dataStream.clear()
//      }
//    }
//    case Done => {
//      actorref ! Write(ByteString(s"Hey I am here"))
//    }
//
//    case Publish => {
//      if (!dataStream.isEmpty) {
//        actorref ! Write(dataStream.fold(ByteString.empty)((acc, cur) => acc ++ cur))
//        dataStream.clear()
//      }
//    }
//    case PeerClosed => context.stop(self)
//    case _          => context.stop(self)
//  }
//
//  context.system.scheduler.schedule(
//    initialDelay = 5 seconds,
//    interval = 5 seconds,
//    receiver = self,
//    message = SimplisticHandler.Done
//  )
//
//  context.system.scheduler.schedule(
//    initialDelay = 20 seconds,
//    interval = 20 seconds,
//    receiver = self,
//    message = SimplisticHandler.Publish
//  )
//}
