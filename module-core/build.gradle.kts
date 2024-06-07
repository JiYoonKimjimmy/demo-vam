dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("com.cubeone", "CubeOneAPI", "1.0.0")

    kapt("org.springframework.boot:spring-boot-configuration-processor")

    runtimeOnly("com.oracle.database.jdbc:ojdbc11")
}