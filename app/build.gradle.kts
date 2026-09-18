import net.ltgt.gradle.errorprone.errorprone

plugins {
    application

    id("net.ltgt.errorprone") version "5.1.1"
    id("com.diffplug.spotless") version "8.10.2"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    errorprone("com.google.errorprone:error_prone_core:2.50.0")
    compileOnly("com.google.errorprone:error_prone_annotations:2.50.0")

    errorprone("com.uber.nullaway:nullaway:0.14.1")
    implementation("org.jspecify:jspecify:1.0.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(26)
    }
}

application {
    mainClass = "Main"
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Xlint:all,-serial")
    options.compilerArgs.add("-Werror")

    options.errorprone {
        allDisabledChecksAsWarnings = true

        error("NullAway")
        option("NullAway:OnlyNullMarked", true)
        option("NullAway:JSpecifyMode", true)

        disable("AddNullMarkedToClass") // We have "AddNullMarkedToPackageInfo".
        disable("SystemOut")
        disable("DefaultPackage") // Only for the Main class
    }
}

spotless {
    java {
        googleJavaFormat("1.36.1")
            .aosp()
            .reflowLongStrings()
            .reorderImports(true)
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
