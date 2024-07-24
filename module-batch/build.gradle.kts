plugins {
    id("java-test-fixtures")
}

dependencies {
    implementation(project(":module-api"))
    implementation(project(":module-core"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    val jdslVersion = "3.4.1"
    implementation("com.linecorp.kotlin-jdsl:jpql-dsl:$jdslVersion")
    implementation("com.linecorp.kotlin-jdsl:jpql-render:$jdslVersion")
    implementation("com.linecorp.kotlin-jdsl:spring-data-jpa-support:$jdslVersion")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.5.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    testImplementation("org.springframework.batch:spring-batch-test")

    testImplementation(testFixtures(project(":module-core")))

    runtimeOnly("com.oracle.database.jdbc:ojdbc11")
}