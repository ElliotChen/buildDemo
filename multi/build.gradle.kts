import org.jetbrains.kotlin.utils.addToStdlib.constant

plugins {
    java
    kotlin("jvm") version "1.4.10" apply false
}

allprojects {
    group = "tw.elliot"
    version = "1.0"

    repositories {
        jcenter()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")

    dependencies {
        compileOnly("javax.servlet:javax.servlet-api:4.0.1")
        implementation("com.google.guava:guava:28.0-jre")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

tasks.forEach {
    it.enabled = false
}