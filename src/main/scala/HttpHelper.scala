
import skinny.http._
import scala.collection.breakOut

object HttpHelper extends HttpWrapper {
  override def cookie = Cookie.empty
}

case class Session(cookie: Cookie, res: Response, req: Request) extends HttpWrapper

case class Cookie(raw: Map[String, String]) {
  override def toString: String = raw.map { case (k, v) => s"$k=$v" }.mkString(";")
}

object Cookie {
  def fromStr(str: String): Cookie = {
    val raw: Map[String, String] = str.split(';').flatMap { part =>
      part.trim.split('=') match {
        case Array(k) => Some(k -> "")
        case Array(k, v) => Some(k -> v)
        case _ => None
      }
    }(breakOut)
    Cookie(raw)
  }

  def empty = Cookie(Map.empty)
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
