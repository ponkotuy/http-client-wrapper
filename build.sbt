
scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "org.skinny-framework" %% "skinny-http-client" % "3.1.0",
  "org.json4s" %% "json4s-native" % "3.6.11"
)

scalacOptions in compile ++= "-deprecation" :: "-feature" :: "-Xlint" :: "-Ywarn-unused" :: Nil

initialCommands in console :=
  """
  import skinny.http._
  import HttpHelper._
  import Implicits._
  import org.json4s._
  import org.json4s.JsonDSL._
  import org.json4s.native.JsonMethods._
  """
