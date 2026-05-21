package com.grab.grazel.migrate.dependencies

import com.grab.grazel.gradle.Repository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class UrlRewriterTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()
    private lateinit var outputFile: File

    @Before
    fun setup() {
        outputFile = temporaryFolder.newFile("output.cfg")
    }

    @Test
    fun `assert rewriter config is only generated for private repositories with username and password`() {
        val urlRewriter = UrlRewriter()
        urlRewriter.generate(
            outputFile,
            setOf(
                Repository(
                    name = "maven",
                    url = "https://maven.com",
                    username = null,
                    password = null
                ),
                Repository(
                    name = "maven",
                    url = "https://maven.com",
                    username = null,
                    password = ""
                ),
                Repository(
                    name = "maven",
                    url = "https://maven.com",
                    username = "x",
                    password = "y"
                )
            )
        )
        outputFile.readLines().let { lines ->
            assertTrue(lines.size == 1, "Only private repos are considered")
            assert("rewrite" in lines.first())
        }
    }

    @Test
    fun `assert rewriter config rewrites url to basic auth url`() {
        val urlRewriter = UrlRewriter()
        urlRewriter.generate(
            outputFile,
            setOf(
                Repository(
                    name = "maven",
                    url = "https://maven.com",
                    username = "x",
                    password = "y"
                ),
                Repository(
                    name = "maven",
                    url = "https://maven.com",
                    username = "user",
                    password = "password"
                )
            )
        )
        val content = outputFile.readLines()
        assertEquals("rewrite maven.com/(.*) user:password@maven.com/\$1", content[0])
        assertEquals("rewrite maven.com/(.*) x:y@maven.com/\$1", content[1])
    }

    @Test
    fun `assert rewriter config throws error with malformed url`() {
        val urlRewriter = UrlRewriter()
        assertFails("Illegal character in path at index 9: malformed url") {
            urlRewriter.generate(
                outputFile,
                setOf(
                    Repository(
                        name = "maven",
                        url = "malformed url",
                        username = "x",
                        password = "y"
                    ),
                )
            )
        }
    }

    @Test
    fun `assert proxy rewrites produce canonical-LHS proxy-RHS lines`() {
        val urlRewriter = UrlRewriter(
            mapOf("http://proxy.example.com:8080/" to "https://repo.example.com/maven/")
        )
        urlRewriter.generate(
            outputFile,
            setOf(
                Repository(
                    name = "maven",
                    url = "http://proxy.example.com:8080/private",
                    username = "u",
                    password = "p"
                )
            )
        )
        assertEquals(
            "rewrite repo.example.com/maven/private/(.*) " +
                "u:p@proxy.example.com:8080/private/\$1",
            outputFile.readText()
        )
    }

    @Test
    fun `assert proxy rewriter preserves explicit port on the proxy side`() {
        val urlRewriter = UrlRewriter(
            mapOf("http://proxy.example.com:8080/" to "https://repo.example.com/")
        )
        urlRewriter.generate(
            outputFile,
            setOf(
                Repository(
                    name = "maven",
                    url = "http://proxy.example.com:8080/repo",
                    username = "u",
                    password = "p"
                )
            )
        )
        assertTrue(
            outputFile.readText().contains("@proxy.example.com:8080/"),
            "RHS must contain the explicit :8080 port; got: ${outputFile.readText()}"
        )
    }

    @Test
    fun `assert trailing slash in repo url does not produce double-slash patterns`() {
        val urlRewriter = UrlRewriter()
        urlRewriter.generate(
            outputFile,
            setOf(
                Repository(
                    name = "maven",
                    url = "https://maven.example.com/repo/",
                    username = "u",
                    password = "p"
                )
            )
        )
        val text = outputFile.readText()
        assertTrue("//" !in text, "Output should not contain double-slash; got: $text")
        assertEquals("rewrite maven.example.com/repo/(.*) u:p@maven.example.com/repo/\$1", text)
    }

    @Test
    fun `assert non-http scheme or null authority is skipped`() {
        val urlRewriter = UrlRewriter()
        urlRewriter.generate(
            outputFile,
            setOf(
                Repository(
                    name = "valid",
                    url = "https://maven.com",
                    username = "u",
                    password = "p"
                ),
                Repository(
                    name = "no-authority",
                    url = "file:/tmp/local-repo",
                    username = "u",
                    password = "p"
                ),
                Repository(
                    name = "non-http-with-authority",
                    url = "ftp://maven.example.com/repo",
                    username = "u",
                    password = "p"
                )
            )
        )
        val lines = outputFile.readLines().filter { it.isNotEmpty() }
        assertEquals(1, lines.size, "Only the valid http(s) entry should be emitted; got: $lines")
        assertEquals("rewrite maven.com/(.*) u:p@maven.com/\$1", lines[0])
    }

    @Test
    fun `assert empty rewrites map behaves identically to no-arg constructor`() {
        val a = UrlRewriter()
        val b = UrlRewriter(emptyMap())
        val outA = temporaryFolder.newFile("a.cfg")
        val outB = temporaryFolder.newFile("b.cfg")
        val repos = setOf(
            Repository(
                name = "maven",
                url = "https://maven.com",
                username = "u",
                password = "p"
            )
        )
        a.generate(outA, repos)
        b.generate(outB, repos)
        assertEquals(outA.readText(), outB.readText())
    }
}
