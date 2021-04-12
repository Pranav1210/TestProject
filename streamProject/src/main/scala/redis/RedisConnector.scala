package redis

import io.lettuce.core._
import io.lettuce.core.cluster.{ ClusterClientOptions, ClusterTopologyRefreshOptions, RedisClusterClient }
import io.lettuce.core.support.ConnectionPoolSupport
import org.apache.commons.pool2.impl.{ GenericObjectPool, GenericObjectPoolConfig }
import redis.Config.clusterClientOptions

import java.util.concurrent.atomic.AtomicInteger
import scala.compat.java8.FunctionConverters.asJavaSupplier
import scala.io.Source
import collection.JavaConverters._
import scala.util.{ Failure, Success, Try }

object Config {

  val refreshOptions: ClusterTopologyRefreshOptions = ClusterTopologyRefreshOptions
    .builder()
    .enablePeriodicRefresh()
    .enableAllAdaptiveRefreshTriggers()
    .build();

  val clusterClientOptions: ClusterClientOptions = ClusterClientOptions
    .builder()
    .autoReconnect(true)
    .maxRedirects(4)
    .topologyRefreshOptions(refreshOptions)
    .build();
}

object RedisConnector {
  // val system: ActorSystem = ActorSystem("Reducer")
//  implicit def ec: ExecutionContext = system.dispatcher
  val requestCount = new AtomicInteger()

  def main(args: Array[String]): Unit = {
//    val uris   = (0 to 5).map(6000 + _).map(RedisURI.create("localhost", _)).toList
//    val 4client = RedisClusterClient.create(uris.asJava)
    val client = RedisClient.create("redis://localhost:6379")
    client.setOptions(clusterClientOptions)
    val connection          = client.connect()
    val file                = Source.fromResource("redisScript.lua").mkString
    val script2Hash: String = connection.sync().scriptLoad(file)
    val dataType            = ScriptOutputType.VALUE
    println(script2Hash)
    while (true) {
      val cm  = scala.io.StdIn.readLine()
      val lim = scala.io.StdIn.readLine()
      Try {
        println(
          client
            .connect()
            .sync()
            .evalsha[String](script2Hash, dataType, List("{vmm}:vmm", "{vmm_2}:vmm_orgId").toArray, cm, lim)
        )
      } match {
        case Success(value)     => println(value)
        case Failure(exception) => println("Failed with exception {}", exception.getMessage)
      }
    }
  }
}
