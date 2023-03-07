plugins {
    id("java")
    application
    id("jacoco")
}

group = "org.iitrpr"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    runtimeOnly("org.postgresql:postgresql:42.3.1")
    // https://mvnrepository.com/artifact/org.jacoco/org.jacoco.agent
    testImplementation("org.jacoco:org.jacoco.agent:0.8.8")

}
tasks.test{
    useJUnitPlatform()
}
tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}
application {
    mainClass.set("org.iitrpr.academicSystem");
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}






