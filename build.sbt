
scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "org.skinny-framework" %% "skinny-http-client" % "3.0.1",
  "org.json4s" %% "json4s-native" % "3.6.6"
)

initialCommands in console :=
  """
  import skinny.http._
  import HttpHelper._
  import Implicits._
  import org.json4s._
  import org.json4s.JsonDSL._
  import org.json4s.native.JsonMethods._
  """
