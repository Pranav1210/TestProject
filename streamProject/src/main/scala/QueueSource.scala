import akka.japi
import akka.stream.{ Attributes, Outlet, SourceShape }
import akka.stream.scaladsl.Source
import akka.stream.stage.{
  AbstractGraphStageWithMaterializedValue,
  GraphStageLogic,
  GraphStageWithMaterializedValue,
  OutHandler
}

import java.util.concurrent.{ BlockingQueue, LinkedBlockingQueue }
import scala.util.Try

object BlockingQueueSource {
  def getSource[T]: Source[T, ConnectionInterface[T]] = Source.fromGraph(new QueueSource[T])
}

trait ConnectionInterface[T] {
  def put(item: T): Unit
  def isStreamRunning: Boolean
}

class QueueSource[T] extends GraphStageWithMaterializedValue[SourceShape[T], ConnectionInterface[T]] {
  val out: Outlet[T] = Outlet("Blocking.out")

  override def shape: SourceShape[T] = SourceShape(out)

  override def createLogicAndMaterializedValue(
      inheritedAttributes: Attributes
  ): (GraphStageLogic, ConnectionInterface[T]) = {
    val bq    = new LinkedBlockingQueue[T](100)
    val graph = new SourceGraphLogicCreator[T](shape, out, bq)
    val interface = new ConnectionInterface[T] {
      override def put(item: T): Unit       = bq.put(item)
      override def isStreamRunning: Boolean = !(graph.isPortClosed)
    }
    (graph, interface)
  }
}

private class SourceGraphLogicCreator[T](shape: SourceShape[T], out: Outlet[T], bq: BlockingQueue[T])
    extends GraphStageLogic(shape) {
  setHandler(
    out,
    new OutHandler {

      override def onPull(): Unit =
        Try(bq.take())
          .foreach(push(out, _))

      override def onDownstreamFinish(): Unit = {
        state = false
        println(s"DownStream finished")
      }
    }
  )
  var state = true

  def isPortClosed: Boolean = isClosed(out)

}
