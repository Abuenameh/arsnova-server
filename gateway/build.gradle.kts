import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  jacoco
  id("com.github.spotbugs")
  id("com.google.cloud.tools.jib")
  id("io.spring.dependency-management")
  id("org.jlleitschuh.gradle.ktlint")
  id("org.springframework.boot")
  kotlin("jvm")
  kotlin("plugin.spring")
}

java.sourceCompatibility = JavaVersion.VERSION_17

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.cloud:spring-cloud-starter-gateway")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.projectreactor:reactor-tools")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("com.github.vladimir-bukhtoyarov:bucket4j-core:${property("bucket4jVersion")}")
  implementation("com.auth0:java-jwt:${property("javaJwtVersion")}")
  implementation("io.micrometer:micrometer-registry-prometheus")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.security:spring-security-test")
  testImplementation("io.projectreactor:reactor-test")
  compileOnly("com.github.spotbugs:spotbugs-annotations:${property("spotbugsAnnotationsVersion")}")
}

dependencyManagement {
  imports {
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
  }
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "17"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.jib {
  jib {
    from {
      image = "eclipse-temurin:17-alpine"
    }
  }
}

tasks.jacocoTestReport {
  reports {
    csv.isEnabled = true
  }
}

spotbugs {
  excludeFilter.set(file("../spotbugs-exclude.xml"))
}

tasks.spotbugsTest {
  enabled = false
}
