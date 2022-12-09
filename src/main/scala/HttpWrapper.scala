
import org.json4s._
import org.json4s.native.JsonMethods._
import skinny.http._

import java.nio.charset.StandardCharsets

trait HttpWrapper {
  import HttpWrapper._

  def headers: Map[String, String]
  def addHeader(key: String, value: String): HttpWrapper

  def get(url: String, params: (String, String)*)(implicit host: Host, protocol: Protocol, ua: UserAgent): Session = {
    val req = Request(fixURL(url)).queryParams(params:_*)
    request(Method.GET, req)
  }

  def head(url: String, params: (String, String)*)(implicit host: Host, protocol: Protocol, ua: UserAgent): Session = {
    val req = Request(fixURL(url)).queryParams(params:_*)
    request(Method.HEAD, req)
  }

  def post(url: String, params: (String, String)*)(implicit host: Host, protocol: Protocol, ua: UserAgent): Session = {
    val req = Request(fixURL(url)).formParams(params:_*)
    request(Method.POST, req)
  }

  def post(url: String, body: String)(implicit host: Host, protocol: Protocol, ua: UserAgent): Session = {
    val req = Request(fixURL(url)).body(body.getBytes(StandardCharsets.UTF_8))
    request(Method.POST, req)
  }

  def post(url: String, json: JValue)(implicit host: Host, protocol: Protocol, ua: UserAgent): Session = {
    val req = Request(fixURL(url)).body(toBytes(json), "application/json; charset=utf-8")
    request(Method.POST, req)
  }

  def put(url: String, params: (String, String)*)(implicit host: Host, protocol: Protocol, ua: UserAgent): Session = {
    val req = Request(fixURL(url)).formParams(params:_*)
    request(Method.PUT, req)
  }

  def put(url: String, body: String)(implicit host: Host, protocol: Protocol, ua: UserAgent): Session = {
    val req = Request(fixURL(url)).body(body.getBytes(StandardCharsets.UTF_8))
    request(Method.PUT, req)
  }

  def put(url: String, json: JValue)(implicit host: Host, protocol: Protocol, ua: UserAgent): Session = {
    val req = Request(fixURL(url)).body(toBytes(json), "application/json; charset=utf-8")
    request(Method.PUT, req)
  }

  def delete(url: String, params: (String, String)*)(implicit host: Host, protocol: Protocol, ua: UserAgent): Session = {
    val req = Request(fixURL(url)).queryParams(params:_*)
    request(Method.DELETE, req)
  }

  def options(url: String, params: (String, String)*)(implicit host: Host, protocol: Protocol, ua: UserAgent): Session = {
    val req = Request(fixURL(url)).queryParams(params:_*)
    request(Method.OPTIONS, req)
  }

  def trace(url: String, params: (String, String)*)(implicit host: Host, protocol: Protocol, ua: UserAgent): Session = {
    val req = Request(fixURL(url)).queryParams(params:_*)
    request(Method.TRACE, req)
  }

  def request(method: Method, req: Request)(implicit ua: UserAgent): Session = {
    println(s"${method.name} ${req.url}")
    req.headers ++= headers
    req.userAgent = Some(ua.name)
    req.readTimeoutMillis = 30000
    def f(req: Request): Response = {
      val res = HTTP.request(method, req)
      if(res.status / 100 == 3) {
        res.header("Location").map { loc =>
          f(req.copy(url = loc))
        }.getOrElse(throw HttpHelperException("Fail redirect"))
      } else res
    }
    val res = f(req)
    println(req.contentType)
    printSummary(res)
    val cookie = res.header("Set-Cookie").map(Cookie.fromStr).getOrElse(Cookie.empty)
    val newHeaders = headers.updated("Cookie", cookie.toString)
    Session(res, req, newHeaders)
  }
}

object HttpWrapper {
  private def fixURL(url: String)(implicit host: Host, protocol: Protocol) = {
    if(url.contains("//")) url
    else {
      protocol.name + "://" + (if(url.startsWith("/")) host.name else "") + url
    }
  }

  private def printSummary(res: Response): Unit = {
    println(res.header(null).getOrElse(""))
    if(res.asString.length > 100) println(res.asString.take(100) + "...") else println(res.asString)
    println()
  }

  private def toBytes(json: JValue): Array[Byte] = pretty(render(json)).getBytes(StandardCharsets.UTF_8)
}
