plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
}

// Versions
val jacksonVersion = "2.13.0"
val commonsConfigurationVersion = "2.7"
val findbugsVersion = "3.0.1"
val junitVersion = "4.13.2"
val guavaVersion = "31.0.1-jre"
val beanutilsVersion = "1.9.4"

dependencies {
    api                 ("org.apache.commons:commons-configuration2:$commonsConfigurationVersion")
    api                 ("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")

    compileOnly         ("com.google.code.findbugs:findbugs:$findbugsVersion")

    testImplementation  ("junit:junit:$junitVersion")
    testImplementation  ("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    testImplementation  ("com.google.guava:guava:$guavaVersion")
    testImplementation  ("commons-beanutils:commons-beanutils:$beanutilsVersion")
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

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

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components["java"])

            pom {
                name.set(pomProjectName)
                description.set(project.description)
                url.set(pomUrl)
                inceptionYear.set(pomInceptionYear)
                licenses {
                    license {
                        name.set(pomLicenseId)
                        url.set(pomLicenseUrl)
                        distribution.set(pomLicenseDist)
                    }
                }
                developers {
                    developer {
                        id.set(pomDeveloperId)
                        name.set(pomDeveloperName)
                        email.set(pomDeveloperEmail)
                    }
                }
                scm {
                    connection.set(pomScmConnection)
                    developerConnection.set(pomScmDevConnection)
                    url.set(pomScmUrl)
                }
            }

        }
    }
}

