plugins {
    `java-library`
    id("application")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}
val edcGroup: String by project
val edcVersion: String by project
repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(project(":launcher:base:connector"))

    implementation("${edcGroup}:configuration-filesystem:${edcVersion}")
    implementation("${edcGroup}:vault-hashicorp:${edcVersion}")
    implementation("${edcGroup}:iam-mock:${edcVersion}")


}

application {
    mainClass.set("org.eclipse.edc.boot.system.runtime.BaseRuntime")
}


tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    exclude("**/pom.properties", "**/pom.xm")
    mergeServiceFiles()
    archiveFileName.set("dataspace-connector.jar")
    destinationDirectory.set(layout.buildDirectory.dir("libs"))
}
