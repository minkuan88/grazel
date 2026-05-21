/*
 * Copyright 2026 Grabtaxi Holdings PTE LTD (GRAB)
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

/**
 * Maps proxy URLs back to their canonical form using a longest-prefix match. Used to keep
 * generated Bazel artifacts (`maven_install.json`, `bazel_downloader.cfg`) portable across
 * environments when Gradle resolution happens through a CI-only URL-rewriting proxy.
 *
 * Each map entry is `proxyUrlPrefix -> canonicalUrlPrefix`. When a URL starts with the longest
 * matching key, the matched prefix is replaced with the corresponding value. Otherwise the URL
 * is returned unchanged.
 */
internal object ProxyUrlRewrites {
    fun toCanonical(url: String, rewrites: Map<String, String>): String {
        if (rewrites.isEmpty()) return url
        val match = rewrites.entries
            .filter { (proxyPrefix, _) -> url.startsWith(proxyPrefix) }
            .maxByOrNull { it.key.length } ?: return url
        return match.value + url.removePrefix(match.key)
    }
}
