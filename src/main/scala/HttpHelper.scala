
import skinny.http._
import scala.collection.breakOut

object HttpHelper extends HttpWrapper {
  import scala.language.implicitConversions

  override def cookie = Cookie.empty

  implicit var host: Host = Host("localhost")
  implicit var protocol: Protocol = Protocol("http")

  implicit def toHost(str: String): Host = Host(str)
  implicit def toProtocol(str: String): Protocol = Protocol(str)
}

case class Session(cookie: Cookie, res: Response, req: Request) extends HttpWrapper

case class Cookie(raw: Map[String, String]) {
  override def toString: String = raw.map { case (k, v) => s"$k=$v" }.mkString(";")
}

object Cookie {
  def fromStr(str: String): Cookie = {
    val raw: Map[String, String] = str.split(';').flatMap { part =>
      part.trim.split('=') match {
        case Array(k, v) => Some(k -> v)
        case _ => None
      }
    }(breakOut)
    Cookie(raw)
  }

  def empty = Cookie(Map.empty)
}

case class HttpHelperException(mes: String) extends RuntimeException(mes)

case class Host(name: String)

case class Protocol(name: String)
