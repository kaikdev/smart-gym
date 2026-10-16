plugins {
    kotlin("jvm") version "2.3.21"
    kotlin("plugin.spring") version "2.3.21"
    kotlin("plugin.jpa") version "2.3.21"
    id("org.springframework.boot") version "4.1.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.smartgym"
version = "0.0.1-SNAPSHOT"
description = "Backend do projeto Smart Gym"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Web (API REST)
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    // Spring Data JPA (acesso ao banco)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // Bean Validation (@NotBlank, @Positive, ...)
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // Spring Security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Suporte do Jackson (JSON) a classes Kotlin
    implementation("tools.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // JJWT - biblioteca para gerar e validar tokens JWT
    implementation("io.jsonwebtoken:jjwt-api:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")

    // Swagger / OpenAPI (documentação da API em /swagger-ui.html)
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.1.1")

    // Driver do MySQL
    runtimeOnly("com.mysql:mysql-connector-j")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

// Faz as classes @Entity serem "abertas" (exigência do Hibernate)
allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
