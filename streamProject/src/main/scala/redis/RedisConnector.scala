package redis

import akka.actor.ActorSystem
import io.lettuce.core._
import io.lettuce.core.api.async.RedisAsyncCommands
import io.lettuce.core.api.sync.RedisCommands
import io.lettuce.core.support.ConnectionPoolSupport
import org.apache.commons.pool2.impl.{ GenericObjectPool, GenericObjectPoolConfig }

import java.util.concurrent.atomic.AtomicInteger
import scala.compat.java8.FunctionConverters.asJavaSupplier
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

object RedisConnector {
  val system: ActorSystem = ActorSystem("Reducer")
//  implicit def ec: ExecutionContext = system.dispatcher
  val requestCount = new AtomicInteger()

  def main(args: Array[String]): Unit = {
    val client = RedisClient.create("redis://127.0.0.1:6379")
    val connectionConfig: GenericObjectPoolConfig[io.lettuce.core.api.StatefulRedisConnection[String, String]] =
      new GenericObjectPoolConfig()
    connectionConfig.setMaxTotal(4)
    val pool: GenericObjectPool[io.lettuce.core.api.StatefulRedisConnection[String, String]] =
      ConnectionPoolSupport.createGenericObjectPool(asJavaSupplier({ client.connect }), connectionConfig)

    val connection = pool.borrowObject()
    val startTime  = System.currentTimeMillis()

    val keyToTest = args(0)
    val cancel = system.scheduler.schedule(1 milliseconds, 1 milliseconds) {
      endCall(connection.async(), keyToTest)
    }

    for {
      i <- 1 to 10000
    } yield {
      while (!rateLimit(connection.sync(), keyToTest, 300)) {}
      // println(s"Hello $i")
    }
    cancel.cancel()
    val totalTime = System.currentTimeMillis() - startTime
    println(s"Total time is ${totalTime / 1000}")
    Thread.sleep(3000)
    println(s"Total number of request is ${requestCount.get()}")
    pool.close()
    system.terminate()
  }

  private def endCall(connection: RedisAsyncCommands[String, String], key: String): Boolean = {
    Future { requestCount.incrementAndGet() }
    connection.eval[String](
      """
        | if tonumber(redis.call('get', KEYS[1]) or 0) > 0 then
        |   redis.call("DECR", KEYS[1])
        | else
        |   redis.call("INCR", KEYS[1])
        | end
        |""".stripMargin,
      ScriptOutputType.VALUE,
      key
    )
    true
  }

  private def rateLimit(connection: RedisCommands[String, String], key: String, limit: Integer): Boolean = {
    val keys = List(limit.toString, key)
    Future { requestCount.incrementAndGet() }
    val result = connection.eval[String](
      """
        | if tonumber(KEYS[1]) > tonumber(redis.call('get', KEYS[2]) or 0) then 
        |   redis.call("INCR", KEYS[2])
        |   return ARGV[1]
        | else 
        |   return ARGV[2]
        | end
        |""".stripMargin,
      ScriptOutputType.VALUE,
      keys.toArray,
      "true",
      "false"
    )
    // println(result)
    if (result == "true")
      true
    else
      false
  }

}
