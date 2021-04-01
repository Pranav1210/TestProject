import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.util.Try
import scala.xml.NodeSeq

object AkkaXMLReq {
  implicit val system = ActorSystem("YoYo")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  case class DataPack(name: String, age: Int)
  def main(args: Array[String]): Unit = {
    var ns:NodeSeq =     <xsp:response version="17.0" xmlns:xsp="http://schema.broadsoft.com/XspXMLInterface">
      <requestId>2</requestId>
      <sessionId>2</sessionId>
      <statusCode>200</statusCode>
      <reason>OK</reason>
      <xsp:payload>
        <Subscription xmlns="http://schema.broadsoft.com/xsi">
          <subscriptionId>3f040b3e-b5cd-415b-af48-1e2501a83f42</subscriptionId>
          <expires>3600</expires>
        </Subscription>
      </xsp:payload>
    </xsp:response>
    val route = path("xml") {
      get {
          complete(HttpEntity(ContentTypes.`text/xml(UTF-8)`, ns.toString()))
      } ~ post {
        println(s"Got a request as with $decodeRequest")
        entity(as[NodeSeq]) { ent =>
          ns = ent
          complete(HttpEntity(ContentTypes.`text/xml(UTF-8)`, ns.toString()))
        }
      }
    }
    Http().bindAndHandle(route, "localhost", 9090)
  }
}