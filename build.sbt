
scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "org.skinny-framework" %% "skinny-http-client" % "3.1.0",
  "org.json4s" %% "json4s-native" % "3.6.11"
)

scalacOptions ++= "-deprecation" :: "-feature" :: "-Xlint" :: "-Ywarn-unused:-imports,_" :: Nil

initialCommands in console :=
  """
  import skinny.http._
  import Implicits._
  import org.json4s._
  import org.json4s.JsonDSL._
  import org.json4s.native.JsonMethods._
  import HttpHelper._
  """
