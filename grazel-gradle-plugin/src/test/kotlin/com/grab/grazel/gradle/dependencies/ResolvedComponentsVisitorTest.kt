package com.grab.grazel.gradle.dependencies

import com.android.build.gradle.AppExtension
import com.grab.grazel.buildProject
import com.grab.grazel.gradle.ANDROID_APPLICATION_PLUGIN
import com.grab.grazel.gradle.variant.VariantBuilder
import com.grab.grazel.gradle.variant.VariantType
import com.grab.grazel.util.addGrazelExtension
import com.grab.grazel.util.createGrazelComponent
import com.grab.grazel.util.doEvaluate
import com.grab.grazel.util.truth
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.internal.artifacts.result.DefaultResolvedDependencyResult
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.repositories
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import kotlin.test.assertTrue

class ResolvedComponentsVisitorTest {
    private lateinit var rootProject: Project
    private lateinit var androidProject: Project
    private lateinit var variantBuilder: VariantBuilder
    private lateinit var compileConfigurations: Set<Configuration>
    private lateinit var resolutionCache: DependencyResolutionService

    @get:Rule
    val temporaryFolder = TemporaryFolder()
    private lateinit var projectDir: File


    @Before
    fun setup() {
        projectDir = temporaryFolder.newFolder("project")
        rootProject = buildProject("root", projectDir = projectDir).also { root ->
            root.addGrazelExtension()
        }
        androidProject = buildProject("android", rootProject)
        with(androidProject) {
            with(plugins) {
                apply(ANDROID_APPLICATION_PLUGIN)
            }
            repositories {
                google()
                mavenCentral()
            }
            configure<AppExtension> {
                compileSdkVersion(30)
            }
            dependencies {
                add(
                    "implementation",
                    "com.google.dagger:dagger:2.37"
                )
                add(
                    "implementation",
                    "com.google.android.support:wearable:2.1.0"
                )
                add(
                    "implementation",
                    "com.google.ar.sceneform.ux:sceneform-ux:1.0.0"
                )
            }
        }

        androidProject.doEvaluate()
        val grazelComponent = rootProject.createGrazelComponent()
        variantBuilder = grazelComponent.variantBuilder().get()
        resolutionCache = grazelComponent.dependencyResolutionService().get()
        compileConfigurations = variantBuilder.build(androidProject)
            .asSequence()
            .filter { it.variantType == VariantType.AndroidBuild }
            .first()
            .compileConfiguration
    }

    @Test
    fun `assert jetifier is enabled when legacy support lib is used`() {
        val results = compileConfigurations.flatMap { configuration ->
            ResolvedComponentsVisitor(resolutionCache).visit(configuration.incoming.resolutionResult.root) { it }
        }
        assertTrue("Artifacts containing support libs are jetified") {
            results
                .asSequence()
                .filter { it.hasJetifier }
                .all {
                    val id = it.component.toString()
                    id.startsWith("com.android.support") ||
                        id.startsWith("com.google.ar") ||
                        id.startsWith("com.google.android")
                }
        }
    }

    @Test
    fun `assert resolved component visitor flattens the graph`() {
        val results = compileConfigurations.flatMap { configuration ->
            ResolvedComponentsVisitor(resolutionCache).visit(configuration.incoming.resolutionResult.root) { it }
        }
        assertTrue("Resolved component visitor flattens the transitive dependencies") {
            results.size == 18
        }
        // Tests for repo, sorting and jetifier
        results.flatMapTo(HashSet()) { it.dependencies }.truth {
            containsExactly(
                "com.android.support:support-core-ui:26.0.2:Google:true",
                "com.google.ar.sceneform:sceneform-base:1.0.0:Google:true",
                "com.android.support:support-v4:26.0.2:Google:true",
                "com.android.support:support-fragment:26.0.2:Google:true",
                "com.google.ar:core:1.2.0:Google:true",
                "javax.inject:javax.inject:1:MavenRepo:false",
                "com.google.ar.sceneform:filament-android:1.0.0:Google:true",
                "com.android.support:support-media-compat:26.0.2:Google:true",
                "com.android.support:percent:26.0.2:Google:true",
                "com.android.support:support-core-utils:26.0.2:Google:true",
                "com.google.ar.sceneform:core:1.0.0:Google:true",
                "com.google.ar.sceneform:rendering:1.0.0:Google:true",
                "com.android.support:recyclerview-v7:26.0.2:Google:true",
                "com.android.support:support-compat:26.0.2:Google:true",
                "com.android.support:support-annotations:26.0.2:Google:true"
            )
        }
    }

    @Test
    @Ignore("Caching is disabled temporarily due to it breaking up-to-date checks")
    fun `assert resolved component visitor caches resolution result`() {
        compileConfigurations.flatMap { configuration ->
            ResolvedComponentsVisitor(resolutionCache)
                .visit(configuration.incoming.resolutionResult.root) { it }
        }
        val root = compileConfigurations.first().incoming.resolutionResult.root
        assertTrue("Resolved components (non project) are cached using provided resolution cache") {
            resolutionCache.getTransitiveResult(root) == null
        }

        assertTrue("External artifacts are cached using provided resolution cache") {
            resolutionCache.getTransitiveResult(
                root.dependencies
                    .filterIsInstance<DefaultResolvedDependencyResult>()
                    .first().selected
            )?.components?.isNotEmpty() ?: false
        }
    }
}