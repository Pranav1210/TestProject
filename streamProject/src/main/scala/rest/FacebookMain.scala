package com.facebook.rest.server

import cats.effect._
// import cats.effect._

import fs2.StreamApp
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeBuilder

import scala.concurrent.ExecutionContext.Implicits.global

object MediaServer extends StreamApp[IO] with Http4sDsl[IO] {
  val server = HttpService[IO] {a orElse b}

  var map = Map[String, String]()

  def a(): PartialFunction[Request[IO], IO[Response[IO]]] =  {
    case r@ GET -> Root / "hello" / person =>
      println(s"Get get request on ${r.headers}")
      println(s"Get get request on ${r.authType}")
      println(s"Body is on $person")
      IO(Response(Status.Ok))
    case _ => IO(Response(Status.Accepted))
  }
  def b(): PartialFunction[Request[IO], IO[Response[IO]]]  = {
    case POST => IO(Response(Status.Ok))
  }

  def stream(args: List[String], requestShutdown: IO[Unit]) = {
    BlazeBuilder[IO]
    .bindHttp(8080, "0.0.0.0")
    .mountService(server, "/")
    .serve
  }
}