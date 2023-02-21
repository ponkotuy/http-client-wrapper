# HTTP Client Wrapper

## Usage

### Startup
```sh
sbt console
```

### GET
```scala
get("myfleet.moe")
```

### GET with args
```scala
get("google.com", "q" -> "myfleet")
```

### POST `application/x-www-form-urlencoded`
```scala
post("hoge.com/session", "username" -> "ponkotuy", "password" -> "*****")
```

### POST json by using json4s
```scala
val session = post("hoge.com/session", ("username" -> "ponkotuy") ~ ("password" -> "*****"))
```

### Cookies
```scala
val session = get("...")
session.get("hoge.com/image/1") // get with cookies
```

### Parse result
```scala
session.status // => 200 use skinny.http.Response method if not exists method
session.json // get JSON Response
pretty(session.json) // get pretty JSON
prettyAll(session.json) // print all pretty JSON (return Unit)
get("myfleet.moe").res // get raw Response
```

### Helper methods
- loadJson
- withBearer
- withHeaders
- encode
- printLines
- encode
- base64
- pretty
- prettyAll
- file
- form

```scala
post("hoge.com/session", loadJson("filename.json")) // loadJson is loading JSON files
withBearer("token").get(url) // use authorization bearer header
withHeaders("key1" -> "value1", "key2" -> "value2").get(url) // use custom headers
get("google.com", "q" -> encode("日本語とか?とかをエンコードできます"))
printLines(get("google.com", q -> "myfleet").string) // print all result body
postMultipart("hoge.com/person", form("key1", "value"), form("key2", file("fuga.jpg"))) // post multipart/form-data
```

### Helper states
- host
- protocol
- ua

```scala
host = "ponkotuy.com"
get("/index.html") // use host settings
protocol = "https"
get("google.com", "q" -> "myfleet") // https protocol
ua = "Mozilla/5.0 (X11; Linux x86_64; rv:53.0) Gecko/20100101 Firefox/53.0" // set User-Agent
```

### Supported HTTP methods
- GET
- POST(post / postMultipart)
- HEAD
- PUT
- DELETE
- OPTIONS
- TRACE
