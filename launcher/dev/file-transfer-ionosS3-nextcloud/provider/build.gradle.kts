plugins {
    id("java")
    id("application")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}
val edcGroup: String by project
val edcVersion: String by project
val ionosGroup: String by project
val ionosVersion: String by project


repositories {
    maven {
        url = uri("https://maven.pkg.github.com/Digital-Ecosystems/edc-ionos-s3/")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME_GITHUB")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN_GITHUB")
        }
    }
    mavenCentral()
}

dependencies{

    implementation("${edcGroup}:control-plane-core:${edcVersion}")
    implementation("${edcGroup}:control-plane-api-client:${edcVersion}")
    implementation("${edcGroup}:control-plane-api:${edcVersion}")
    implementation("${edcGroup}:api-observability:${edcVersion}")
    implementation("${edcGroup}:data-plane-client:${edcVersion}")
    implementation("${edcGroup}:data-plane-selector-client:${edcVersion}")
    implementation("${edcGroup}:data-plane-selector-core:${edcVersion}")
    implementation("${edcGroup}:data-plane-selector-api:${edcVersion}")
    implementation("${edcGroup}:configuration-filesystem:${edcVersion}")
    implementation("${edcGroup}:transfer-data-plane:${edcVersion}")
    implementation("${edcGroup}:http:${edcVersion}")
    implementation("${edcGroup}:dsp:${edcVersion}")
    implementation("${edcGroup}:auth-tokenbased:${edcVersion}")
    implementation("${edcGroup}:management-api:${edcVersion}")
    implementation("${edcGroup}:vault-hashicorp:${edcVersion}")
    implementation("${edcGroup}:iam-mock:${edcVersion}")

    implementation ("${ionosGroup}:provision-ionos-s3:${ionosVersion}")
    implementation(project(":edc-ionos-nextcloud-extension:nextcloud-dataplane"))
    implementation ("${ionosGroup}:data-plane-ionos-s3:${ionosVersion}")
    implementation(project(":edc-ionos-nextcloud-extension:nextcloud-provision"))


   // implementation("org.postgresql:postgresql:$postgresVersion")
    implementation("${edcGroup}:sql-pool-apache-commons:$edcVersion")
    implementation("${edcGroup}:transaction-local:$edcVersion")
    implementation("${edcGroup}:transaction-datasource-spi:$edcVersion")

    implementation("${edcGroup}:asset-index-sql:$edcVersion")
    implementation("${edcGroup}:policy-definition-store-sql:$edcVersion")
    implementation("${edcGroup}:contract-definition-store-sql:$edcVersion")
    implementation("${edcGroup}:contract-negotiation-store-sql:$edcVersion")
    implementation("${edcGroup}:transfer-process-store-sql:$edcVersion")



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
    archiveFileName.set("dataspace-connector.jar")
}