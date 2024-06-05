import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import org.springframework.boot.gradle.tasks.bundling.BootJar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    val kotlinVersion = "2.0.0"
    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    id("com.gorylenko.gradle-git-properties") version "2.4.2"
    jacoco
}

repositories {
    mavenCentral()
}

subprojects {

    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "com.gorylenko.gradle-git-properties")
    apply(plugin = "jacoco")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    repositories {
        mavenCentral()
        maven { url = uri("http://nexus.konadev.com/content/repositories/public"); isAllowInsecureProtocol = true }
        maven { url = uri("http://nexus.konadev.com/content/repositories/release"); isAllowInsecureProtocol = true }
        maven { url = uri("http://nexus.konadev.com/content/repositories/jcenter"); isAllowInsecureProtocol = true }
        maven { url = uri("http://nexus.konadev.com/content/repositories/thirdparty"); isAllowInsecureProtocol = true }
        maven { url = uri("http://nexus.konasldev.com:8082/repository/gradle-plugins/"); isAllowInsecureProtocol = true }
        maven { url = uri("http://nexus.konasldev.com:8082/repository/thirdparty"); isAllowInsecureProtocol = true }
    }

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
        implementation("org.springframework.boot:spring-boot-starter-web")
//        implementation("org.springframework.boot:spring-boot-starter-actuator")
//        implementation("org.springframework.boot:spring-boot-starter-undertow")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")

//        implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.4.0")
//        kapt("org.springframework.boot:spring-boot-configuration-processor")

        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

//        implementation("com.konasl.commonlibs:spring-web:7.0.1")
//        implementation("com.konasl.commonlibs:logger:7.0.1")
//        implementation("com.cubeone", "CubeOneAPI", "1.0.0")

        runtimeOnly("com.oracle.database.jdbc:ojdbc11")

        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(module = "junit")
            exclude(module = "mockito-core")
        }
        testImplementation("org.junit.jupiter:junit-jupiter-api")
        testImplementation("org.junit.jupiter:junit-jupiter-params")
        testImplementation("com.ninja-squad:springmockk:4.0.2")

        //kotest
        testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
        testImplementation("io.kotest:kotest-assertions-core:5.8.0")
        testImplementation("io.kotest:kotest-property:5.8.0")
        testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.3")

        testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine")
    }

    gitProperties {
        val primary = "${project.property("version.primary")}"
        val major = "${project.property("version.major")}"
        val minor = "${project.property("version.minor")}"

        val buildVersion = listOf(primary, major, minor).joinToString(".")
        val buildTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        println("buildVersion = $buildVersion")
        println("buildDateTime = $buildTime")

        customProperty("git.build.version", buildVersion)
        customProperty("git.build.time", buildTime)
    }

    tasks.withType(KotlinCompilationTask::class.java) {
        compilerOptions.freeCompilerArgs.add("-Xjsr305=strict")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        finalizedBy(tasks.jacocoTestReport)
    }

    jacoco {
        toolVersion = "0.8.11"
    }

    tasks.jacocoTestReport {
        dependsOn(tasks.test)
    }

    tasks.jacocoTestReport {
        reports {
            html.required = true
            xml.required = false
            csv.required = false
            html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
        }

        finalizedBy("jacocoTestCoverageVerification")
    }

    tasks.jacocoTestCoverageVerification {
        violationRules {
            rule {
                enabled = false
                limit {
                    minimum = "0.10".toBigDecimal()
                }
            }
        }
    }
}

project("module-api") {
    val bootJar: BootJar by tasks
    bootJar.enabled = true
    bootJar.archiveFileName.set("vam.jar")
}