plugins {
    id("java")
}

val edcGroup: String by project
val edcVersion: String by project


repositories {
    mavenCentral()
}

dependencies {

    implementation("${edcGroup}:transfer-spi:${edcVersion}")
    implementation("${edcGroup}:boot:${edcVersion}")
    implementation("${edcGroup}:connector-core:${edcVersion}")
    implementation("${edcGroup}:core-spi:${edcVersion}")
    implementation("${edcGroup}:http:${edcVersion}")
    implementation("${edcGroup}:control-plane-core:${edcVersion}")

    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("org.jsoup:jsoup:1.14.3")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}