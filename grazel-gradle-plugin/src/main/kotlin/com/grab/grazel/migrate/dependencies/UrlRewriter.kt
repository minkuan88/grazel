/*
 * Copyright 2023 Grabtaxi Holdings PTE LTD (GRAB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.grab.grazel.migrate.dependencies

import com.grab.grazel.gradle.Repository
import java.io.File
import java.net.URI

private val HTTP_SCHEMES = setOf("http", "https")

/**
 * Generates `bazel_downloader.cfg` rewrite rules for repositories that require Basic auth.
 *
 * When [proxyRewrites] is empty (the default), each rule maps the repository's URL to itself
 * with credentials injected: `rewrite <authority><path>/(.*) <user>:<pass>@<authority><path>/$1`.
 *
 * When [proxyRewrites] is non-empty, the left-hand-side uses the canonical URL (looked up via
 * [ProxyUrlRewrites.toCanonical]) while the right-hand-side keeps the proxy URL Gradle is
 * currently configured against. This lets Bazel invocations done during `migrateToBazel`
 * route through the proxy for repositories that have credentials configured. Repositories
 * without credentials are not emitted (this rewriter only produces auth-injection rules), so
 * if your build relies on credential-less internal repos those are unaffected here.
 */
internal class UrlRewriter(
    private val proxyRewrites: Map<String, String> = emptyMap()
) {
    fun generate(
        outputFile: File,
        allRepositories: Set<Repository>
    ) {
        require(outputFile.exists()) {
            "Output file ${outputFile.absolutePath} does not exist"
        }
        outputFile.writeText(
            allRepositories
                .asSequence()
                .filter { it.username != null && it.password != null }
                .mapNotNull { repo ->
                    val proxyPrefix = URI(repo.url).toRewritePrefixOrNull() ?: return@mapNotNull null
                    val canonicalPrefix = URI(ProxyUrlRewrites.toCanonical(repo.url, proxyRewrites))
                        .toRewritePrefixOrNull() ?: return@mapNotNull null
                    "rewrite $canonicalPrefix/(.*) ${repo.username}:${repo.password}@$proxyPrefix/$1"
                }.sorted().joinToString(separator = "\n")
        )
    }

    private fun URI.toRewritePrefixOrNull(): String? {
        if (scheme?.lowercase() !in HTTP_SCHEMES) return null
        val auth = authority ?: return null
        return "$auth${(rawPath ?: "").trimEnd('/')}"
    }
}
