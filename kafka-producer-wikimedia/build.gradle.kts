plugins {
    id("java")
}

group = "com.conduktor.demos"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // SLF4J dependencies for logging
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("org.slf4j:slf4j-simple:1.7.36")

    // https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    // https://mvnrepository.com/artifact/com.launchdarkly/okhttp-eventsource
    implementation ("com.launchdarkly:okhttp-eventsource:2.5.0")

    // Kafka client library
    implementation("org.apache.kafka:kafka-clients:3.1.0")





    // JUnit dependencies for testing
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}