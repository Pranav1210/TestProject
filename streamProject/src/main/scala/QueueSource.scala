import akka.japi
import akka.stream.{ Attributes, Outlet, SourceShape }
import akka.stream.scaladsl.Source
import akka.stream.stage.{ AbstractGraphStageWithMaterializedValue, GraphStageLogic, OutHandler }

import java.util.concurrent.{ BlockingQueue, LinkedBlockingQueue }
import scala.util.Try

object BlockingQueueSource {
  def getSource[T]: Source[T, ConnectionInterface[T]] = Source.fromGraph(new QueueSource[T])
}

trait ConnectionInterface[T] {
  def put(item: T): Unit
  def isStreamRunning: Boolean
}

class QueueSource[T] extends AbstractGraphStageWithMaterializedValue[SourceShape[T], ConnectionInterface[T]] {
  val out: Outlet[T] = Outlet("Blocking.out")

  override def createLogicAndMaterializedValuePair(
      inheritedAttributes: Attributes
  ): japi.Pair[GraphStageLogic, ConnectionInterface[T]] = {
    val bq    = new LinkedBlockingQueue[T](100)
    val graph = new SourceGraphLogicCreator[T](shape, out, bq)
    val interface = new ConnectionInterface[T] {
      override def put(item: T): Unit       = bq.put(item)
      override def isStreamRunning: Boolean = graph.isStreamRunning
    }
    japi.Pair.create(graph, interface)
  }

  override def shape: SourceShape[T] = SourceShape(out)
}

private class SourceGraphLogicCreator[T](shape: SourceShape[T], out: Outlet[T], bq: BlockingQueue[T])
    extends GraphStageLogic(shape) with OutHandler {
  setHandler(out, this)

  private var state = true

  override def onPull(): Unit =
    Try(bq.take())
      .map { x =>
        x match {
          case y: Integer if y % 100 == 0 =>
//            Thread.sleep(1000)
            x
          case _ => x
        }
      }
      .foreach(push(out, _))

}
