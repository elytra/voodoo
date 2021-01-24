package voodoo

import com.eyeem.watchadoin.Stopwatch
import mu.KotlinLogging
import voodoo.builder.Builder
import voodoo.changelog.ChangelogBuilder
import voodoo.data.nested.NestedPack
import voodoo.pack.AbstractPack
import voodoo.tome.TomeEnv
import voodoo.util.SharedFolders
import voodoo.dsl.GeneratedConstants
import java.io.File

sealed class VoodooTask(open val key: String) {
    companion object {
        private val logger = KotlinLogging.logger{}
    }
    object Build : VoodooTask("build") {
        suspend fun execute(
            stopwatch: Stopwatch,
            id: String,
            nestedPack: NestedPack,
            rootFolder: File,
            tomeEnv: TomeEnv?
        ) = stopwatch {
            require(rootFolder.isAbsolute) { "rootFolder: '$rootFolder' is not absolute" }
            //        rootFolder.resolve(id).walkTopDown().asSequence()
            //            .filter {
            //                it.isFile && it.name.endsWith(".entry.json")
            //            }
            //            .forEach {
            //                it.delete()
            //            }
            val modpack = nestedPack.flatten(rootFolder = rootFolder, id = id) //TODO: add stopwatch more levels down
            logger.debug { "modpack: $modpack" }
            logger.debug { "entrySet: ${modpack.entrySet}" }

            val lockPack = Builder.lock("build".watch, modpack)

            if (tomeEnv != null) {
                val uploadDir = SharedFolders.UploadDir.get(id)
                val rootDir = SharedFolders.RootDir.get().absoluteFile

                // TODO: merge tome into core
                "tome".watch {
                    Tome.generate(this, lockPack, tomeEnv, uploadDir)
                }

                // TODO: just generate meta info

//                Diff.writeMetaInfo(
//                    stopwatch = "writeMetaInfo".watch,
//                    rootDir = rootDir.absoluteFile,
//                    newPack = lockPack
//                )
            }
        }
    }

    object Changelog : VoodooTask("changelog") {
        suspend fun execute(
            stopwatch: Stopwatch,
            id: String,
            changelogBuilder: ChangelogBuilder,
            tomeEnv: TomeEnv
        ): Nothing = error("code moved to clikt commands")
//        stopwatch {
//                "changelogTask".watch {
//                    val rootDir = SharedFolders.RootDir.get().absoluteFile
//                    val uploadDir = SharedFolders.UploadDir.get(id)
//                    val docDir = SharedFolders.DocDir.get(id)
//
//                    val lockFileName = "$id.lock.pack.json"
//                    val lockFile = rootDir.resolve(id).resolve(lockFileName)
//
//                    val lockPack = LockPack.parse(lockFile.absoluteFile, rootDir)
//
//                    "tome".watch {
//                        Tome.generate(this, lockPack, tomeEnv, uploadDir)
//                    }
//                    Diff.createChangelog(
//                        stopwatch = "createDiff".watch,
//                        docDir = docDir,
//                        rootDir = rootDir.absoluteFile,
//                        currentPack = lockPack,
//                        changelogBuilder = changelogBuilder
//                    )
//                }
//            }
    }

    object Pack : VoodooTask("pack") {
        suspend fun execute(
            stopwatch: Stopwatch,
            id: String,
            packer: AbstractPack
        ): Nothing = error("code moved to clikt 'package' command")
//        stopwatch {
//            val rootDir = SharedFolders.RootDir.get().absoluteFile
//            val uploadDir = SharedFolders.UploadDir.get(id)
//            val lockFileName = "$id.lock.pack.json"
//            val lockFile = rootDir.resolve(id).resolve(lockFileName)
//
//            val modpack = LockPack.parse(lockFile.absoluteFile, rootDir)
//            // TODO: pass pack method (enum / object)
//            voodoo.Pack.pack("pack".watch, modpack, uploadDir, packer)
//        }
    }

    object Test : VoodooTask("test") {
        suspend fun execute(
            stopwatch: Stopwatch,
            id: String,
            method: TestMethod
        ): Nothing = error("code moved to clikt 'launch' command")
//        = stopwatch {
//            "testTask".watch {
//                val rootDir = SharedFolders.RootDir.get().absoluteFile
//                val lockFileName = "$id.lock.pack.json"
//                val lockFile = rootDir.resolve(id).resolve(lockFileName)
//
//                val modpack = LockPack.parse(lockFile.absoluteFile, rootDir)
//
//                method.tester.execute(stopwatch = "${method.key}-test".watch, modpack = modpack, clean = method.clean)
//            }
//        }
    }

    object Version : VoodooTask("version") {
        val version = GeneratedConstants.FULL_VERSION
    }
}