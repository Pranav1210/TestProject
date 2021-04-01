package com.facebook.rest.server

import org.http4s.util.string._
import org.http4s.headers.Authorization
import cats.data.Kleisli
import org.http4s.Request
import cats.effect.IO
import scala.util.Either
import org.http4s.Uri
import org.http4s.AuthScheme
import org.http4s.Credentials

object Auth {
  val authUser: Kleisli[IO, Request[IO], Either[String, String]] = Kleisli({ request =>
    IO(for {
      header <- request.headers.get(Authorization).toRight("Failed")
      token <- Option(header.toString).toRight("Failed")
    } yield token)
  })
}

object HttpClient {
  import org.http4s.client.dsl.io._
  import org.http4s.headers._
  import org.http4s.MediaType._
  import org.http4s.client.blaze._
  val httpClient = Http1Client[IO]().unsafeRunSync
}