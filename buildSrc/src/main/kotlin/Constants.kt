import org.gradle.api.tasks.wrapper.Wrapper

fun create(
        group: String,
        name: String,
        version: String? = null
): String = buildString {
    append(group)
    append(':')
    append(name)
    version?.let {
        append(':')
        append(it)
    }
}

object Gradle {
    const val version = "4.10.2"
    val distributionType = Wrapper.DistributionType.ALL
}

object Kotlin {
    const val version = "1.2.71"
}

object Coroutines {
    const val version = "0.30.0"
    val dependency = create(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = version)
}

object Serialization {
    const val version = "0.6.2"
    const val id = "kotlinx-serialization"
    const val module = "org.jetbrains.kotlinx:kotlinx-gradle-serialization-plugin"
    val dependency = create(group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-runtime", version = version)
}

object Poet {
    const val version = "1.0.0-RC1"
    val dependency = create(group = "com.squareup", name = "kotlinpoet", version = version)
}

object Fuel {
    const val version = "1.15.0"
    val dependency = create(group = "com.github.kittinunf.fuel", name = "fuel", version = version)
    val dependencyCoroutines = create(group = "com.github.kittinunf.fuel", name = "fuel-coroutines", version = version)
}

object Argparser {
    const val version = "2.0.7"
    val dependency = create(group = "com.xenomachina", name = "kotlin-argparser", version = version)
}

object KotlinxHtml {
    const val version = "0.6.10"
    val dependency = create(group = "org.jetbrains.kotlinx", name = "kotlinx-html-jvm", version = version)
}

object Logging {
    const val version = "1.6.10"

    val dependency = create(group = "io.github.microutils", name = "kotlin-logging", version = version)
    val dependencyLogbackClassic = create(group = "ch.qos.logback", name = "logback-classic", version = "1.3.0-alpha4")
}

object Spek {
    const val version = "2.0.0-rc.1"
    val dependencyDsl = create(group = "org.spekframework.spek2", name = "spek-dsl-jvm", version = version)
    val dependencyRunner = create(group = "org.spekframework.spek2", name = "spek-runner-junit5", version = version)
    val dependencyJUnit5 = create(group = "org.junit.platform", name = "junit-platform-engine", version = "1.3.0-RC1")
}

object Apache {
    val commonsCompress = create(group = "org.apache.commons", name = "commons-compress", version = "1.18")
}

object Jenkins {
    const val jenkinsUrl: String = "https://ci.elytradev.com"
    const val jenkinsJob: String = "elytra/Voodoo/master"
}