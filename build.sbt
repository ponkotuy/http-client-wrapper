
scalaVersion := "2.13.12"

libraryDependencies ++= Seq(
  "org.skinny-framework" %% "skinny-http-client" % "4.0.0",
  "org.json4s" %% "json4s-native" % "4.0.6"
)

scalacOptions ++= "-deprecation" :: "-feature" :: "-Xlint" :: "-Ywarn-unused:-imports,_" :: Nil

console / initialCommands :=
  """
  import skinny.http._
  import Implicits._
  import org.json4s._
  import org.json4s.JsonDSL._
  import org.json4s.native.JsonMethods._
  import HttpHelper._
  """
