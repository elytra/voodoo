// Generated by delombok at Sat Jul 14 01:46:55 CEST 2018
/*
 * SK's Minecraft Launcher
 * Copyright (C) 2010-2014 Albert Pham <http://www.sk89q.com> and contributors
 * Please see LICENSE.txt for license information.
 */
package com.skcraft.launcher.builder

import com.skcraft.launcher.model.modpack.FileInstall
import com.skcraft.launcher.model.modpack.Manifest
import mu.KLogging
import org.apache.commons.io.FilenameUtils
import voodoo.util.toHex
import java.io.File
import java.io.IOException
import java.security.MessageDigest

/**
 * Walks a path and adds hashed path versions to the given
 * [Manifest].
 *
 * @param manifest the manifest
 * @param applicator applies properties to manifest entries
 * @param destDir the destination directory to copy the hashed objects
 */
class ClientFileCollector(
    private val manifest: Manifest,
    private val applicator: PropertiesApplicator,
    private val destDir: File
) : DirectoryWalker() {
    init {
    }

    override fun getBehavior(name: String): DirectoryWalker.DirectoryBehavior {
        return getDirectoryBehavior(name)
    }

    @Throws(IOException::class)
    override fun onFile(file: File, relPath: String) {
        if (file.name.endsWith(FileInfoScanner.FILE_SUFFIX) || file.name.endsWith(URL_FILE_SUFFIX)) {
            return
        }
        val sha1 = MessageDigest.getInstance("SHA-1")
        val bytes = file.readBytes()
        val hash = sha1.digest(bytes).toHex()
        val to = FilenameUtils.separatorsToUnix(FilenameUtils.normalize(relPath))
        // url.txt override file
        val urlFile = File(file.absoluteFile.parentFile, file.name + URL_FILE_SUFFIX)
        val location: String
        var copy = true
        if (urlFile.exists() && !System.getProperty("com.skcraft.builder.ignoreURLOverrides", "false").equals(
                "true",
                ignoreCase = true
            )
        ) {
            location = urlFile.readLines().first()
            copy = false
        } else {
            location = hash.substring(0, 2) + "/" + hash.substring(2, 4) + "/" + hash
        }
        val destPath = File(destDir, location)
        val entry = FileInstall(
            hash = hash,
            location = location,
            to = to,
            size = file.length()
        )
        applicator.apply(entry)
        destPath.parentFile.mkdirs()
        logger.info(String.format("Adding %s from %s...", relPath, file.absolutePath))
        if (copy) {
            file.copyTo(destPath, overwrite = true)
        }
        manifest.tasks += entry
    }

    companion object : KLogging() {
        const val URL_FILE_SUFFIX = ".url.txt"

        fun getDirectoryBehavior(name: String): DirectoryWalker.DirectoryBehavior {
            return when {
                name.startsWith(".") -> DirectoryWalker.DirectoryBehavior.SKIP
                name == "_OPTIONAL" -> DirectoryWalker.DirectoryBehavior.IGNORE
                name == "_SERVER" -> DirectoryWalker.DirectoryBehavior.SKIP
                name == "_CLIENT" -> DirectoryWalker.DirectoryBehavior.IGNORE
                else -> DirectoryWalker.DirectoryBehavior.CONTINUE
            }
        }
    }
}