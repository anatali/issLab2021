/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/6.7.1/userguide/building_java_projects.html
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    id("org.jetbrains.kotlin.jvm") version "1.4.32"
}

repositories {
    // Use JCenter for resolving dependencies.
    jcenter()
    flatDir{ dirs("../../unibolibs")   }   //Our libraries
}

dependencies {
    // Use JUnit test framework.
    testImplementation("junit:junit:4.13")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:29.0-jre")

    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:29.0-jre")

    //COROUTINE
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.0")

    //OkHttp library for websockets with Kotlin
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    //Ktor is a framework for quickly creating web applications in Kotlin with minimal effort.
    //implementation("io.ktor:ktor:1.5.1")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

//JSON
    // https://mvnrepository.com/artifact/org.json/json
    implementation("org.json:json:20201115")
    // https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple
    //implementation("com.googlecode.json-simple:json-simple:1.1.1")

//COAP
    // https://mvnrepository.com/artifact/org.eclipse.californium/californium-core
    implementation("org.eclipse.californium:californium-core:2.0.0-M12")
    // https://mvnrepository.com/artifact/org.eclipse.californium/californium-proxy
    implementation("org.eclipse.californium:californium-proxy:2.0.0-M12")
//LOG4j
    implementation("org.slf4j:slf4j-log4j12:1.7.25")


//SOCKET.IO
    //implementation("com.github.nkzawa:socket.io-client:0.6.0")
// https://mvnrepository.com/artifact/javax.websocket/javax.websocket-api
    implementation("javax.websocket:javax.websocket-api:1.1")   //javax.websocket api is only the specification
    implementation("org.glassfish.tyrus.bundles:tyrus-standalone-client:1.9")

//PLANNER aimacode
// https://mvnrepository.com/artifact/com.googlecode.aima-java/aima-core
    implementation("com.googlecode.aima-java:aima-core:3.0.0")

//STRING COLORS
    implementation( "com.andreapivetta.kolor:kolor:1.0.0" )

//SPRING (client)
    // https://mvnrepository.com/artifact/org.springframework/spring-web
    implementation("org.springframework:spring-web:5.3.6")

//HTTP CLIENT
    // https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
    implementation ("org.apache.httpcomponents:httpclient:4.5.13")
    // https://mvnrepository.com/artifact/commons-io/commons-io
    implementation ("commons-io:commons-io:2.6")

//UNIBO
    implementation("uniboIssActorKotlin:IssActorKotlinRobotSupport:2.0")
    implementation("uniboIssSupport:IssWsHttpJavaSupport:1.0")
    implementation("uniboInterfaces:uniboInterfaces")
    implementation("uniboProtocolSupport:unibonoawtsupports")
    implementation("tuprolog:2p301")
    implementation("qak:it.unibo.qakactor:2.4")
    //RADAR
    implementation("radar:radarPojo")
    //RADAR GUI
    // https://mvnrepository.com/artifact/org.pushingpixels/trident
    implementation("org.pushingpixels:trident:1.3")

    //implementation("uniboplanner20:it.unibo.planner20:1.0")
}


application {
    // Define the main class for the application.
    mainClass.set("it.unibo.radarAsResource.App")
}