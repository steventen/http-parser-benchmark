# Http Request Message Parsing Benchmark 

Benchmark the performance on parsing a simple Http request message.

Each parser takes a simple http message as input, and creates an object or struct accordingly,
each testing function returns a string containing the http method, one header value and one cookie name. This is to prevent the code from handling the parsing lazily and also to avoid dead code elimination.


## Run Benchmark on Java Library

[Java Microbenchmark Harness (JMH)](https://github.com/openjdk/jmh) is used. 
```
cd java/

mvn clean verify

java -jar target/benchmarks.jar
```

### Results

Computer: Mac mini (2012)
CPU: 2.3 GHz Quad-Core Intel Core i7
Memory: 16 GB 1600 MHz DDR3

### Netty

`HttpRequestDecoder` from `Netty` `v4.1.72.Final` is used to parse the request message

```
# JMH version: 1.33
# VM version: JDK 11.0.7, OpenJDK 64-Bit Server VM, 11.0.7+10
# VM invoker: /Library/Java/JavaVirtualMachines/adoptopenjdk-11.jdk/Contents/Home/bin/java
# VM options: <none>
# Blackhole mode: full + dont-inline hint (default, use -Djmh.blackhole.autoDetect=true to auto-detect)
# Warmup: 5 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: org.sample.MyBenchmark.testHttpParserFromNetty

... ...

Result "org.sample.MyBenchmark.testHttpParserFromNetty":
  5637.774 ±(99.9%) 195.638 ns/op [Average]
  (min, avg, max) = (5299.449, 5637.774, 5997.024), stdev = 261.172
  CI (99.9%): [5442.136, 5833.413] (assumes normal distribution)
```

### Rawhttp

[Rawhttp](https://github.com/renatoathaydes/rawhttp) `v2.4.1` is used for this benchmark

```
# JMH version: 1.33
# VM version: JDK 11.0.7, OpenJDK 64-Bit Server VM, 11.0.7+10
# VM invoker: /Library/Java/JavaVirtualMachines/adoptopenjdk-11.jdk/Contents/Home/bin/java
# VM options: <none>
# Blackhole mode: full + dont-inline hint (default, use -Djmh.blackhole.autoDetect=true to auto-detect)
# Warmup: 5 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: org.sample.MyBenchmark.testRawHttp

... ...

Result "org.sample.MyBenchmark.testRawHttp":
  7109.697 ±(99.9%) 218.194 ns/op [Average]
  (min, avg, max) = (6636.232, 7109.697, 7606.377), stdev = 291.283
  CI (99.9%): [6891.503, 7327.892] (assumes normal distribution)
```

Compare both together, we have
```
Benchmark                            Mode  Cnt     Score     Error  Units
MyBenchmark.testHttpParserFromNetty  avgt   25  5637.774 ± 195.638  ns/op
MyBenchmark.testRawHttp              avgt   25  7109.697 ± 218.194  ns/op
```


## Run Benchmark on Go Library

The build-in benchmarking facility from testing package is used for Go.

```
goos: darwin
goarch: amd64
pkg: example/hello
cpu: Intel(R) Core(TM) i7-3615QM CPU @ 2.30GHz
BenchmarkParseRequest-8   	  233466	      4753 ns/op
PASS
ok  	example/hello	1.330s
```