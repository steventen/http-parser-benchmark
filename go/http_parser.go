package main

import (
	"bufio"
	. "net/http"
	"strings"
)

func ParseHttpRequest() string {
	crlf := "\r\n"
	raw := "GET https://github.com/steventen HTTP/1.1" + crlf +
		"Host: github.com" + crlf +
		"Accept-Language: en-us" + crlf +
		"Accept: text/json" + crlf +
		"Accept-Encoding: gzip, deflate, br" + crlf +
		"Cookie: a=123; b=456;c=wfhhnchauhd" + crlf + crlf

	req, _ := ReadRequest(bufio.NewReader(strings.NewReader(raw)))

	var sb strings.Builder
	sb.WriteString(req.Method)
	sb.WriteString(req.Header.Get("Accept-Encoding"))
	sb.WriteString(req.Cookies()[0].Name)

	// fmt.Println("Http Method: " + req.Method)
	// fmt.Println("Accept-Encoding: " + req.Header.Get("Accept-Encoding"))
	// fmt.Println("A Cookie: " + req.Cookies()[0].Name)
	// fmt.Println(sb.String())

	// return calculated values to avoid the "evaluated but not used" error
	return sb.String()
}
