plugins {
    id("java")
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("gui.LibraryApp")
}


repositories {
    mavenCentral()
}

dependencies {
    implementation("org.postgresql:postgresql:42.7.2")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}