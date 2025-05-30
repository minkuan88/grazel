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

package com.grab.grazel.fake

import groovy.lang.Closure
import org.gradle.api.Action
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationPublications
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.DependencyConstraintSet
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.artifacts.ExcludeRule
import org.gradle.api.artifacts.PublishArtifactSet
import org.gradle.api.artifacts.ResolutionStrategy
import org.gradle.api.artifacts.ResolvableDependencies
import org.gradle.api.artifacts.ResolvedConfiguration
import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.file.FileTree
import org.gradle.api.provider.Provider
import org.gradle.api.specs.Spec
import org.gradle.api.tasks.TaskDependency
import java.io.File

class FakeConfiguration(private val name: String = "") : Configuration {

    override fun iterator(): MutableIterator<File> {
        TODO("Not yet implemented")
    }

    override fun addToAntBuilder(builder: Any, nodeName: String, type: FileCollection.AntType) {
        TODO("Not yet implemented")
    }

    override fun addToAntBuilder(builder: Any, nodeName: String): Any {
        TODO("Not yet implemented")
    }

    override fun getBuildDependencies(): TaskDependency {
        TODO("Not yet implemented")
    }

    override fun getSingleFile(): File {
        TODO("Not yet implemented")
    }

    override fun getFiles(): MutableSet<File> {
        TODO("Not yet implemented")
    }

    override fun contains(file: File): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAsPath(): String {
        TODO("Not yet implemented")
    }

    override fun plus(collection: FileCollection): FileCollection {
        TODO("Not yet implemented")
    }

    override fun minus(collection: FileCollection): FileCollection {
        TODO("Not yet implemented")
    }

    override fun filter(filterClosure: Closure<*>): FileCollection {
        TODO("Not yet implemented")
    }

    override fun filter(filterSpec: Spec<in File>): FileCollection {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAsFileTree(): FileTree {
        TODO("Not yet implemented")
    }

    override fun getElements(): Provider<MutableSet<FileSystemLocation>> {
        TODO("Not yet implemented")
    }

    override fun getAttributes(): AttributeContainer {
        TODO("Not yet implemented")
    }

    override fun attributes(action: Action<in AttributeContainer>): Configuration {
        TODO("Not yet implemented")
    }

    override fun getResolutionStrategy(): ResolutionStrategy {
        TODO("Not yet implemented")
    }

    override fun resolutionStrategy(closure: Closure<*>): Configuration {
        TODO("Not yet implemented")
    }

    override fun resolutionStrategy(action: Action<in ResolutionStrategy>): Configuration {
        TODO("Not yet implemented")
    }

    override fun getState(): Configuration.State {
        TODO("Not yet implemented")
    }

    override fun getName(): String = name

    override fun isVisible(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setVisible(visible: Boolean): Configuration {
        TODO("Not yet implemented")
    }

    override fun getExtendsFrom(): MutableSet<Configuration> {
        TODO("Not yet implemented")
    }

    override fun setExtendsFrom(superConfigs: MutableIterable<Configuration>): Configuration {
        TODO("Not yet implemented")
    }

    override fun extendsFrom(vararg superConfigs: Configuration?): Configuration {
        TODO("Not yet implemented")
    }

    override fun isTransitive(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setTransitive(t: Boolean): Configuration {
        TODO("Not yet implemented")
    }

    override fun getDescription(): String? {
        TODO("Not yet implemented")
    }

    override fun setDescription(description: String?): Configuration {
        TODO("Not yet implemented")
    }

    override fun getHierarchy(): MutableSet<Configuration> {
        TODO("Not yet implemented")
    }

    override fun resolve(): MutableSet<File> {
        TODO("Not yet implemented")
    }

    override fun files(dependencySpecClosure: Closure<*>): MutableSet<File> {
        TODO("Not yet implemented")
    }

    override fun files(dependencySpec: Spec<in Dependency>): MutableSet<File> {
        TODO("Not yet implemented")
    }

    override fun files(vararg dependencies: Dependency?): MutableSet<File> {
        TODO("Not yet implemented")
    }

    override fun fileCollection(dependencySpec: Spec<in Dependency>): FileCollection {
        TODO("Not yet implemented")
    }

    override fun fileCollection(dependencySpecClosure: Closure<*>): FileCollection {
        TODO("Not yet implemented")
    }

    override fun fileCollection(vararg dependencies: Dependency?): FileCollection {
        TODO("Not yet implemented")
    }

    override fun getResolvedConfiguration(): ResolvedConfiguration {
        TODO("Not yet implemented")
    }

    override fun getUploadTaskName(): String {
        TODO("Not yet implemented")
    }

    override fun getTaskDependencyFromProjectDependency(
        useDependedOn: Boolean,
        taskName: String
    ): TaskDependency {
        TODO("Not yet implemented")
    }

    override fun getDependencies(): DependencySet {
        TODO("Not yet implemented")
    }

    override fun getAllDependencies(): DependencySet {
        TODO("Not yet implemented")
    }

    override fun getDependencyConstraints(): DependencyConstraintSet {
        TODO("Not yet implemented")
    }

    override fun getAllDependencyConstraints(): DependencyConstraintSet {
        TODO("Not yet implemented")
    }

    override fun getArtifacts(): PublishArtifactSet {
        TODO("Not yet implemented")
    }

    override fun getAllArtifacts(): PublishArtifactSet {
        TODO("Not yet implemented")
    }

    override fun getExcludeRules(): MutableSet<ExcludeRule> {
        TODO("Not yet implemented")
    }

    override fun exclude(excludeProperties: MutableMap<String, String>): Configuration {
        TODO("Not yet implemented")
    }

    override fun defaultDependencies(action: Action<in DependencySet>): Configuration {
        TODO("Not yet implemented")
    }

    override fun withDependencies(action: Action<in DependencySet>): Configuration {
        TODO("Not yet implemented")
    }

    override fun getAll(): MutableSet<Configuration> {
        TODO("Not yet implemented")
    }

    override fun getIncoming(): ResolvableDependencies {
        TODO("Not yet implemented")
    }

    override fun getOutgoing(): ConfigurationPublications {
        TODO("Not yet implemented")
    }

    override fun outgoing(action: Action<in ConfigurationPublications>) {
        TODO("Not yet implemented")
    }

    override fun copy(): Configuration {
        TODO("Not yet implemented")
    }

    override fun copy(dependencySpec: Spec<in Dependency>): Configuration {
        TODO("Not yet implemented")
    }

    override fun copy(dependencySpec: Closure<*>): Configuration {
        TODO("Not yet implemented")
    }

    override fun copyRecursive(): Configuration {
        TODO("Not yet implemented")
    }

    override fun copyRecursive(dependencySpec: Spec<in Dependency>): Configuration {
        TODO("Not yet implemented")
    }

    override fun copyRecursive(dependencySpec: Closure<*>): Configuration {
        TODO("Not yet implemented")
    }

    override fun setCanBeConsumed(allowed: Boolean) {
        TODO("Not yet implemented")
    }

    override fun isCanBeConsumed(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setCanBeResolved(allowed: Boolean) {
        TODO("Not yet implemented")
    }

    override fun isCanBeResolved(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setCanBeDeclared(allowed: Boolean) {
        TODO("Not yet implemented")
    }

    override fun isCanBeDeclared(): Boolean {
        TODO("Not yet implemented")
    }

    override fun shouldResolveConsistentlyWith(versionsSource: Configuration): Configuration {
        TODO("Not yet implemented")
    }

    override fun disableConsistentResolution(): Configuration {
        TODO("Not yet implemented")
    }
}