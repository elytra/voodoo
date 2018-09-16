// Generated by delombok at Sat Jul 14 01:46:55 CEST 2018
/*
 * SK's Minecraft Launcher
 * Copyright (C) 2010-2014 Albert Pham <http://www.sk89q.com> and contributors
 * Please see LICENSE.txt for license information.
 */
package com.skcraft.launcher.builder

import com.skcraft.launcher.model.modpack.LaunchModifier
import com.skcraft.launcher.model.modpack.Manifest
import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class BuilderConfig {
    @Optional
    var name: String? = null
    @Optional var title: String? = null
    @Optional var gameVersion: String? = null
    @SerialName("launch")
    @Optional var launchModifier: LaunchModifier? = LaunchModifier()
        set(launchModifier) {
            field = launchModifier ?: LaunchModifier()
        }
    @Optional var features: List<FeaturePattern>? = arrayListOf()
        set(features) {
            field = features ?: arrayListOf()
        }
    @Optional var userFiles: FnPatternList? = FnPatternList()
        set(userFiles) {
            field = userFiles ?: FnPatternList()
        }

    fun update(manifest: Manifest) {
        manifest.updateName(name)
        manifest.updateTitle(title)
        manifest.updateGameVersion(gameVersion)
        manifest.launchModifier = launchModifier
    }

    fun registerProperties(applicator: PropertiesApplicator) {
        if (this.features != null) {
            for (feature in this.features!!) {
                if(feature.feature.name.isNullOrEmpty()) {
                    throw IllegalArgumentException("Empty feature name found")
                }
                applicator.register(feature)
            }
        }
        applicator.userFiles = this.userFiles
    }
}
