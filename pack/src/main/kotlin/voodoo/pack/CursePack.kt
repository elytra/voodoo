package voodoo.pack

import com.eyeem.watchadoin.Stopwatch
import kotlinx.coroutines.*
import kotlinx.html.ATarget
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.html
import kotlinx.html.li
import kotlinx.html.stream.createHTML
import kotlinx.html.ul
import Modloader
import mu.KotlinLogging
import voodoo.data.Side
import voodoo.data.curse.CurseFile
import voodoo.data.curse.CurseManifest
import voodoo.data.curse.CurseMinecraft
import voodoo.data.curse.CurseModLoader
import voodoo.data.lock.LockEntry
import voodoo.data.lock.LockPack
import voodoo.forge.ForgeUtil
import voodoo.provider.CurseProvider
import voodoo.provider.Providers
import voodoo.util.*
import java.io.File

/**
 * Created by nikky on 30/03/18.
 * @author Nikky
 */

object CursePack : AbstractPack("curse") {
    private val logger = KotlinLogging.logger {}
    override val label = "Curse Pack"

    override fun File.getOutputFolder(id: String, version: String): File = resolve("curse")

    override suspend fun pack(
        stopwatch: Stopwatch,
        modpack: LockPack,
        config: PackConfig,
        output: File,
        uploadBaseDir: File,
        clean: Boolean,
        versionAlias: String?
    ) = stopwatch {
        val directories = Directories.get()

        val cacheDir = directories.cacheHome
        val workspaceDir = directories.cacheHome.resolve("curse-workspace")
        val modpackDir = workspaceDir.resolve(with(modpack) { "$id-${versionAlias ?: version}" })
        val srcFolder = modpackDir.resolve("overrides")

        if (clean) {
            logger.info("cleaning modpack directory $srcFolder")
            srcFolder.deleteRecursively()
        }
        if (!srcFolder.exists()) {
            logger.info("copying files into overrides")
            val mcDir = modpack.sourceFolder
            if (mcDir.exists()) {
                mcDir.copyRecursively(srcFolder, overwrite = true)
            } else {
                logger.warn("minecraft directory $mcDir does not exist")
            }
        }

        for (file in srcFolder.walkTopDown()) {
            when {
                file.name == "_SERVER" -> file.deleteRecursively()
                file.name == "_CLIENT" -> file.renameTo(file.parentFile)
            }
        }

        val loadersFolder = modpackDir.resolve("loaders")
        logger.info("cleaning loaders $loadersFolder")
        loadersFolder.deleteRecursively()

        coroutineScope {
            val jobs = mutableListOf<Pair<String, Deferred<CurseFile?>>>()

            val forgeVersion = (modpack.modloader as? Modloader.Forge)?.let { loader ->
                ForgeUtil.forgeVersionOf(loader.forgeVersion)
            }

            val modsFolder = srcFolder.resolve("mods")
            logger.info("cleaning mods $modsFolder")
            modsFolder.deleteRecursively()

            val curseMods = withPool { pool ->
//                val curseModsChannel = Channel<CurseFile>(Channel.CONFLATED)

                // download entries
                for (entry in modpack.entries) {
                    if (entry.side == Side.SERVER) continue
                    jobs += "install_${entry.id}" to async<CurseFile?>(context = coroutineContext + CoroutineName("install_${entry.id}") + pool) {
                        "install_${entry.id}".watch {
                            logger.info("starting job: install '${entry.id}' entry: $entry")
                            val targetFolder = srcFolder.resolve(entry.path).absoluteFile.parentFile
                            val required = !modpack.isEntryOptional(entry.id)

                            logger.debug { "required: $required" }

                            val provider = Providers[entry.providerType]
                            if (provider == CurseProvider && entry is LockEntry.Curse) {
                                return@watch CurseFile(
                                        entry.projectID,
                                        entry.fileID,
                                        required
                                    ).also {
                                        logger.info("added curse file $it")
                                    }
                            } else {
                                logger.debug { "is not a curse thing: $entry" }
                                val (_, file) = provider.download("download_${entry.id}".watch, entry, targetFolder, cacheDir) ?: return@watch null
                                if (!required) {
                                    val optionalFile = file.parentFile.resolve(file.name + ".disabled")
                                    file.renameTo(optionalFile)
                                }
                                return@watch null
                            }
                        }
                    }
                    delay(10)
                }

//                delay(100)
                logger.info("waiting for jobs to finish")
//                curseModsChannel.consume {
//                    logger.debug { "received: $this" }
//                    jobs.forEach {
//                        logger.debug { "waiting for ${it[CoroutineName.Key]?.name}" }
//
//                        it.join()
//                    }
////                    jobs.joinAll()
//                }
//                curseModsChannel.toList()
                jobs.mapNotNull { (name, deferred) ->
                    logger.debug { "waiting for $name" }
                    val value = deferred.await()

                    logger.debug { "finished: $name, value: $value" }
                    value
                }
            }

            // generate modlist
            val modListFile = modpackDir.resolve("modlist.html")

            val html = "html".watch {
                createHTML().html {
                    body {
                        ul {
                            for (entry in modpack.entries.sortedBy { it.displayName.toLowerCase() }) {
                                val provider = Providers[entry.providerType]
                                if (entry.side == Side.SERVER) {
                                    continue
                                }
                                val projectPage =
                                    runBlocking { provider.getProjectPage(entry) }
                                val authors = runBlocking { provider.getAuthors(entry) }
                                val authorString =
                                    if (authors.isNotEmpty()) " (by ${authors.joinToString(", ")})" else ""

                                li {
                                    when {
                                        projectPage.isNotEmpty() -> a(href = projectPage) { +"${entry.displayName} $authorString" }
                                        entry is LockEntry.Direct && entry.url.isNotBlank() -> {
                                            +"direct: "
                                            a(
                                                href = entry.url,
                                                target = ATarget.blank
                                            ) { +"${entry.displayName} $authorString" }
                                        }
                                        else -> {
                                            val source = if ( entry is LockEntry.Local && entry.fileSrc.isNotBlank()) {
                                                    "file://" + entry.fileSrc
                                                } else {
                                                    "unknown"
                                                }
                                            +"${entry.displayName} $authorString (source: $source)"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (modListFile.exists()) modListFile.delete()
            modListFile.createNewFile()
            modListFile.writeText(html)

            val curseManifest = CurseManifest(
                name = modpack.title.blankOr ?: modpack.id,
                version = versionAlias ?: modpack.version,
                author = modpack.authors.joinToString(", "),
                minecraft = CurseMinecraft(
                    version = modpack.mcVersion,
                    modLoaders = forgeVersion?.run {
                        listOf(
                            CurseModLoader(
                                id = "forge-$forgeVersion",
                                primary = true
                            )
                        )
                    } ?: emptyList()
                ),
                manifestType = "minecraftModpack",
                manifestVersion = 1,
                files = curseMods,
                overrides = "overrides"
            )
            val manifestFile = modpackDir.resolve("manifest.json")
            manifestFile.writeText(json.encodeToString(CurseManifest.serializer(), curseManifest))

            val cursePackFile = output.resolve(with(modpack) { "$id-${versionAlias ?: version}.zip" })

            packToZip(modpackDir, cursePackFile)

            logger.info("packed ${modpack.id} -> $cursePackFile")
        }
    }
}