import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

dependencies {
    implementation(project(":module-core"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
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
