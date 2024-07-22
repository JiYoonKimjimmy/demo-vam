dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("com.linecorp.kotlin-jdsl:jpql-dsl:3.4.1")
    implementation("com.linecorp.kotlin-jdsl:jpql-render:3.4.1")
    implementation("com.linecorp.kotlin-jdsl:spring-data-jpa-support:3.4.1")

    implementation(files("libs/CubeOneAPI-1.0.0.jar"))

    kapt("org.springframework.boot:spring-boot-configuration-processor")

    runtimeOnly("com.oracle.database.jdbc:ojdbc11")
}

noArg {
    annotation("jakarta.persistence.Entity")
}