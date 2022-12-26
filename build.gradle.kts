import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.zaproxy.gradle.addon.AddOnStatus
import org.zaproxy.gradle.addon.internal.model.GitHubUser
import org.zaproxy.gradle.addon.internal.model.ProjectInfo
import org.zaproxy.gradle.addon.internal.model.ReleaseState
import org.zaproxy.gradle.addon.internal.tasks.GenerateReleaseStateLastCommit
import org.zaproxy.gradle.addon.misc.ConvertMarkdownToHtml


plugins {
    kotlin("jvm") version "1.7.20"
    id("org.zaproxy.add-on") version "0.8.0"
    id("com.diffplug.spotless") version "6.11.0"
}

group = "ai.levo"
description = "Build OpenAPI Specs with ZAP traffic using Levo.ai."

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10")
    implementation("org.apache.logging.log4j:log4j-api-kotlin:1.2.0")
    testImplementation(kotlin("test"))
}

zapAddOn {
    addOnName.set("Levo.ai")
    addOnStatus.set(AddOnStatus.ALPHA)
    zapVersion.set("2.12.0")

    releaseLink.set("https://github.com/levoai/levoai-zap-addon/compare/v@PREVIOUS_VERSION@...v@CURRENT_VERSION@")
    unreleasedLink.set("https://github.com/levoai/levoai-zap-addon/compare/v@CURRENT_VERSION@...HEAD")

    manifest {
        author.set("Levo.ai")
        url.set("https://levo.ai")
        repo.set("https://github.com/levoai/levoai-zap-addon")
        changesFile.set(tasks.named<ConvertMarkdownToHtml>("generateManifestChanges").flatMap { it.html })

        helpSet {
            baseName.set("help%LC%.helpset")
            localeToken.set("%LC%")
        }
    }

    gitHubRelease {
        user.set(GitHubUser("levobot", "github-bot@levo.ai", System.getenv("LEVOBOT_TOKEN")))
    }
}

spotless {
    kotlin {
        licenseHeaderFile("gradle/spotless/license.kt")
        ktlint()
    }

    kotlinGradle {
        ktlint()
    }
}

tasks.prepareNextDevIter {
    mustRunAfter(tasks.createRelease)
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

val projectInfo = ProjectInfo.from(project)
val generateReleaseStateLastCommit by tasks.registering(GenerateReleaseStateLastCommit::class) {
    projects.set(listOf(projectInfo))
}

val releaseAddOn by tasks.registering {
    if (ReleaseState.read(projectInfo).isNewRelease()) {
        dependsOn(tasks.createRelease)
        dependsOn(tasks.createPullRequestNextDevIter)
    }
}
