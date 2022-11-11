plugins {
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.gitversion)
    alias(libs.plugins.versions)
    alias(libs.plugins.dependencycheck)
}

val gitVersion: groovy.lang.Closure<String> by extra

version = gitVersion()
group = "com.virtlink.commons"
description = "Adds support for FasterXML's Jackson to Apache Commons Configuration 2."

repositories {
    mavenCentral()
}

dependencies {
    api                 (libs.commons.configuration2)
    api                 (libs.jackson.databind)

    compileOnly         (libs.findbugs)

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
            repositories {
                maven {
                    name = "OSSRH"
                    url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                    credentials {
                        username = System.getenv("OSSRH_USERNAME")
                        password = System.getenv("OSSRH_TOKEN")
                    }
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}
