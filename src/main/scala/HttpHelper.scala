
import java.nio.charset.StandardCharsets

import org.json4s._
import org.json4s.native.JsonMethods._
import skinny.http._
import scala.collection.breakOut

object HttpHelper extends HttpWrapper {
  override def cookie = Cookie.empty
}

case class Session(cookie: Cookie, res: Response) extends HttpWrapper

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

trait HttpWrapper {
  import HttpWrapper._

  def cookie: Cookie

  def get(url: String, params: (String, String)*): Session = {
    val req = Request(fixURL(url)).queryParams(params:_*).header("Cookie", cookie.toString)
    request(Method.GET, req)
  }

  def head(url: String, params: (String, String)*): Session = {
    val req = Request(fixURL(url)).queryParams(params:_*).header("Cookie", cookie.toString)
    request(Method.HEAD, req)
  }

  def post(url: String, params: (String, String)*): Session = {
    val req = Request(fixURL(url)).queryParams(params:_*).header("Cookie", cookie.toString)
    request(Method.POST, req)
  }

  def post(url: String, body: String): Session = {
    val req = Request(fixURL(url)).body(body.getBytes(StandardCharsets.UTF_8))
    request(Method.POST, req)
  }

  def post(url: String, json: JValue): Session = {
    val req = Request(fixURL(url)).body(toBytes(json), "application/json; charset=utf-8")
    request(Method.POST, req)
  }

  def put(url: String, params: (String, String)*): Session = {
    val req = Request(fixURL(url)).queryParams(params:_*).header("Cookie", cookie.toString)
    request(Method.PUT, req)
  }

  def put(url: String, body: String): Session = {
    val req = Request(fixURL(url)).body(body.getBytes(StandardCharsets.UTF_8))
    request(Method.PUT, req)
  }

  def put(url: String, json: JValue): Session = {
    val req = Request(fixURL(url)).body(toBytes(json), "application/json; charset=utf-8")
    request(Method.PUT, req)
  }

  def delete(url: String, params: (String, String)*): Session = {
    val req = Request(fixURL(url)).queryParams(params:_*).header("Cookie", cookie.toString)
    request(Method.DELETE, req)
  }

  def options(url: String, params: (String, String)*): Session = {
    val req = Request(fixURL(url)).queryParams(params:_*).header("Cookie", cookie.toString)
    request(Method.OPTIONS, req)
  }

  def trace(url: String, params: (String, String)*): Session = {
    val req = Request(fixURL(url)).queryParams(params:_*).header("Cookie", cookie.toString)
    request(Method.TRACE, req)
  }

  def request(method: Method, req: Request): Session = {
    def f(req: Request): Response = {
      val res = HTTP.request(method, req)
      if(res.status / 100 == 3) {
        res.header("Location").map { loc =>
          f(req.copy(url = loc))
        }.getOrElse(throw new HttpHelperException("Fail redirect"))
      } else res
    }
    val res = f(req)
    println(req.contentType)
    printSummary(res)
    val cookie = res.header("Set-Cookie").map(Cookie.fromStr).getOrElse(Cookie.empty)
    Session(cookie, res)
  }
}

object HttpWrapper {
  private def fixURL(url: String) = if(url.contains("//")) url else "http://" + url

  private def printSummary(res: Response): Unit = {
    println(res.header(null).getOrElse(""))
    if(res.asString.length > 100) println(res.asString.take(100) + "...") else println(res.asString)
    println()
  }

  private def toBytes(json: JValue): Array[Byte] = pretty(render(json)).getBytes(StandardCharsets.UTF_8)
}
