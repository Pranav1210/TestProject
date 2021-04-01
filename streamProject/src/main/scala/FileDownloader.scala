//package com.cisco.ccc.cats
//
//import scala.concurrent.ExecutionContext
//import akka.actor.ActorSystem
//import akka.actor.ActorRefFactory
//import akka.stream.ActorMaterializer
//import scala.concurrent.ExecutionContext.global
//import scala.concurrent.Await
//import scala.concurrent.duration._
//import java.io.InputStream
//import akka.http.scaladsl.model.HttpRequest
//import akka.http.scaladsl.model.HttpMethods
//import akka.http.scaladsl.model.headers.RawHeader
//import akka.http.scaladsl.model.Uri
//import akka.http.scaladsl.Http
//import scala.concurrent.Future
//import akka.util.ByteString
//import akka.stream.scaladsl.Sink
//import akka.http.scaladsl.model.headers.Accept
//import akka.http.scaladsl.model.MediaType
//import akka.http.scaladsl.model.MediaTypes
//import scala.util.Success
//import scala.util.Failure
//import spray.json._
//
//object ChunkedEntities {
//  implicit val ec: ExecutionContext = global
//  implicit val actoySystem = ActorSystem("GlobalSystem")
//  implicit val materializer = ActorMaterializer()
//  val actorRefFactory: ActorRefFactory = actoySystem
//
//  def main(args: Array[String]): Unit = {
//    val request = HttpRequest(HttpMethods.GET,
//      Uri(s"https://rest.ext.appstaging.ciscoccservice.com/aws/api/blobs/csrs/INTERACTION:interactionContext__s:81ae1160-721a-11e9-a924-05324384feea"),
//      List(RawHeader("Authorization", "XTBsvjD4wV+tKdsckYUMizhPwEI=; tenantId=387"), RawHeader("from", "api@cconesp.net")))
//    val res  = Http().singleRequest(request).map(_.entity)
//    res.map(println(_))
//    res.map(response => response.entity.dataBytes.runFold(ByteString.empty)(_ ++ _)).map(r => r.map(println(_)))
//    //val result = res.flatMap(resp => resp.entity.toStrict(10.second)).flatMap(e=> e.dataBytes.runFold(ByteString.empty)(_ ++ _))
//    res.onComplete{
//      case Success(bytes) => println(s"Success is $bytes")
//        println(bytes.status.isFailure())
//      bytes.decodeString("US-ASCII").foreach(println(_))
//      case Failure(ex) => println(s"Failure is $ex")
//    }
//  }
//}