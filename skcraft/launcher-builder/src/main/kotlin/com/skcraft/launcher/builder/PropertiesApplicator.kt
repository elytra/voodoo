// Generated by delombok at Sat Jul 14 01:46:55 CEST 2018
/*
 * SK's Minecraft Launcher
 * Copyright (C) 2010-2014 Albert Pham <http://www.sk89q.com> and contributors
 * Please see LICENSE.txt for license information.
 */
package com.skcraft.launcher.builder

import com.skcraft.launcher.model.modpack.*
import java.util.ArrayList
import java.util.HashSet

class PropertiesApplicator(private val manifest: Manifest) {
    private val used = HashSet<Feature>()
    private val features = ArrayList<FeaturePattern>()
    var userFiles: FnPatternList? = null

    val featuresInUse: List<Feature>
        get() = ArrayList(used)

    fun apply(entry: ManifestEntry) {
        if (entry is FileInstall) {
            apply(entry)
        }
    }

    private fun apply(entry: FileInstall) {
        val path = entry.targetPath
        entry.conditionWhen = fromFeature(path)
        entry.isUserFile = isUserFile(path)
    }

    fun isUserFile(path: String): Boolean {
        return if (userFiles != null) {
            userFiles!!.matches(path)
        } else {
            false
        }
    }

    fun fromFeature(path: String): Condition? {
        val found = ArrayList<Feature>()
        for (pattern in features) {
            if (pattern.matches(path)) {
                used.add(pattern.feature)
                found.add(pattern.feature)
            }
        }
        return if (!found.isEmpty()) {
            RequireAny(found)
        } else {
            null
        }
    }

    fun register(component: FeaturePattern) {
        features.add(component)
    }
}