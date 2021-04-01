//package server
//
//import akka.actor.{ Actor, ActorRef, ActorSystem, Cancellable, PoisonPill, Props }
//import akka.io.{ IO, Tcp }
//import java.net.InetSocketAddress
//
//import scala.concurrent.duration._
//import Tcp._
//import akka.util.ByteString
////import server.SimplisticHandler.{ Done, KeepAlive, Publish }
//
//import scala.collection.mutable
//
//object TcpStream {
//  implicit val as = ActorSystem.create("tcp")
//
//  // def main(args: Array[String]): Unit = {}
//
////    def callback(data: ByteString): Option[ByteString] = Option(data)
////    as.actorOf(Props(new Server(callback)))
//}
////
////class Server(CALLBACK: ByteString => Option[ByteString]) extends Actor {
////
////  import context.system
////
////  IO(Tcp) ! Bind(self, new InetSocketAddress("www.google.com", 443 ))
////
////  def receive = {
////    case CommandFailed(_: Bind) => context.stop(self)
////
////    case c @ Connected(remote, local) =>
////      val connection = sender()
////      val handler = context.actorOf(Props(new SimplisticHandler(connection, CALLBACK)))
////      connection ! Register(handler)
////  }
////}
////
////object SimplisticHandler {
////  case object Done
////  case object Publish
////  case object KeepAlive
////  case object Connected
////}
////
////class SimplisticHandler(connection: ActorRef, CALLBACK: ByteString => Option[ByteString]) extends Actor {
////  import Tcp._
////  implicit val ec = context.dispatcher
////  val actorref: ActorRef = connection
////  val dataStream = mutable.MutableList.empty[ByteString]
////  context.watch(connection)
////  var r: Cancellable = _
////
////  def receive = {
////    case Received(data) => {
////      CALLBACK(data).foreach(println(_))
////      if(data.utf8String == "Kill")
////        dataStream += data
////      if(dataStream.length > 3) {
////        actorref ! Write(dataStream.fold(ByteString.empty)((acc, cur) =>acc ++ cur))
////        dataStream.clear()
////      }
////    }
////    case Done => {
////      println("Done")
////      actorref ! Write(ByteString("GET / HTTP/1.1\r\nHost: www.google.com\r\n\r\n"))
////    }
////
////    case KeepAlive =>
////      r = context.system.scheduler.scheduleOnce(30 seconds, self, PoisonPill)
////
////    case Connected => r.cancel()
////
////    case Publish => {
////      if(dataStream.nonEmpty) {
////        actorref ! Write(dataStream.fold(ByteString.empty)((acc, cur) =>acc ++ cur))
////        dataStream.clear()
////      }
////    }
////    case PeerClosed     => context.stop(self)
////  }
////
////  context.system.scheduler.schedule(
////    initialDelay = 5 seconds,
////    interval = 5 seconds,
////    receiver = self,
////    message = SimplisticHandler.Done
////  )
////
////  context.system.scheduler.schedule(
////    initialDelay = 5 seconds,
////    interval = 5 seconds,
////    receiver = self,
////    message = SimplisticHandler.KeepAlive
////  )
////
////  context.system.scheduler.schedule(
////    initialDelay = 20 seconds,
////    interval = 20 seconds,
////    receiver = self,
////    message = SimplisticHandler.Publish
////  )
////}
