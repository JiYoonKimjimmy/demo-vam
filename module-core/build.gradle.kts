dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    val jdslVersion = "3.4.1"
    implementation("com.linecorp.kotlin-jdsl:jpql-dsl:$jdslVersion")
    implementation("com.linecorp.kotlin-jdsl:jpql-render:$jdslVersion")
    implementation("com.linecorp.kotlin-jdsl:spring-data-jpa-support:$jdslVersion")

    kapt("org.springframework.boot:spring-boot-configuration-processor")

    runtimeOnly("com.oracle.database.jdbc:ojdbc11")

    testImplementation("it.ozimov:embedded-redis:0.7.2")
}

noArg {
    annotation("jakarta.persistence.Entity")
}