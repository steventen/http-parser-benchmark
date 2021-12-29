/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sample;

import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.util.CharsetUtil;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import rawhttp.cookies.ServerCookieHelper;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;

import java.net.HttpCookie;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MyBenchmark {
    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public String testHttpParserFromNetty() {
        String crlf = "\r\n";
        String rawReq =  "GET https://github.com/steventen HTTP/1.1" + crlf +
          "Host: github.com" + crlf +
          "Accept-Language: en-us" + crlf +
          "Accept: text/json" + crlf +
          "Accept-Encoding: gzip, deflate, br" + crlf +
          "Cookie: a=123; b=456;c=wfhhnchauhd"
          + crlf + crlf;

        EmbeddedChannel channel = new EmbeddedChannel(new HttpRequestDecoder());
        channel.writeInbound(Unpooled.copiedBuffer(rawReq, CharsetUtil.US_ASCII));
        HttpRequest req = channel.readInbound();

        // explicitly use some parsed data to avoid lazy generation
        // also return the data to avoid dead code elimination
        StringBuilder out = new StringBuilder();
        out.append(req.method());
        out.append(req.headers().get("Accept-Encoding"));
        Set<Cookie> cookies = ServerCookieDecoder.LAX.decode(req.headers().get("Cookie"));
        out.append(cookies.iterator().next().name());

        //System.out.println(out);

        return out.toString();
    }

   @Benchmark
   @OutputTimeUnit(TimeUnit.NANOSECONDS)
   @BenchmarkMode(Mode.AverageTime)
   public String testRawHttp() {
       String crlf = "\r\n";
       String rawReq =  "GET https://github.com/steventen HTTP/1.1" + crlf +
         "Host: github.com" + crlf +
         "Accept-Language: en-us" + crlf +
         "Accept: text/json" + crlf +
         "Accept-Encoding: gzip, deflate, br" + crlf +
         "Cookie: a=123; b=456;c=wfhhnchauhd"
         + crlf + crlf;
       RawHttp rawHttp = new RawHttp();
       RawHttpRequest req = rawHttp.parseRequest(rawReq);

       // explicitly use some parsed data to avoid lazy generation
       // also return the data to avoid dead code elimination
       StringBuilder out = new StringBuilder();
       out.append(req.getMethod());
       out.append(req.getHeaders().get("Accept-Encoding"));
       List<HttpCookie> cookies = ServerCookieHelper.readClientCookies(req);
       out.append(cookies.iterator().next().getName());

       //System.out.println(out);

       return out.toString();
   }
}
