plugins {
    id("java")
    id("maven-publish")
}

val edcGroup: String by project
val edcVersion: String by project

val extensionsGroup: String by project
val extensionsVersion: String by project
val gitHubPkgsName: String by project
val gitHubPkgsUrl: String by project
val gitHubUser: String? by project
val gitHubToken: String? by project

repositories {
    mavenCentral()
}

dependencies {

    implementation("${edcGroup}:transfer-spi:${edcVersion}")
    implementation("dev.failsafe:failsafe:3.2.4")

    implementation(project(":edc-ionos-nextcloud-extension:nextcloud-core"))
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = extensionsGroup
            artifactId = "provision-ionos-nextcloud"
            version = extensionsVersion

            from(components["java"])

            pom {
                name.set("provision-ionos-nextcloud")
                description.set("Extension to manage an Nextcloud")
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/${project.properties["github_owner"]}/${project.properties["github_repo"]}")

            credentials {
                username = gitHubUser
                password = gitHubToken
            }
        }
    }
}