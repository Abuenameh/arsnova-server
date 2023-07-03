group = "net.particify.arsnova"
version = "0.0.1-SNAPSHOT"

extra["gitlabHost"] = "gitlab.com"

plugins {
  id("com.github.spotbugs") version "5.0.14" apply false
  id("com.google.cloud.tools.jib") version "3.3.2" apply false
  id("io.freefair.aspectj.post-compile-weaving") version "8.1.0" apply false
  id("org.jlleitschuh.gradle.ktlint") version "11.4.2" apply false
  id("org.springframework.boot") version "3.1.1" apply false
  kotlin("jvm") version "1.8.22" apply false
  kotlin("plugin.jpa") version "1.8.22" apply false
  kotlin("plugin.spring") version "1.8.22" apply false
}

subprojects {
  repositories {
    mavenCentral()
  }
}

tasks.register<Task>(name = "getDeps") {
  description = "Resolve and prefetch dependencies"
  doLast {
    rootProject.allprojects.forEach {
      it.buildscript.configurations.filter(Configuration::isCanBeResolved).forEach { it.resolve() }
      it.configurations.filter(Configuration::isCanBeResolved).forEach { it.resolve() }
    }
  }
}
