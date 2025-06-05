import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.Configuration
import org.jooq.meta.jaxb.Database
import org.jooq.meta.jaxb.Generate
import org.jooq.meta.jaxb.GeneratedAnnotationType
import org.jooq.meta.jaxb.Generator
import org.jooq.meta.jaxb.Jdbc
import org.jooq.meta.jaxb.Target

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.liquibase.gradle") version "2.2.0"
    id("org.jooq.jooq-codegen-gradle") version "3.20.4"
}

group = "sfedu.net"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.5")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.4.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("org.jooq:jooq-codegen:3.20.4")
    implementation("org.jooq:jooq:3.20.2")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.3")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.3")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")
    liquibaseRuntime("org.liquibase:liquibase-core:4.31.0")
    liquibaseRuntime("org.postgresql:postgresql:42.6.0")
    liquibaseRuntime("info.picocli:picocli:4.6.1")
    implementation("org.apache.poi:poi:5.4.0")
    implementation("org.apache.poi:poi-ooxml:5.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")
    implementation("org.liquibase:liquibase-core:4.31.0")
    implementation("io.arrow-kt:arrow-core:1.2.4")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

sourceSets["main"].java.srcDir("${project.layout.buildDirectory.asFile.get()}/generated/source/jooq/src/main/java")


buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jooq:jooq-codegen:3.20.4")
        classpath("org.postgresql:postgresql:42.6.0")
        classpath("org.jooq:jooq:3.20.2")
        classpath("org.liquibase:liquibase-core:4.31.0")
    }
}

liquibase {
    activities.register("main") {
        this.arguments = mapOf(
            "logLevel" to "info",
            "changeLogFile" to "src/main/resources/db.changelog/main.xml",
            "url" to "jdbc:postgresql://localhost:5432/formator",
            "username" to "postgres",
            "password" to "dunice",
            "classpath" to "src/main/resources"
        )
    }

    runList = "main"
}

tasks.register("jooqGenerate") {
    group = "build"

    doLast {
        GenerationTool.generate(
            Configuration()
                .withJdbc(
                    Jdbc()
                        .withDriver("org.postgresql.Driver")
                        .withUrl("jdbc:postgresql://localhost:5432/formator")
                        .withUser("postgres")
                        .withPassword("dunice")
                )
                .withGenerator(
                    Generator()
                        .withDatabase(
                            Database()
                                .withInputSchema("formator")
                        )
                        .withGenerate(
                            Generate()
                                .withPojos(true)
                                .withSpatialTypes(false)
                                .withDaos(true)
                                .withGeneratedAnnotation(true)
                                .withGeneratedAnnotationType(GeneratedAnnotationType.JAVAX_ANNOTATION_PROCESSING_GENERATED)
                                .withPojosEqualsAndHashCode(true)
                        )
                        .withTarget(
                            Target()
                                .withPackageName("sfedu.net.formator.generated")
                                .withDirectory("${project.layout.buildDirectory.asFile.get()}/generated/source/jooq/src/main/java")
                        )
                )
        )

        println("jOOQ code generation finished")
    }
}

tasks.named("compileJava") {
    dependsOn("jooqGenerate")
}

tasks.named("compileKotlin") {
    dependsOn("jooqGenerate")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
