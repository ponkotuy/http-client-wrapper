
scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  "org.skinny-framework" %% "skinny-http-client" % "3.0.3",
  "org.json4s" %% "json4s-native" % "3.6.7"
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
