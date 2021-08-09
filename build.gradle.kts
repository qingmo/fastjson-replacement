import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Calendar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import groovy.util.Node
import groovy.util.NodeList
import java.net.URI

val signingKeyId: String? by project
val signingPassword: String? by project
ext["signing.keyId"] = signingKeyId ?: "foo"
ext["signing.password"] = signingPassword ?: "bar"

plugins {
    val kotlinVersion = "1.5.10"
    java
    kotlin("jvm") version kotlinVersion
    `java-library`
    `maven-publish`
    id("com.github.hierynomus.license") version "0.15.0"
    `signing`
    `jacoco`
}

group = "io.github.qingmo"
version = "1.0.2"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    val jacksonModuleVersion = "2.12.3"
    implementation(kotlin("stdlib"))
    implementation("com.fasterxml.jackson.core:jackson-databind:${jacksonModuleVersion}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${jacksonModuleVersion}")
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    implementation("joda-time:joda-time:2.10.10")


}

tasks {
    jacocoTestReport {
        dependsOn("test")
        reports {
            xml.required.set(true)
            xml.isEnabled = true
            xml.destination = file("${buildDir}/reports/jacoco/report.xml")
            html.isEnabled = true
            csv.isEnabled = false
        }
    }

    withType<Wrapper> {
        gradleVersion = "6.8.3"
        distributionType = Wrapper.DistributionType.BIN
    }

    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.isDeprecation = true
    }

    withType<Test> {
        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
            events("passed", "failed", "skipped")
        }
        finalizedBy("jacocoTestReport")
    }

    withType<Jar> {
        dependsOn("licenseFormat", "generatePomFileForMavenJavaPublication")
        manifest {
            attributes(
                mapOf(
                    "Implementation-Title" to archiveBaseName,
                    "Implementation-Version" to archiveVersion,
                    "Built-Gradle" to gradle.gradleVersion,
                    "Bundle-DocURL" to "https://github.com/qingmo/fastjson-replacement",
                    "Build-OS" to System.getProperty("os.name"),
                    "Built-By" to System.getProperty("user.name"),
                    "Build-Timestamp" to LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                )
            )
        }

        into("META-INF/") {
            from(rootProject.file("LICENSE"))
        }

        into("META-INF/maven/${project.group}/${project.name}") {
            from("build/publications/mavenJava/")
            rename(".*", "pom.xml")
        }
    }

    whenTaskAdded {
        if (this.name.contains("signMavenJavaPublication")) {
            this.enabled = (ext["signing.keyId"] != "foo")
        }
    }

}

java {
    withSourcesJar()
    withJavadocJar()
}

jacoco {
    toolVersion = "0.8.7"
    reportsDirectory.set(layout.buildDirectory.dir("${buildDir}/reports/jacoco/"))
}

license {
    encoding = "UTF-8"
    header = rootProject.file("license.txt")
    includes(listOf("**/*.java", "**/*.kt"))
    excludes(
        listOf(
            "**/test/java/**/*.kt",
            "**/test/java/**/*.java",
            "**/*Test.java"
        )
    )
    mapping(
        mapOf(
            "java" to "SLASHSTAR_STYLE",
            "kt" to "SLASHSTAR_STYLE"
        )
    )
    ignoreFailures = true
    ext["year"] = Calendar.getInstance().get(Calendar.YEAR)
}

publishing {
    repositories {
        maven {
            val username: String? by project
            val password: String? by project
            val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            url = if ((project.version as String).endsWith("SNAPSHOT")) URI(snapshotsRepoUrl) else URI(releasesRepoUrl)
            credentials {
                setUsername(username)
                setPassword(password)
            }
        }

    }
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = rootProject.group as String
            artifactId = rootProject.name
            version = rootProject.version as String

            pom {
                name.set("fastjson-replacement")
                packaging = "jar"
                description.set("Fastjson-replacement is a Bridge Pattern to change  Fastjson implementation to Jackson implementation.")
                url.set("https://github.com/qingmo/fastjson-replacement")

                scm {
                    connection.set("scm:git@github.com:Codearte/gradle-nexus-staging-plugin.git")
                    developerConnection.set("scm:git@github.com:Codearte/gradle-nexus-staging-plugin.git")
                    url.set("https://github.com/qingmo/fastjson-replacement")
                }

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://mit-license.org/")
                    }
                }

                developers {
                    developer {
                        id.set("chaos")
                        name.set("qingmo")
                        email.set("eagleqingluo@gmail.com")
                    }
                }

                withXml {
                    val dependenciesNode: NodeList = asNode().get("dependencies") as NodeList
                    if (dependenciesNode.isNotEmpty()) {
                        (dependenciesNode.get(0) as Node).children().forEach {
                            val node = it as Node
                            val nodeList = node.get("scope") as NodeList
                            if (nodeList.isNotEmpty() && (nodeList[0] as Node).text() == "runtime") {
                                (nodeList[0] as Node).setValue("compile")
                            }

                        }
                    }
                }
            }


        }
    }
}

signing {
    sign(publishing.publications.get("mavenJava"))
}

