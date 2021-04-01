package com.cisco.ccc.emm.kafka.actor

import akka.event.{ EventBus, LookupClassification }
import akka.actor.ActorRef
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

final case class MsgEnvelope(taskId: Int, taskStateChange: Int) 
class LookupBus extends EventBus with LookupClassification {
  type Event = MsgEnvelope
  type Classifier = Int
  type Subscriber = ActorRef

  override protected def publish(event: Event, subscriber: Subscriber): Unit = {
    subscriber ! event.taskStateChange
  }
  override protected def classify(event: Event): Classifier = event.taskId
  override protected def compareSubscribers(a: Subscriber, b: Subscriber): Int =
    a.compareTo(b)
  override protected def mapSize: Int = 128
}

class EventHandler() extends Actor {
  
  var atClock: AtomicInteger = new AtomicInteger(0)
  def receive = {
    case x :: Nil => println(x)
    case x : Int => { 
      println(atClock.incrementAndGet())
      println(x)}
    case _ => println("No idea what this is")
  }
}

object TestSpace {
  val system = ActorSystem.create()
  val mm = system.actorOf(Props[EventHandler])
  val taskId = (Math.random() * 100).toInt
  def sub(eb: LookupBus, actorRef: ActorRef, task: Int) = {
    eb.subscribe(actorRef, task)
  }
  val eventBus = new LookupBus()
  val atClock: AtomicInteger = new AtomicInteger(0)
  implicit val ec: ExecutionContext = system.dispatcher
  def main(args: Array[String]): Unit = {
    sub(eventBus, mm, taskId)
    sub(eventBus, mm, (taskId + 55) % 100)

    Future {
      while (true) {
        val number = (Math.random() * 100).toInt
        println(s"Published number is future 1 $number and count")
        eventBus.publish(MsgEnvelope(number, number))
      }
    }
    Future {
      while (true) {
        val number = (Math.random() * 100).toInt
        println(s"Published number is future 2 $number and count")
        eventBus.publish(MsgEnvelope(number, number))
      }
    }
    while (true) {
      val number = (Math.random() * 100).toInt
      println(s"Published number is future 3 $number and count")
      eventBus.publish(MsgEnvelope(number, number))
    }
  }
}