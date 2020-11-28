package voodoo.builder

import com.eyeem.watchadoin.Stopwatch
import mu.KLogging
import voodoo.data.flat.FlatModPack
import voodoo.data.lock.LockEntry
import voodoo.data.lock.LockPack
import voodoo.provider.Providers
import voodoo.util.filterValueIsInstance
import voodoo.util.toJson
import kotlin.system.exitProcess

/**
 * Created by nikky on 28/03/18.
 * @author Nikky
 */

object Builder : KLogging() {
    suspend fun lock(
        stopwatch: Stopwatch,
        modpack: FlatModPack
    ): LockPack = stopwatch {
        val targetFile = LockPack.fileForVersion(baseDir = modpack.baseFolder, version = modpack.version)
        val targetFolder = LockPack.baseFolderForVersion(baseDir = modpack.baseFolder, version = modpack.version)

        modpack.entrySet.forEach { entry ->
            logger.info("id: ${entry.id} entry: $entry")
        }

        "resolve". watch {
            try {
                resolve(
                    this,
                    modpack
                )
            } catch (e: Exception) {
                e.printStackTrace()
//                coroutineContext.cancel()
                exitProcess(1)
            }
        }


        "validate".watch {
            modpack.lockEntryMap.forEach { (entryId, lockEntry) ->
                val provider = Providers[lockEntry.providerType]
                if (!provider.validate(entryId, lockEntry)) {
                    logger.error { lockEntry }
                    throw IllegalStateException("entry did not validate")
                }
            }
        }

        logger.info("Creating locked pack...")
        val lockedPack = modpack.lock("lock".watch, targetFolder)

        logger.info("Writing lock file... $targetFile")
        targetFile.parentFile.mkdirs()
        targetFile.writeText(lockedPack.toJson(LockPack.serializer()))

        logger.info { "copying input files into output" }
        //FIXME: also include local ?
        logger.info { "copying: ${modpack.sourceFolder}" }
        lockedPack.sourceFolder.also { sourceFolder ->
            sourceFolder.deleteRecursively()
            sourceFolder.mkdirs()
            modpack.sourceFolder.copyRecursively(sourceFolder, overwrite = true)
        }
        logger.info { "copying: ${modpack.iconFile}" }
        modpack.iconFile
            .takeIf { it.exists() }
            ?.copyTo(lockedPack.iconFile, overwrite = true)

        lockedPack.localFolder.also { localFolder ->
            localFolder.deleteRecursively()
            localFolder.mkdirs()
            lockedPack.entries.filterValueIsInstance<String, LockEntry.Local>()
                .forEach { (entryId, entry) ->
                    val localTargetFile = localFolder.resolve(entry.fileSrc)
                    logger.info { "copying: $targetFile" }
                    localTargetFile.absoluteFile.parentFile.mkdirs()
                    modpack.localFolder.resolve(entry.fileSrc).copyTo(localTargetFile, overwrite = true)
                }
        }

        lockedPack
    }
}
