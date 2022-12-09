## HTTP Client Wrapper

### Usage

```sh
sbt console
```

```scala
get("myfleet.moe") // get simple
get("google.com", "q" -> "myfleet") // with args
post("hoge.com/session", "username" -> "ponkotuy", "password" -> "*****") // post form
val session = post("hoge.com/session", ("username" -> "ponkotuy") ~ ("password" -> "*****")) // post json by using json4s
session.get("hoge.com/image/1") // get with cookies
session.status // => 200 use skinny.http.Response method if not exists method
session.json // get JSON Response
pretty(session.json) // get pretty JSON
post("hoge.com/session", loadJson("filename.json")) // loadJson load JSON files
get("myfleet.moe").res // get raw Response
host = "ponkotuy.com"
get("/index.html") // use host settings
protocol = "https"
get("google.com", "q" -> "myfleet") // https protocol
ua = "Mozilla/5.0 (X11; Linux x86_64; rv:53.0) Gecko/20100101 Firefox/53.0" // set User-Agent
withBearer("token").get(url) // use authorization bearer header
withHeaders("key1" -> "value1", "key2" -> "value2").get(url) // use custom headers
get("google.com", "q" -> encode("日本語とか?とかをエンコードできます"))
printLines(get("google.com", q -> "myfleet").string) // print all result body
```

You can use get, post, head, put, delete, options and trace.
