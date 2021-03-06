import de.fayard.refreshVersions.bootstrapRefreshVersions
pluginManagement {
    repositories {
        maven(url = "https://dl.bintray.com/kotlin/kotlin-eap") {
            name = "Kotlin EAP"
        }
        maven(url = "https://kotlin.bintray.com/kotlinx") {
            name = "kotlinx"
        }
        maven(url = "http://maven.modmuss50.me") {
            name = "modmuss50"
        }
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
//    resolutionStrategy {
//        eachPlugin {
//            if (requested.id.id == "kotlinx-serialization") {
//                useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
//            }
//        }
//    }
}
buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies.classpath("de.fayard.refreshVersions:refreshVersions:0.9.7")
}

bootstrapRefreshVersions(
    listOf(rootDir.resolve("dependencies-rules.txt").readText())
)

plugins {
    id("com.gradle.enterprise").version("3.5")
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
//        publishAlwaysIf(true)
    }
}

rootProject.name = "voodoo-parent"

includeBuild("buildUtil")
include(":wrapper")
include(":voodoo")
include(":core")
include(":dsl")
include(":format", ":format:packager")
include(":multimc", ":multimc:installer")
include(":util", ":util:download", ":util:maven", ":util:jenkins")
include(":tome", ":pack", ":pack:tester")
include(":server-installer")

//include(":plugin")

fun prefixProject(project: ProjectDescriptor, prefix: String) {
    project.name = prefix + "-" + project.name
    logger.lifecycle("path of ${project.name} is ${project.path}")
    project.children.forEach { child ->
        prefixProject(child, project.name)
    }
}

// TODO move into publishing
rootProject.children.forEach { child ->
    child.children.forEach { grandchild ->
        prefixProject(grandchild, child.name)
    }
}