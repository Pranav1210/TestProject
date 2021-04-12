package kafka

import akka.actor.ActorSystem
import io.netty.util.internal.logging.Slf4JLoggerFactory
import org.apache.kafka.common.serialization.{ Serdes, StringDeserializer, StringSerializer }

import java.util.Properties
import java.util.concurrent.TimeUnit
import org.apache.kafka.streams.kstream.{ KGroupedStream, KStream, Materialized }
import org.apache.kafka.streams.{ KafkaStreams, StreamsBuilder, StreamsConfig }
import org.apache.kafka.streams.state.Stores

import java.util.logging.Logger

//object Functions {
//
//  def getKafkaConsumerSource()(implicit system: ActorSystem): ConsumerSettings[String, String] = {
//    val config = system.settings.config.getConfig("akka.kafka.consumer")
//    ConsumerSettings(config, new StringDeserializer, new StringDeserializer)
//      .withProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, s"10.226.182.238:9092")
//      .withProperty(ConsumerConfig.GROUP_ID_CONFIG, "mock-load-server-test")
//      .withProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
//      .withProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000")
//  }
//}

object StreamConsumer extends App {

  implicit val as    = ActorSystem("Kafka")
  def init(): String = ""

  val props: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application1")
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "10.226.182.238:9092")
    p.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, "16")
    p.put(StreamsConfig.NUM_STANDBY_REPLICAS_CONFIG, "1")
    p.put(StreamsConfig.PROBING_REBALANCE_INTERVAL_MS_CONFIG, "65000")
    p
  }
  val builder: StreamsBuilder                   = new StreamsBuilder
  val textLines: KGroupedStream[String, String] = builder.stream[String, String]("ccc_configevent1").groupByKey()
  textLines
    .aggregate(
      () => "",
      (k: String, v: String, s: String) => s"$k $v $s",
      Materialized
        .as(Stores.inMemoryKeyValueStore("InteractionStateStore"))
        .withKeySerde(Serdes.serdeFrom(new StringSerializer, new StringDeserializer()))
        .withValueSerde(Serdes.serdeFrom(new StringSerializer, new StringDeserializer()))
    )
  val streams: KafkaStreams = new KafkaStreams(builder.build(), props)
  streams.start()
  as.registerOnTermination(() => streams.close())
}
