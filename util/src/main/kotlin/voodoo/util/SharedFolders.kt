package voodoo.util

import java.io.File

object SharedFolders {

    interface SystemProperty {
        val key: String
    }

    object RootDir : SystemProperty {
        override val key = "voodoo.rootDir"
        lateinit var value: File // = File(System.getProperty("user.dir"))
        val defaultInitialized: Boolean
            get() = ::value.isInitialized
        var resolver: () -> File = { value }
        fun get() = System.getProperty(key)?.asFile ?: resolver()
    }

    object GitRoot : SystemProperty {
        override val key = "voodoo.gitRoot"
        var resolver: (rootDir: File) -> File = { rootDir -> rootDir }
        fun get(): File = System.getProperty(key)?.asFile ?: resolver(RootDir.get())
    }

    object PackDir : SystemProperty {
        override val key = "voodoo.packDir"
        var resolver: (rootDir: File) -> File = { rootDir -> rootDir.resolve("packs") }
        fun get(): File = System.getProperty(key)?.asFile ?: resolver(RootDir.get())
    }

    object TomeDir : SystemProperty {
        override val key = "voodoo.tomeDir"
        var resolver: (rootDir: File) -> File = { rootDir -> rootDir.resolve("tome") }
        fun get(): File = System.getProperty(key)?.asFile ?: resolver(RootDir.get())
    }

    object IncludeDir : SystemProperty {
        override val key = "voodoo.includeDir"
        var resolver: (rootDir: File) -> File = { rootDir -> rootDir.resolve("include") }
        fun get(): File = System.getProperty(key)?.asFile ?: resolver(RootDir.get())
    }

    object BuildCache : SystemProperty {
        override val key = "voodoo.buildCache"
        var resolver: (rootDir: File) -> File = { rootDir -> rootDir.resolve("build").resolve(".voodoo") }
        fun get(): File = System.getProperty(key)?.asFile ?: resolver(RootDir.get())
    }

    object GeneratedSrcShared : SystemProperty {
        override val key = "voodoo.generatedGlobalSrcDir"
        var resolver: (rootDir: File) -> File = { rootDir -> rootDir.resolve("build").resolve(".voodoo").resolve("gen") }
        fun get(): File = System.getProperty(key)?.asFile ?: resolver(RootDir.get())
    }

    object GeneratedSrc : SystemProperty {
        override val key = "voodoo.generatedSrcDir"
        var resolver: (rootDir: File, id: String) -> File = { rootDir, id -> rootDir.resolve("build").resolve(".voodoo").resolve(id) }
        fun get(id: String): File = System.getProperty(key)?.asFile ?: resolver(RootDir.get(), id.toLowerCase())
    }

    object UploadDir : SystemProperty {
        override val key = "voodoo.uploadDir"
        var resolver: (rootDir: File, id: String) -> File = { rootDir, id -> rootDir.resolve("_upload") }
        fun get(id: String): File = System.getProperty(key)?.asFile ?: resolver(RootDir.get(), id)
    }

    object DocDir : SystemProperty {
        override val key = "voodoo.docDir"
        var resolver: (rootDir: File, id: String) -> File = { rootDir, id -> UploadDir.get(id).resolve("docs").resolve(id) }
        fun get(id: String): File = System.getProperty(key)?.asFile ?: resolver(RootDir.get(), id)
    }

    fun setSystemProperties(id: String, setSystemProperty: (key: String, value: Any) -> Unit) {
        RootDir.apply { setSystemProperty(key, get()) }
        GitRoot.apply { setSystemProperty(key, get()) }
        PackDir.apply { setSystemProperty(key, get()) }
        TomeDir.apply { setSystemProperty(key, get()) }
        IncludeDir.apply { setSystemProperty(key, get()) }
        BuildCache.apply { setSystemProperty(key, get()) }
        GeneratedSrcShared.apply { setSystemProperty(key, get()) }
        GeneratedSrc.apply { setSystemProperty(key, get(id)) }
        UploadDir.apply { setSystemProperty(key, get(id)) }
        DocDir.apply { setSystemProperty(key, get(id)) }
    }
}
