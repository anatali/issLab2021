import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.4.5"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.4.32"
	kotlin("plugin.spring") version "1.4.32"
	java
}

group = "it.unibo"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	jcenter() 	//required by andrea pivetta
	flatDir{ dirs("../unibolibs")   }   //Our libraries
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	//See https://mkyong.com/spring-boot/intellij-idea-spring-boot-template-reload-is-not-working/
	/* INTELLIJ
	File –> Setting –> Build, Execution, Deployment –> Compiler –> check this Build project automatically
	SHIFT+CTRL+A registry | compiler.automake.allow.when.app.running
	If the static files are not reloaded, press CTRL+F9 to force a reload.
	 */
	testImplementation("org.springframework.boot:spring-boot-starter-test")
//KOTLIN

	// Align versions of all Kotlin components
	implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

	// Use the Kotlin JDK 8 standard library.
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	// This dependency is used by the application.
	implementation("com.google.guava:guava:29.0-jre")

	//COROUTINE
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.1.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.0")


//JSON
	// https://mvnrepository.com/artifact/org.json/json
	implementation("org.json:json:20201115" )

	//OkHttp library for websockets with Kotlin
	implementation("com.squareup.okhttp3:okhttp:4.9.0")

//PLANNER aimacode
// https://mvnrepository.com/artifact/com.googlecode.aima-java/aima-core
	implementation("com.googlecode.aima-java:aima-core:3.0.0")

//STRING COLORS
	// https://mvnrepository.com/artifact/com.andreapivetta.kolor/kolor
	implementation( "com.andreapivetta.kolor:kolor:1.0.0" )

//UNIBO
	implementation("IssActorKotlinRobotSupport:IssActorKotlinRobotSupport:2.0")
	implementation("uniboIssSupport:IssWsHttpJavaSupport:1.0")
	implementation("uniboInterfaces:uniboInterfaces")
	implementation("uniboProtocolSupport:unibonoawtsupports")
	//implementation("uniboplanner20:it.unibo.planner20:1.0")



}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
