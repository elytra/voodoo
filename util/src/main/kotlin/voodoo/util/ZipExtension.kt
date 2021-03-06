package voodoo.util

import mu.KotlinLogging
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


private val logger = KotlinLogging.logger {}

fun packToZip(sourceDir: File, zipFile: File, preserveTimestamps: Boolean = true) {
    zipFile.let { if (it.exists()) it.delete() }

    zipFile.outputStream().use { os ->
        ZipOutputStream(os).use { stream ->
            sourceDir.walkTopDown().filter { file -> !file.isDirectory }.forEach { file ->
                val zipEntry = ZipEntry(file.toRelativeUnixPath(sourceDir))
                if(!preserveTimestamps) {
                    zipEntry.time = 0
                } else {
                    zipEntry.time = file.lastModified()
                }
                stream.putNextEntry(zipEntry)
                stream.write(file.readBytes())
                stream.closeEntry()
            }
        }
    }
}

fun uncompressZip(zipFile: File, destDir: File) {
    UnzipUtility.unzip(zipFile, destDir)
}
//
//fun compressTarGz(sourceDir: File, zipFile: File) {
//    zipFile.let { if (it.exists()) it.delete() }
//
//    zipFile.outputStream().use { os ->
//        GzipCompressorOutputStream(os).use { gzOut ->
//            TarArchiveOutputStream(gzOut).use { tOut ->
//                sourceDir.walkTopDown().filter { file -> !file.isDirectory }.forEach { file ->
//                    val tarEntry = TarArchiveEntry(file, file.toRelativeUnixPath(sourceDir))
//                    tOut.putArchiveEntry(tarEntry)
//                    Files.copy(file.toPath(), tOut)
//                    tOut.closeArchiveEntry();
//                }
//            }
//        }
//    }
//}
//
//fun uncompressTarGz(zipFile: File, destDir: File) {
//    logger.info("unzipping: $zipFile into $destDir")
//    require(zipFile.exists()) { "$zipFile does not exist" }
//    require(zipFile.isFile) { "$zipFile not not a file" }
//
//    val destDir = destDir.absoluteFile
//    if (!destDir.exists()) {
//        destDir.mkdir()
//    }
//
//    FileInputStream(zipFile).use { fi ->
//        BufferedInputStream(fi).use { bi ->
//            GzipCompressorInputStream(bi).use { gzi ->
//                TarArchiveInputStream(gzi).use { ti ->
//                    do {
//                        val entry = ti.nextTarEntry ?: break
//                        val targetFile = destDir.resolve(entry.name)
//
//                        targetFile.parentFile.mkdirs()
//                        Files.copy(ti, targetFile.toPath())
//                    } while(true)
//                }
//            }
//        }
//    }
//}
//
//fun packTar(sourceDir: File, zipFile: File) {
//    zipFile.let { if (it.exists()) it.delete() }
//
//    zipFile.outputStream().use { os ->
//        TarArchiveOutputStream(os).use { tOut ->
//            sourceDir.walkTopDown().filter { file -> !file.isDirectory }.forEach { file ->
//                val tarEntry = TarArchiveEntry(file, file.toRelativeUnixPath(sourceDir))
//                tOut.putArchiveEntry(tarEntry)
//                Files.copy(file.toPath(), tOut)
//                tOut.closeArchiveEntry();
//            }
//        }
//    }
//}
//
//fun uncompressTar(zipFile: File, destDir: File) {
//    logger.info("unzipping: $zipFile into $destDir")
//    require(zipFile.exists()) { "$zipFile does not exist" }
//    require(zipFile.isFile) { "$zipFile not not a file" }
//
//    val destDir = destDir.absoluteFile
//    if (!destDir.exists()) {
//        destDir.mkdir()
//    }
//
//    FileInputStream(zipFile).use { fi ->
//        BufferedInputStream(fi).use { bi ->
//            TarArchiveInputStream(bi).use { ti ->
//                do {
//                    val entry = ti.nextTarEntry ?: break
//                    val targetFile = destDir.resolve(entry.name)
//
//                    targetFile.parentFile.mkdirs()
//                    Files.copy(ti, targetFile.toPath())
//                } while(true)
//            }
//        }
//    }
//}
