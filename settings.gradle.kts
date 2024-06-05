rootProject.name = "vam"

include(":module-api")

pluginManagement {
    repositories {
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
}
