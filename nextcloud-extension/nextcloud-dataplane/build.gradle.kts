plugins {
    id("java")
}

val edcGroup: String by project
val edcVersion: String by project
repositories {
    mavenCentral()
}

dependencies {
    implementation("${edcGroup}:data-plane-spi:${edcVersion}")
    implementation("${edcGroup}:util:${edcVersion}")
    implementation("${edcGroup}:transfer-spi:${edcVersion}")
    implementation("${edcGroup}:data-plane-util:${edcVersion}")
    implementation("${edcGroup}:data-plane-core:${edcVersion}")
    implementation("${edcGroup}:http:${edcVersion}")

    implementation(project(":nextcloud-extension:nextcloud-core"))
    implementation("com.google.code.gson:gson:2.8.6")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}