package voodoo.data.lock

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import mu.KotlinLogging
import voodoo.data.curse.FileID
import voodoo.data.curse.ProjectID
import voodoo.provider.*
import java.time.Instant

/**
 * Created by nikky on 28/03/18.
 * @author Nikky
 */
@Serializable
sealed class LockEntry : CommonLockModule {
    @Transient
    abstract val providerType: String

    @Serializable
    @SerialName("curse")
    data class Curse(
        val common: CommonLockComponent,
        val projectID: ProjectID = ProjectID.INVALID,
        val fileID: FileID = FileID.INVALID,
        val useOriginalUrl: Boolean = true,
        val skipFingerprintCheck: Boolean = false
    ) : LockEntry(), CommonLockModule by common {
        override val providerType = CurseProvider.id
        init {
            optional = optionalData != null
        }
    }

    @Serializable
    @SerialName("direct")
    data class Direct(
        val common: CommonLockComponent,
        val url: String = "",
        val useOriginalUrl: Boolean = true
    ) : LockEntry(), CommonLockModule by common {
        override val providerType = DirectProvider.id
        init {
            optional = optionalData != null
        }
    }

    @Serializable
    @SerialName("jenkins")
    data class Jenkins(
        val common: CommonLockComponent,
        val jenkinsUrl: String = "",
        val job: String = "",
        val buildNumber: Int = -1,
        val fileNameRegex: String = ".*(?<!-sources\\.jar)(?<!-api\\.jar)(?<!-deobf\\.jar)(?<!-lib\\.jar)(?<!-slim\\.jar)$"
    ) : LockEntry(), CommonLockModule by common {
        override val providerType = JenkinsProvider.id
        init {
            optional = optionalData != null
        }
    }

    @Serializable
    @SerialName("local")
    data class Local(
        val common: CommonLockComponent,
        var fileSrc: String = ""
    ) : LockEntry(), CommonLockModule by common {
        override val providerType = LocalProvider.id
        init {
            optional = optionalData != null
        }
    }

    @Serializable
    @SerialName("noop")
    data class Noop(
        val common: CommonLockComponent
    ) : LockEntry(), CommonLockModule by common {
        override val providerType = NoopProvider.id
        init {
            optional = optionalData != null
        }
    }

    fun displayName(entryId: String): String = name?.takeIf { it.isNotBlank() } ?: runBlocking { provider().generateName(entryId, this@LockEntry) }

    @Transient
    lateinit var parent: LockPack

    @Transient
    var optional: Boolean = false // optionalData != null

//    /**
//     * relative to src folder
//     */
//    @Transient
//    val serialFile: File
//        get() {
//            return folder.resolve("$id.lock.json")
//        }

//    @Transient
//    lateinit var srcFolder: File

//P

    fun provider(): ProviderBase = Providers[providerType]

    fun version(): String = runBlocking { provider().getVersion(this@LockEntry) }

    fun license(): String = runBlocking { provider().getLicense(this@LockEntry) }

    fun thumbnail(): String = runBlocking { provider().getThumbnail(this@LockEntry) }

    fun authors(): String = runBlocking { provider().getAuthors(this@LockEntry).joinToString(", ") }

    fun projectPage(): String = runBlocking { provider().getProjectPage(this@LockEntry) }

    fun releaseDate(): Instant? = runBlocking { provider().getReleaseDate(this@LockEntry) }

//    fun isCurse(): Boolean = provider == CurseProvider.id
//
//    fun isJenkins(): Boolean = provider == JenkinsProvider.id
//
//    fun isDirect(): Boolean = provider == DirectProvider.id
//
//    fun isJson(): Boolean = provider == UpdateJsonProvider.id
//
//    fun isLocal(): Boolean = provider == LocalProvider.id

    //    @Serializer(forClass = LockEntry::class)
    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
