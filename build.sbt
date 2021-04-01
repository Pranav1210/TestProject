lazy val commonSettings = Seq(
  organization := "com.cisco",
  version := "1.0",
  scalacOptions += "-Ypartial-unification",
  scalaVersion := "2.12.11")

lazy val root = Project(
  id = "ccc",
  base = file("."))
  .settings(commonSettings: _*)
  .aggregate(streamProject)

lazy val streamProject = Project(
    id = "streamProject",
    base = file("streamProject"))
    .settings(commonSettings: _*)
    .settings(Dependencies.streamProject: _*)
