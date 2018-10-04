// Generated by delombok at Sat Jul 14 01:46:55 CEST 2018
/*
 * SK's Minecraft Launcher
 * Copyright (C) 2010-2014 Albert Pham <http://www.sk89q.com> and contributors
 * Please see LICENSE.txt for license information.
 */
package com.skcraft.launcher.builder

import com.skcraft.launcher.model.modpack.Feature
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeaturePattern(
    @SerialName("feature")
    @Serializable(with = Feature.Companion::class) var feature: Feature,
    @SerialName("files")
    var filePatterns: FnPatternList
) {
    fun matches(path: String): Boolean {
        return filePatterns.matches(path)
    }
}
