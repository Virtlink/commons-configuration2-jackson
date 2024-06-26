// Workaround for IntelliJ issue where `libs` is errored: https://youtrack.jetbrains.com/issue/KTIJ-19369
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.nexuspublish)
    alias(libs.plugins.gitversion)
    alias(libs.plugins.versions)
    alias(libs.plugins.dependencycheck)
}

val gitVersion: groovy.lang.Closure<String> by extra

version = gitVersion()
group = "com.virtlink.commons"
description = "Adds support for FasterXML's Jackson to Apache Commons Configuration 2."

extra["isSnapshotVersion"] = version.toString().endsWith("-SNAPSHOT")
extra["isDirtyVersion"] = version.toString().endsWith(".dirty")
extra["isCI"] = !System.getenv("CI").isNullOrEmpty()

repositories {
    mavenCentral()
}

dependencies {
    api                 (libs.commons.configuration2)
    api                 (libs.jackson.databind)
    api                 (libs.commons.text) // Dependency override of commons.configuration2

    compileOnly         (libs.jsr305)

    testImplementation  (libs.junit)
    testImplementation  (libs.jackson.dataformat.yaml)
    testImplementation  (libs.guava)
    testImplementation  (libs.commons.beanutils)
}

configure<JavaPluginExtension> {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
    withSourcesJar()
    withJavadocJar()
}

allprojects {
    tasks {
        withType<JavaCompile> {
            options.compilerArgs.add("-Xlint:unchecked")
            options.compilerArgs.add("-Xlint:deprecation")
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set("Jackson for Commons Configuration 2")
                description.set(project.description)
                url.set("https://github.com/Virtlink/commons-configuration2-jackson")
                inceptionYear.set("2015")
                licenses {
                    // From: https://spdx.org/licenses/
                    license {
                        name.set("Apache-2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("virtlink")
                        name.set("Daniel A. A. Pelsmaeker")
                        email.set("d.a.a.pelsmaeker@tudelft.nl")
                    }
                }
                scm {
                    connection.set("scm:git@github.com:Virtlink/commons-configuration2-jackson.git")
                    developerConnection.set("scm:git@github.com:Virtlink/commons-configuration2-jackson.git")
                    url.set("scm:git@github.com:Virtlink/commons-configuration2-jackson.git")
                }
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Virtlink/commons-configuration2-jackson")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://oss.sonatype.org/content/repositories/snapshots/"))
            username.set(project.findProperty("ossrh.user") as String? ?: System.getenv("OSSRH_USERNAME"))
            password.set(project.findProperty("ossrh.token") as String? ?: System.getenv("OSSRH_TOKEN"))
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
    if (!project.hasProperty("signing.secretKeyRingFile")) {
        // If no secretKeyRingFile was set, we assume an in-memory key in the SIGNING_KEY environment variable (used in CI)
        useInMemoryPgpKeys(
            project.findProperty("signing.keyId") as String? ?: System.getenv("SIGNING_KEY_ID"),
            System.getenv("SIGNING_KEY"),
            project.findProperty("signing.password") as String? ?: System.getenv("SIGNING_KEY_PASSWORD"),
        )
    }
}

val checkNotDirty by tasks.registering {
    doLast {
        if (project.extra["isDirtyVersion"] as Boolean) {
            throw GradleException("Cannot publish a dirty version: ${project.version}")
        }
    }
}

tasks.publish { dependsOn(checkNotDirty) }

