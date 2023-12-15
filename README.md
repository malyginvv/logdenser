# LogDenser [![CircleCI](https://dl.circleci.com/status-badge/img/gh/malyginvv/logdenser/tree/main.svg?style=shield)](https://dl.circleci.com/status-badge/redirect/gh/malyginvv/logdenser/tree/main) [![codecov](https://codecov.io/gh/malyginvv/logdenser/graph/badge.svg?token=JRST9RSKKX)](https://codecov.io/gh/malyginvv/logdenser) [![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/malyginvv/logdenser/main/LICENSE)

Log condenser - a small Java library that groups strings based on similarity.

## What it does

A typical application log contains a lot of similar entries: messages that were produced by the same logging statement but with different parameters.
Let's look at this output:
```
okhttp3.mockwebserver.MockWebServer$3 execute
MockWebServer[51213] starting to accept connections
okhttp3.mockwebserver.MockWebServer$4 processOneRequest
MockWebServer[51213] received request: HEAD /resource HTTP/1.1 and responded: HTTP/1.1 200 OK
okhttp3.mockwebserver.MockWebServer$3 acceptConnections
MockWebServer[51213] done accepting connections: Socket closed
okhttp3.mockwebserver.MockWebServer$3 execute
MockWebServer[51215] starting to accept connections
okhttp3.mockwebserver.MockWebServer$4 processOneRequest
MockWebServer[51215] received request: HEAD /missing HTTP/1.1 and responded: HTTP/1.1 404 Client Error
okhttp3.mockwebserver.MockWebServer$3 acceptConnections
MockWebServer[51215] done accepting connections: Socket closed
okhttp3.mockwebserver.MockWebServer$3 execute
MockWebServer[51217] starting to accept connections
okhttp3.mockwebserver.MockWebServer$4 processOneRequest
MockWebServer[51217] received request: GET /resource HTTP/1.1 and responded: HTTP/1.1 200 OK
okhttp3.mockwebserver.MockWebServer$3 acceptConnections
MockWebServer[51217] done accepting connections: Socket closed
okhttp3.mockwebserver.MockWebServer$3 execute
MockWebServer[51219] starting to accept connections
okhttp3.mockwebserver.MockWebServer$4 processOneRequest
MockWebServer[51219] received request: GET /resource HTTP/1.1 and responded: HTTP/1.1 200 OK
okhttp3.mockwebserver.MockWebServer$3 acceptConnections
MockWebServer[51219] done accepting connections: Socket closed
okhttp3.mockwebserver.MockWebServer$3 execute
MockWebServer[51221] starting to accept connections
okhttp3.mockwebserver.MockWebServer$4 processOneRequest
MockWebServer[51221] received request: HEAD /resource HTTP/1.1 and responded: HTTP/1.1 200 OK
okhttp3.mockwebserver.MockWebServer$4 processOneRequest
MockWebServer[51221] received request: HEAD /resource HTTP/1.1 and responded: HTTP/1.1 200 OK
okhttp3.mockwebserver.MockWebServer$3 acceptConnections
MockWebServer[51221] done accepting connections: Socket closed
```
Log condenser packs these messages to a list of views or combinations.
```
okhttp3.mockwebserver.MockWebServer$3 <execute|acceptConnections> {10}
okhttp3.mockwebserver.MockWebServer$4 processOneRequest {6}
MockWebServer <[51213]|[51215]|[51217]|[51219]|[51221]> starting to accept connections {5}
MockWebServer <[51213]|[51215]|[51217]|[51219]|[51221]> done accepting connections: Socket closed {5}
MockWebServer <[51213]|[51217]|[51219]|[51221]> received request: <HEAD|GET> /resource HTTP/1.1 and responded: HTTP/1.1 200 OK {5}
MockWebServer [51215] received request: HEAD /missing HTTP/1.1 and responded: HTTP/1.1 404 Client Error {1}
```
Here `<a|b|c>` represents multiple options `a`, `b`, and `c` that are present in the original log. 
And `{N}` in the end means how many times we've seen that pattern in the log.
E.g. `okhttp3.mockwebserver.MockWebServer$3 <execute|acceptConnections>` means there were 10 strings like 
`okhttp3.mockwebserver.MockWebServer$3 execute` and `okhttp3.mockwebserver.MockWebServer$3 acceptConnections`.
But we don't know how many `... execute` or `... acceptConnections` were there.

The fifth element from the list above could be visualized as:
```
               ┌─ [51213] ─┐
               ├─ [51217] ─┤                     ┌─ HEAD ─┐
MockWebServer ─┼─ [51217] ─┼─ received request: ─┴─ GET ──┴─ /resource HTTP/1.1 and responded: HTTP/1.1 200 OK
               └─ [51219] ─┘
```
Again we can easily see all the possible string combinations, but we don't know the exact combinations.
This is an intentional limitation that simplifies processing and makes results easier to analyze.

## Logging
This library uses [System.Logger API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/System.Logger.html) for logging. 
It should be compatible with every major logging framework. Doesn't add any dependencies and defaults to [JUL](https://docs.oracle.com/en/java/javase/17/docs/api/java.logging/java/util/logging/package-summary.html).