# LogDenser [![CircleCI](https://dl.circleci.com/status-badge/img/gh/malyginvv/logdenser/tree/main.svg?style=shield)](https://dl.circleci.com/status-badge/redirect/gh/malyginvv/logdenser/tree/main) [![codecov](https://codecov.io/gh/malyginvv/logdenser/graph/badge.svg?token=JRST9RSKKX)](https://codecov.io/gh/malyginvv/logdenser) [![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/malyginvv/logdenser/main/LICENSE)

Log condenser — a small Java library that groups strings based on similarity.

## What it does

A typical application log contains plenty of similar entries: messages that were produced by the same logging statement but with different parameters.
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
Again, we can easily see all the possible string combinations, but we don't know the exact combinations.
This is an intentional limitation that simplifies processing and makes results easier to analyze.

## How to use
Add [pkg:maven/pro.malygin.logdenser/logdenser-core@0.2.1](https://central.sonatype.com/artifact/pro.malygin.logdenser/logdenser-core/overview) to your project using your favorite build tool.

### Quick start
The fastest and easiest approach to condense some strings is to create an instance of [DefaultCondenser](https://github.com/malyginvv/logdenser/blob/main/core/src/main/java/pro/malygin/logdenser/condenser/DefaultCondenser.java) like that:
```java
var condenser = new DefaultCondenser((left, right, editDistance) -> editDistance.distance() < 2);
```
and call its `condense` method passing a collection of strings you need to group.
```java
var results = condenser.condense(myLogs);
```

### Fine tuning
#### Condensing Matcher
The [Condensing Matcher](https://github.com/malyginvv/logdenser/blob/main/core/src/main/java/pro/malygin/logdenser/condenser/CondensingMatcher.java) defines a heuristic that determines whether two strings are considered similar and 
therefore must be included in the same group.

The simplest implementation just compares edit distance to a constant, like in the example from the Quick start.
```java
(left, right, editDistance) -> editDistance.distance() < 2
```
A more sophisticated matcher might compare edit distance to a number derived from token string length and 
consider checking properties of token strings themselves.
```java
(left, right, editDistance) -> {
    int size = left.size();
    int maxDistance;
    if (size < 5) maxDistance = 2;
    else if (size < 10) maxDistance = 3;
    else maxDistance = 4;
    return left.first().equals(right.first()) && editDistance.distance() < maxDistance;
}
```

#### DefaultCondenser pipeline
DefaultCondenser does a pretty simple job:
1. Filters input string using the supplied `Predicate<String> filter`. By default, it accepts any string from input.
2. Submits every filtered string to a Line Processor. 
The Line Processor transforms the string and tokenizes it. 
A transformation might leave the first N lines of the string and discard anything else. That could help eliminate the stack trace. 
The default [Line Processor](https://github.com/malyginvv/logdenser/blob/main/core/src/main/java/pro/malygin/logdenser/processor/LineProcessor.java) doesn't transform the string and uses the [SingleLevelTokenizer](https://github.com/malyginvv/logdenser/blob/main/core/src/main/java/pro/malygin/logdenser/tokenizer/SingleLevelTokenizer.java)
3. Filters the resulting token strings, discarding all empty ones.
4. Groups token strings by their length.
5. Applies a [Token Condenser](https://github.com/malyginvv/logdenser/blob/main/core/src/main/java/pro/malygin/logdenser/condenser/TokenCondenser.java) to each group of the same length. 
The [default Token Condenser](https://github.com/malyginvv/logdenser/blob/main/core/src/main/java/pro/malygin/logdenser/condenser/SameLengthTokenCondenser.java) uses the [Hamming Distance calculator](https://github.com/malyginvv/logdenser/blob/main/core/src/main/java/pro/malygin/logdenser/distance/HammingDistanceCalculator.java) and a Condensing Matcher supplied by the user.
6. Returns the list of results. 

#### Using DefaultCondenser
DefaultCondenser has a couple of other constructors. You can specify input string filter, transformer, and matcher.
Or even use a custom-built line processor and token condenser. 

## Logging
This library uses [System.Logger API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/System.Logger.html) for logging. 
It should be compatible with every major logging framework. Doesn't add any dependencies and defaults to [JUL](https://docs.oracle.com/en/java/javase/17/docs/api/java.logging/java/util/logging/package-summary.html).