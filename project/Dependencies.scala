import sbt._
import Keys._

object Dependencies {

  val akka23Version   = "2.3.9"
  val akka25Version   = "2.5.32"
  val akkaHttpVersion = "10.2.0"
  val http4sVersion   = "0.18.21"
  val circeVersion    = "0.11.1"

  val akka23Actor = "com.typesafe.akka" %% "akka-actor" % akka23Version
  val akka23SLF4J = "com.typesafe.akka" %% "akka-slf4j" % akka23Version

  val akka25Actor    = "com.typesafe.akka"  %% "akka-actor"              % akka25Version
  val akka25SLF4J    = "com.typesafe.akka"  %% "akka-slf4j"              % akka25Version
  val akkaStream     = "com.typesafe.akka"  %% "akka-stream"             % akka25Version
  val akkaHttp       = "com.typesafe.akka"  %% "akka-http"               % akkaHttpVersion
  val akkaXml        = "com.typesafe.akka"  %% "akka-http-xml"           % "10.1.8"
  val alpAkkaXml     = "com.lightbend.akka" %% "akka-stream-alpakka-xml" % "1.0.0"
  val akkaHttpJson   = "com.typesafe.akka"  %% "akka-http-spray-json"    % akkaHttpVersion
  val typesafeConfig = "com.typesafe"       % "config"                   % "1.3.1"

  val akkaHttpSession = "com.softwaremill.akka-http-session" %% "core" % "0.3.0"

  val sparkStreaming          = "org.apache.spark"   %% "spark-streaming"           % "1.6.3"
  val sparkStreamingkafka     = "org.apache.spark"   %% "spark-streaming-kafka"     % "1.6.3"
  val sparkCassandraConnector = "com.datastax.spark" %% "spark-cassandra-connector" % "1.6.3"

  val cats        = "org.typelevel" %% "cats-core"        % "1.5.0"
  val catsEffects = "org.typelevel" %% "cats-effect-laws" % "1.1.0" % "test"

  val cassandraDriver = "com.datastax.cassandra" % "cassandra-driver-core" % "3.5.0"

  val javaMail     = "com.sun.mail"       % "javax.mail"    % "1.6.1"
  val commonsCodec = "commons-codec"      % "commons-codec" % "1.10"
  val commonsLang3 = "org.apache.commons" % "commons-lang3" % "3.5"
  val commonsNet   = "commons-net"        % "commons-net"   % "3.5"

  val commonValidator = "commons-validator"  % "commons-validator"   % "1.6"
  val fasterXmlUUID   = "com.fasterxml.uuid" % "java-uuid-generator" % "3.1.4"

  val jsonLens = "net.virtual-void" %% "json-lenses" % "0.6.2"
  val ficus    = "com.iheart"       %% "ficus"       % "1.4.3"

  val findbugs = "com.google.code.findbugs" % "jsr305" % "3.0.1"

  val gatlingBundle = "io.gatling" % "gatling-bundle" % "2.3.0" artifacts (Artifact("gatling-bundle",
                                                                                    "zip",
                                                                                    "zip",
                                                                                    "bundle"))
  val gatlingCharts = "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.3.0"
  val gatlingTestFW = "io.gatling"            % "gatling-test-framework"    % "2.3.0"

  val http4s       = "org.http4s" %% "http4s-dsl"          % http4sVersion
  val http4sServer = "org.http4s" %% "http4s-blaze-server" % http4sVersion
  val http4sClient = "org.http4s" %% "http4s-blaze-client" % http4sVersion

  val guava = "com.google.guava" % "guava" % "25.1-jre"

  val hdrHistogram = "org.hdrhistogram" % "HdrHistogram" % "2.1.9"

  val javaWebsocket = "org.java-websocket" % "Java-WebSocket" % "1.3.8"

  val jedis   = "redis.clients" % "jedis"        % "2.9.0"
  val lettuce = "io.lettuce"    % "lettuce-core" % "5.3.6.RELEASE"

