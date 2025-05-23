/*
 * Copyright 2022 Grabtaxi Holdings PTE LTD (GRAB)
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

package com.grab.grazel.migrate.kotlin

import com.grab.grazel.bazel.TestSize
import com.grab.grazel.bazel.starlark.BazelDependency
import com.grab.grazel.migrate.BazelBuildTarget
import com.grab.grazel.migrate.android.AndroidUnitTestTarget

data class UnitTestData(
    val name: String,
    val srcs: List<String>,
    val additionalSrcSets: List<String>,
    val deps: List<BazelDependency>,
    val tags: List<String>,
    val associates: List<BazelDependency>,
    val testSize: TestSize = TestSize.MEDIUM,
    val hasAndroidJarDep: Boolean = false,
)

internal fun UnitTestData.toUnitTestTarget(): BazelBuildTarget =
    if (hasAndroidJarDep) {
        AndroidUnitTestTarget(
            name = name,
            srcs = srcs,
            deps = deps,
            tags = tags,
            associates = associates,
            customPackage = "",
            additionalSrcSets = additionalSrcSets,
            compose = false,
            testSize = testSize,
        )
    } else {
        UnitTestTarget(
            name = name,
            srcs = srcs,
            additionalSrcSets = additionalSrcSets,
            deps = deps,
            associates = associates,
            testSize = testSize,
            tags = tags,
        )
    }
