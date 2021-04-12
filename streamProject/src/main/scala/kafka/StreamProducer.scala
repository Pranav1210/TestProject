package kafka

import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.{ Sink, Source }
import org.apache.kafka.clients.producer.{ KafkaProducer, ProducerRecord }
import org.apache.kafka.common.serialization.StringSerializer

import java.util.concurrent.atomic.AtomicLong
import java.util.Properties
import scala.concurrent.ExecutionContext
import scala.concurrent._

object ProducerConfigs {

  def getKafkaProducerSetting()(implicit system: ActorSystem): ProducerSettings[String, String] = {
    val config = system.settings.config.getConfig("akka.kafka.producer")
    ProducerSettings(config, new StringSerializer, new StringSerializer)
      .withBootstrapServers("10.226.182.238:9092")
  }
}

object StreamProducer extends App {
  implicit val as                   = ActorSystem("Kafka")
  implicit def ec: ExecutionContext = as.dispatcher

  val sink        = Producer.plainSink[String, String](ProducerConfigs.getKafkaProducerSetting())
  val atomicClock = new AtomicLong()

  val props = new Properties()
  props.put("bootstrap.servers", "10.226.182.238:9092")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  val producer = new KafkaProducer[String, String](props)

  val f = Source(1 to 100000)
    .map(_.toString)
    .map(value => new ProducerRecord[String, String]("ccc_configevent", value, value))
    .mapAsync(8) { value =>
      Future(producer.send(value).get())
    }
    .runWith(Sink.ignore)

}
