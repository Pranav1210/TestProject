import io.circe._
import io.circe.parser._
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.semiauto._
import io.circe.parser.decode
import org.apache.commons.lang3.StringEscapeUtils.unescapeJava
case class InnerTest(number: String)
case class First(name: String, sec: String, n: String)

object CirceTests extends App {

  val j                                   = """{
      |  "name" : "Hey",
      |  "sec" : "Dude",
      |  "n" : {  "name1" : "Hey",  "sec2" : "Dude",  "n3" : {    "number4" : "I am here"  }}
      |}""".stripMargin.replaceAll("\\n", "")
  implicit val fooDecoder: Decoder[First] = deriveDecoder
  implicit val fooEncoder: Encoder[First] = deriveEncoder

  val nasdaq: First = decode[First](j).right.get
  println(nasdaq.asJson.toString())

}