  val jerseyBundle = "com.sun.jersey" % "jersey-bundle" % "1.19.3"

  val jodaConvert = "org.joda"  % "joda-convert" % "1.8.1"
  val jodaTime    = "joda-time" % "joda-time"    % "2.9.7"

  val joseJWT = "com.nimbusds" % "nimbus-jose-jwt" % "2.26"

  val logbackClassic         = "ch.qos.logback"                   % "logback-classic"          % "1.1.8"
  val logbackLogstashEncoder = "net.logstash.logback"             % "logstash-logback-encoder" % "4.8"
  val jacksonCore            = "com.fasterxml.jackson.core"       % "jackson-core"             % "2.9.6"
  val jacksonDatabind        = "com.fasterxml.jackson.core"       % "jackson-databind"         % "2.9.6"
  val jacksonDataFormat      = "com.fasterxml.jackson.dataformat" % "jackson-dataformat-cbor"  % "2.9.6"

  val circeCore          = "io.circe" %% "circe-core"           % circeVersion
  val circeGeneric       = "io.circe" %% "circe-generic"        % circeVersion
  val circeParser        = "io.circe" %% "circe-parser"         % circeVersion
  val circeShapes        = "io.circe" %% "circe-shapes"         % circeVersion
  val circeGenericExtras = "io.circe" %% "circe-generic-extras" % circeVersion

  val request  = "com.lihaoyi"            %% "requests"  % "0.1.7"
  val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "1.1.0"

  val scOpt          = "com.github.scopt"      %% "scopt"             % "3.5.0"
  val slf4j          = "org.slf4j"             % "slf4j-api"          % "1.7.22"
  val sttp           = "com.softwaremill.sttp" %% "core"              % "1.5.11"
  val sttpAkk        = "com.softwaremill.sttp" %% "akka-http-backend" % "1.5.11"
  val sttpJson       = "com.softwaremill.sttp" %% "json4s"            % "1.5.11"
  val sttpNativeJson = "org.json4s"            %% "json4s-native"     % "3.6.0"

  val kafkaClients = "org.apache.kafka" % "kafka-clients" % "0.10.2.1"
  val kafkaStreams = "org.apache.kafka" % "kafka-streams" % "0.10.2.1"
  val kafkaCore    = "org.apache.kafka" %% "kafka"        % "0.8.2.1"

  val amazonKinesisClient   = "com.amazonaws" % "amazon-kinesis-client"   % "1.8.5"
  val amazonKinesisProducer = "com.amazonaws" % "amazon-kinesis-producer" % "0.12.5"
  val amazonKinesisSDK      = "com.amazonaws" % "aws-java-sdk-kinesis"    % "1.11.228"
  val amazonSdk             = "com.amazonaws" % "aws-java-sdk-kms"        % "1.11.511"

  val rateLimitJ = "es.moki.ratelimitj" % "ratelimitj-inmemory" % "0.4.1"

  val apacheEmail       = "org.apache.commons" % "commons-email" % "1.5"
  val apacheCommonsPool = "org.apache.commons" % "commons-pool2" % "2.9.0"

  val uuid = "com.eaio.uuid" % "uuid" % "3.2"

  val scalaParser = "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.1"
  val dispatchV   = "0.11.3"
  val dispatch    = "net.databinder.dispatch" %% "dispatch-core" % dispatchV

  val streamProject = Seq(
    libraryDependencies ++= Seq(
      amazonSdk,
      lettuce,
      akka25Actor,
      akkaXml,
      alpAkkaXml,
      akka25SLF4J,
      akkaStream,
      akkaHttp,
      apacheCommonsPool,
      akkaHttpJson,
      cats,
      slf4j,
      request,
      catsEffects,
      commonsLang3,
      scalaXml,
      http4s,
      http4sServer,
      http4sClient,
      circeCore,
      circeGeneric,
      circeParser,
      circeShapes,
      circeGenericExtras
    )
  )
}
