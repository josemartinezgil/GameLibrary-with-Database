plugins {
    kotlin("jvm") version "2.3.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // Para SQLite
    implementation("org.xerial:sqlite-jdbc:3.43.0.0")
    // Para MySQL
    implementation("com.mysql:mysql-connector-j:9.7.0")

}


kotlin {
    jvmToolchain(24)
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaExec> {
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}