package com.facebook.rest.server

import cats.effect.IO

sealed trait Value
case class Name(value: String) extends Value

trait Message[T] {
  def get(message: T): Value
  def set(message: T, value: Value): IO[Unit]
}

object Messenger {
  val msg = new Message[String] {
    def get(message: String): Value = new Name("Artificial")
    def set(message: String, value: Value) = IO.unit
  }
}