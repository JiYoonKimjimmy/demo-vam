dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    kapt("org.springframework.boot:spring-boot-configuration-processor")

    runtimeOnly("com.oracle.database.jdbc:ojdbc11")
}

noArg {
    annotation("jakarta.persistence.Entity")
}