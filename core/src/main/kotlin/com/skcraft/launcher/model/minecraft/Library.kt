// Generated by delombok at Sat Jul 14 04:26:21 CEST 2018
/*
 * SK's Minecraft Launcher
 * Copyright (C) 2010-2014 Albert Pham <http://www.sk89q.com> and contributors
 * Please see LICENSE.txt for license information.
 */
package com.skcraft.launcher.model.minecraft

import com.skcraft.launcher.util.Environment
import com.skcraft.launcher.util.Platform
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.HashMapSerializer
import kotlinx.serialization.serializer
import kotlinx.serialization.list
import java.util.regex.Pattern
import kotlinx.serialization.Transient

@Serializable
data class Library(
    val name: String
) {
    @Serializer(forClass = Library::class)
    companion object : KSerializer<Library> {
        override fun serialize(output: Encoder, obj: Library) {
            val elemOutput = output.beginStructure(descriptor)
            elemOutput.encodeStringElement(descriptor, 0, obj.name)
            obj.baseUrl?.let { url ->
                elemOutput.encodeStringElement(descriptor, 1, url)
            }
            obj.natives?.let { natives ->
                elemOutput.encodeSerializableElement(descriptor, 2, HashMapSerializer(String.serializer(), String.serializer()), natives)
            }
            obj.extract?.let { extract ->
                elemOutput.encodeSerializableElement(descriptor, 3, Extract::class.serializer(), extract)
            }
            obj.rules?.filter {
                    it.action != null || it.os != null
                }?.let { rules ->
                    elemOutput.encodeSerializableElement(descriptor, 4, Rule::class.serializer().list, rules)
            }
            elemOutput.endStructure(descriptor)
        }

        fun String.split() =
            this.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    }

    @kotlin.jvm.Transient
    @Transient
    var group: String = name.split()[0]
    @kotlin.jvm.Transient
    @Transient
    var artifact: String? = name.split()[1]
    @kotlin.jvm.Transient
    @Transient
    var version: String? = name.split()[2]
    @SerialName("url")
    @Optional
    var baseUrl: String? = null
    @Optional
    var natives: Map<String, String>? = null
    @Optional
    var extract: Extract? = null
    @Optional
    var rules: List<Rule>? = null
    // Forge-added
    @Optional
    @Transient
    var comment: String? = null
    // Custom
    @Optional
    @Transient
    var isLocallyAvailable: Boolean = false

    fun matches(environment: Environment): Boolean {
        var allow = false
        if (rules != null) {
            for (rule in rules!!) {
                if (rule.matches(environment)) {
                    allow = rule.action == Action.allow
                }
            }
        } else {
            allow = true
        }
        return allow
    }

    fun getNativeString(platform: Platform): String? {
        return if (natives != null) {
            when (platform) {
                Platform.LINUX -> natives!!["linux"]

                Platform.WINDOWS -> natives!!["windows"]

                Platform.MAC_OS_X -> natives!!["osx"]

                else -> null
            }
        } else {
            null
        }
    }

    fun getFilename(environment: Environment): String {
        val nativeString = getNativeString(environment.platform)
        return if (nativeString != null) {
            String.format("%s-%s-%s.jar", artifact, version, nativeString)
        } else String.format("%s-%s.jar", artifact, version)
    }

    fun getPath(environment: Environment): String = buildString {
        append(group.replace('.', '/'))
        append("/")
        append(artifact)
        append("/")
        append(version)
        append("/")
        append(getFilename(environment))
    }.replace("\${arch}", environment.archBits)

    @Serializable
    data class Rule(
        @Optional
        var action: Action? = null,
        @Optional
        var os: OS? = null
    ) {

        fun matches(environment: Environment): Boolean {
            return if (os == null) {
                true
            } else {
                os!!.matches(environment)
            }
        }
    }

    @Serializable
    data class OS(
        @Optional var name: String? = null,
        @Optional var platform: Platform? = null,
        @Optional var version: Pattern? = null
    ) {
        fun matches(environment: Environment): Boolean {
            return (platform == null || platform == environment.platform) && (version == null || version!!.matcher(
                environment.platformVersion
            ).matches())
        }

        @Serializer(forClass = OS::class)
        companion object : KSerializer<OS> {
            override fun serialize(output: Encoder, obj: OS) {
                val elemOutput = output.beginStructure(descriptor)
                obj.name?.let {
                    elemOutput.encodeStringElement(descriptor, 0, it)
                }
                obj.platform?.let { platform ->
                    elemOutput.encodeSerializableElement(descriptor, 1, PlatformSerializer, platform)
                }
                obj.version?.let { version ->
                    elemOutput.encodeSerializableElement(descriptor, 2, Pattern::class.serializer(), version)
                }
                elemOutput.endStructure(descriptor)
            }
        }
    }

    @Serializable
    data class Extract(
        var exclude: List<String>
    )

    enum class Action {
        allow, disallow;
    }
}
