package voodoo.builder.curse

import aballano.kotlinmemoization.memoize
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import khttp.get
import mu.KLogging
import voodoo.builder.data.Entry
import voodoo.builder.data.Modpack
import voodoo.builder.provider.CurseProviderThing

/**
 * Created by nikky on 30/01/18.
 * @author Nikky
 * @version 1.0
 */
object CurseUtil : KLogging() {
    private val META_URL = "https://cursemeta.nikky.moe" //TODO: move into Entry

    val mapper = jacksonObjectMapper() // Enable Json parsing
            .registerModule(KotlinModule())!! // Enable Kotlin support

    val data: List<Addon> = getAddonData()

    private fun getAddonData(): List<Addon> {
        // val url = "$META_URL/api/addon/?mods=1&texturepacks=1&worlds=1&property=id,name,summary,websiteURL,packageType,categorySection"
        val url = "$META_URL/api/addon/?mods=1&texturepacks=1&worlds=1"

        CurseProviderThing.logger.trace("get $url")
        val r = get(url)
        if (r.statusCode == 200) {
            return mapper.readValue(r.text)
        }

        CurseProviderThing.logger.error("failed getting cursemeta data")
        throw Exception("failed getting cursemeta data")
    }

    private fun getAddonFileCall(addonId: Int, fileId: Int): AddonFile? {
        val url = "$META_URL/api/addon/$addonId/files/$fileId"

        CurseProviderThing.logger.debug("get $url")
        val r = get(url)
        if (r.statusCode == 200) {
            return mapper.readValue(r.text)
        }
        return null
    }

    val getAddonFile = ::getAddonFileCall.memoize()

    private fun getAllAddonFilesCall(addonId: Int): List<AddonFile> {
        val url = "$META_URL/api/addon/$addonId/files"

        CurseProviderThing.logger.debug("get $url")
        val r = get(url)
        if (r.statusCode == 200) {
            return mapper.readValue(r.text)
        }
        throw Exception("failed getting cursemeta data")
    }

    val getAllAddonFiles = ::getAllAddonFilesCall.memoize()

    private fun getAddonCall(addonId: Int): Addon? {
        val url = "$META_URL/api/addon/$addonId"

        CurseProviderThing.logger.debug("get $url")
        val r = get(url)
        if (r.statusCode == 200) {
            return mapper.readValue(r.text)
        }
        return null
    }

    val getAddon = ::getAddonCall.memoize()


    fun findFile(entry: Entry, modpack: Modpack): Triple<Int, Int, String> {
        val mcVersions = listOf(modpack.mcVersion) + entry.validMcVersions
        val name = entry.name
        val version = entry.version
        var releaseTypes = entry.releaseTypes
//        if(releaseTypes.isEmpty()) {
//            releaseTypes = setOf(ReleaseType.RELEASE, ReleaseType.BETA) //TODO: is this not already set because i enforce defaults ?
//        }
        var addonId = entry.id
        var fileId = entry.fileId
        val fileNameRegex = entry.curseFileNameRegex

        val addon = data.find { addon ->
            (name.isNotBlank() && name.equals(addon.name, true))
                    || (addonId > 0 && addonId == addon.id)
        } ?: return Triple(-1, -1, "")

        addonId = addon.id

        val re = Regex(fileNameRegex)

        if (fileId > 0) {
            val file = getAddonFile(addonId, fileId)
            if (file != null)
                return Triple(addonId, file.id, file.fileNameOnDisk)
        }

        var files = getAllAddonFiles(addonId).sortedWith(compareByDescending { it.fileDate })

        var oldFiles = files

        if (version.isNotBlank()) {
            files = files.filter { f ->
                (f.fileName.contains(version, true) || f.fileName == version)
            }
            if (files.isEmpty()) {
                logger.error("filtered files did not match version {}", oldFiles)
            }
            oldFiles = files
        }

        if (files.isNotEmpty()) {
            files = files.filter { f ->
                mcVersions.any { v -> f.gameVersion.contains(v) }
            }

            if (files.isEmpty()) {
                logger.error("filtered files did not match mcVersion {}", oldFiles)
            }
            oldFiles = files
        }

        if (files.isNotEmpty()) {
            files = files.filter { f ->
                releaseTypes.contains(f.releaseType)
            }

            if (files.isEmpty()) {
                logger.error("filtered files did not match releaseType {}", oldFiles)
            }
            oldFiles = files
        }

        if (files.isNotEmpty()) {
            files = files.filter { f ->
                re.matches(f.fileName)
            }
            if (files.isEmpty()) {
                logger.error("filtered files did not match regex {}", oldFiles)
            }
        }

        val file = files.sortedWith(compareByDescending { it.fileDate }).firstOrNull()
        if (file == null) {
            val filesUrl = "$META_URL/api/addon/$addonId/files"
            CurseProviderThing.logger.error("no matching version found for ${addon.name} addon_url: ${addon.webSiteURL} " +
                    "files: $filesUrl mc version: $mcVersions version: $version \n" +
                    "$addon")
            CurseProviderThing.logger.error("no file matching the parameters found for ${addon.name}")
            return Triple(addonId, -1, "")
        }
        return Triple(addonId, file.id, file.fileNameOnDisk)
    }
}