plugins {
    id("java")
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

val edcGroup: String by project
val edcVersion: String by project

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("${edcGroup}:boot:${edcVersion}")

    implementation("${edcGroup}:control-plane-core:${edcVersion}")

    implementation("${edcGroup}:api-observability:${edcVersion}")

    implementation("${edcGroup}:configuration-filesystem:${edcVersion}")

    implementation("${edcGroup}:http:${edcVersion}")
    implementation("${edcGroup}:dsp:${edcVersion}")

    implementation("${edcGroup}:auth-tokenbased:${edcVersion}")

    implementation("$edcGroup:management-api:$edcVersion")

    implementation("${edcGroup}:iam-mock:${edcVersion}")


    testImplementation ("${edcGroup}:junit:${edcVersion}")


    implementation("${edcGroup}:data-plane-selector-core:${edcVersion}")

    implementation("${edcGroup}:data-plane-client:${edcVersion}")

    implementation("${edcGroup}:transfer-data-plane:${edcVersion}")

    implementation("${edcGroup}:data-plane-selector-client:${edcVersion}")

    implementation(project(":edc-ionos-nextcloud-extension:nextcloud-dataplane"))
    implementation(project(":edc-ionos-nextcloud-extension:nextcloud-provision"))
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}

application {
    mainClass.set("org.eclipse.edc.boot.system.runtime.BaseRuntime")
}


tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    exclude("**/pom.properties", "**/pom.xm")
    mergeServiceFiles()
    archiveFileName.set("dataspace-connector.jar")
}