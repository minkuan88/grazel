package com.grab.grazel.migrate.dependencies

import org.junit.Test
import kotlin.test.assertEquals

class ProxyUrlRewritesTest {

    @Test
    fun `empty rewrites map returns the input url unchanged`() {
        assertEquals(
            "http://proxy.example.com:8080/private/foo.pom",
            ProxyUrlRewrites.toCanonical(
                "http://proxy.example.com:8080/private/foo.pom",
                emptyMap()
            )
        )
    }

    @Test
    fun `url with no matching prefix returns unchanged`() {
        assertEquals(
            "https://repo.maven.apache.org/maven2/foo",
            ProxyUrlRewrites.toCanonical(
                "https://repo.maven.apache.org/maven2/foo",
                mapOf("http://proxy.example.com:8080/" to "https://repo.example.com/maven/")
            )
        )
    }

    @Test
    fun `single matching prefix is substituted`() {
        assertEquals(
            "https://repo.example.com/maven/private/foo.pom",
            ProxyUrlRewrites.toCanonical(
                "http://proxy.example.com:8080/private/foo.pom",
                mapOf("http://proxy.example.com:8080/" to "https://repo.example.com/maven/")
            )
        )
    }

    @Test
    fun `longest matching prefix wins over shorter overlapping prefix`() {
        val rewrites = mapOf(
            "http://proxy.example.com:8080/" to "https://repo.example.com/maven/",
            "http://proxy.example.com:8080/private/" to "https://repo.example.com/special/"
        )
        assertEquals(
            "https://repo.example.com/special/foo.pom",
            ProxyUrlRewrites.toCanonical(
                "http://proxy.example.com:8080/private/foo.pom",
                rewrites
            )
        )
    }

    @Test
    fun `prefix match does not double-substitute already-canonical url`() {
        val rewrites = mapOf(
            "http://proxy.example.com:8080/" to "https://repo.example.com/maven/"
        )
        val canonical = "https://repo.example.com/maven/private/foo.pom"
        assertEquals(canonical, ProxyUrlRewrites.toCanonical(canonical, rewrites))
    }
}
