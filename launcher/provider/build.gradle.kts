plugins {
    id("java")
    id("application")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}
val edcGroup: String by project
val edcVersion: String by project

repositories {
    mavenCentral()
}

dependencies{

    implementation("${edcGroup}:connector-core:${edcVersion}")
    implementation("${edcGroup}:http:${edcVersion}")
    implementation("${edcGroup}:dsp:${edcVersion}")
    implementation("${edcGroup}:management-api:${edcVersion}")

    implementation("${edcGroup}:auth-tokenbased:${edcVersion}")
    implementation("${edcGroup}:vault-hashicorp:${edcVersion}")
    implementation("${edcGroup}:iam-mock:${edcVersion}")
    implementation("${edcGroup}:api-observability:${edcVersion}")
    implementation("${edcGroup}:transfer-data-plane:${edcVersion}")
    implementation("${edcGroup}:configuration-filesystem:${edcVersion}")

    //Control Plane
    implementation("${edcGroup}:control-plane-core:${edcVersion}")
    implementation("${edcGroup}:control-plane-api-client:${edcVersion}")
    implementation("${edcGroup}:control-plane-api:${edcVersion}")
    implementation("${edcGroup}:control-api-configuration:${edcVersion}")

    //Data Plane
    implementation("${edcGroup}:data-plane-selector-api:${edcVersion}")
    implementation("${edcGroup}:data-plane-selector-core:${edcVersion}")
    implementation("${edcGroup}:data-plane-self-registration:${edcVersion}")
    implementation("${edcGroup}:data-plane-control-api:${edcVersion}")
    implementation("${edcGroup}:data-plane-public-api-v2:${edcVersion}")
    implementation("${edcGroup}:data-plane-core:${edcVersion}")
    implementation("${edcGroup}:data-plane-http:${edcVersion}")
    implementation("${edcGroup}:transfer-data-plane-signaling:${edcVersion}")

    implementation(project(":edc-ionos-nextcloud-extension:nextcloud-provision"))
    implementation(project(":edc-ionos-nextcloud-extension:nextcloud-dataplane"))

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}
application {
    mainClass.set("org.eclipse.edc.boot.system.runtime.BaseRuntime")
}
tasks.shadowJar {
    isZip64 = true
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    exclude("**/pom.properties", "**/pom.xm")
    mergeServiceFiles()
    archiveFileName.set("connector.jar")
}
