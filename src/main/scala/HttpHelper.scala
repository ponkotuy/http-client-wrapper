import org.json4s._
import org.json4s.native.JsonMethods._
import skinny.http._

import java.net.URLEncoder
import java.nio.file.{Files, Paths}
import scala.jdk.CollectionConverters._

object HttpHelper extends WithHeader(Map.empty) {
  def withBearer(auth: String): WithHeader = {
    WithHeader(Map("Authorization" -> s"Bearer ${auth}"))
  }

  def withHeaders(headers: (String, String)*) = WithHeader(headers.toMap)

  def printLines(str: String): Unit = {
    str.lines.foreach(println)
  }

  def withHeaders(headers: (String, String)*): WithHeader = WithHeader(headers.toMap)

  def loadJson(fname: String): JValue =
    parse(Files.readAllLines(Paths.get(fname)).asScala.mkString(""))

  def encode(str: String): String = URLEncoder.encode(str, "UTF-8")

  def pretty(json: JValue) = org.json4s.native.prettyJson(render(json))
}

case class Session(res: Response, req: Request, headers: Map[String, String]) extends HttpWrapper {
  def addHeader(key: String, value: String): Session =
    Session(res, req, headers.updated(key, value))

  def json: JValue = parse(new String(res.body))

  def string: String = new String(res.body)
}

case class WithHeader(headers: Map[String, String]) extends HttpWrapper {
  override def addHeader(key: String, value: String): WithHeader =
    WithHeader(headers.updated(key, value))
}

case class Cookie(raw: Map[String, String]) {
  override def toString: String = raw.map { case (k, v) => s"$k=$v" }.mkString(";")
  def header: (String, String) = "Cookie" -> toString
}

object Cookie {
  def fromStr(str: String): Cookie = {
    val raw: Map[String, String] = str.split(';').flatMap { part =>
      part.trim.split('=') match {
        case Array(k) => Some(k -> "")
        case Array(k, v) => Some(k -> v)
        case _ => None
      }
    }.toMap
    Cookie(raw)
  }

  def empty: Cookie = Cookie(Map.empty)
}

case class HttpHelperException(mes: String) extends RuntimeException(mes)

case class Host(name: String) extends AnyVal

case class Protocol(name: String) extends AnyVal

case class UserAgent(name: String) extends AnyVal

object Implicits {
  import scala.language.implicitConversions

  implicit var host: Host = Host("localhost")
  implicit var protocol: Protocol = Protocol("http")
  implicit var ua: UserAgent = UserAgent("http-client-wrapper default user agent")

  implicit def toHost(str: String): Host = Host(str)
  implicit def toProtocol(str: String): Protocol = Protocol(str)
  implicit def toUserAgent(str: String): UserAgent = UserAgent(str)
  implicit def removeSession(session: Session): Response = session.res
}
