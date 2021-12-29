package main

import (
	"testing"
)

func BenchmarkParseRequest(b *testing.B) {
	for i := 0; i < b.N; i++ {
		ParseHttpRequest()
	}
}
