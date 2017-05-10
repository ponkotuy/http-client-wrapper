
scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "org.skinny-framework" %% "skinny-http-client" % "2.3.6",
  "org.json4s" %% "json4s-native" % "3.5.2"
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
