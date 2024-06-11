rootProject.name = "vam"

pluginManagement {
    repositories {
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("http://nexus.konasldev.com:8082/repository"); isAllowInsecureProtocol = true }
        maven { url = uri("http://nexus.konadev.com/content/repositories/central"); isAllowInsecureProtocol = true }
        maven { url = uri("http://nexus.konadev.com/content/repositories/gradle-plugin"); isAllowInsecureProtocol = true }
    }
}

include("module-core", "module-api", "module-batch")
