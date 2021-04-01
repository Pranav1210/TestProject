import scala.collection.convert.AsJavaConverters
val calls = List((1,4),(2,3),(5,10),(6,7),(4,8))
val start = calls.map(_._1).min
val end = calls.map(_._2).max

val timeLine = (1 to (end - start)).toList.asJava

calls.foldLeft(timeLine)((acc, curr) => timeLine.)
