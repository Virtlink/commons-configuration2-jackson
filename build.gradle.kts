plugins {
    `java-library`
    `maven-publish`
    signing
    id("com.github.ben-manes.versions") version "0.42.0"
}

version = "0.11.0"
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

            // Use gradle.properties file:
            val pomProjectName: String by project
            val pomInceptionYear: String by project
            val pomUrl: String by project
            val pomIssueUrl: String by project

            val pomLicenseName: String by project
            val pomLicenseId: String by project
            val pomLicenseUrl: String by project
            val pomLicenseDist: String by project

            val pomDeveloperId: String by project
            val pomDeveloperName: String by project
            val pomDeveloperEmail: String by project

            val pomScmUrl: String by project
            val pomScmConnection: String by project
            val pomScmDevConnection: String by project

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
                        password = System.getenv("OSSRH_PASSWORD")
                    }
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}