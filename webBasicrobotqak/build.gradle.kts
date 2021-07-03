import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.1"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.10"
	kotlin("plugin.spring") version "1.5.10"
	java
	application
	jacoco
	distribution
}

group = "it.unibo"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_1_8 //JavaVersion.VERSION_11

repositories {
	mavenCentral()
	jcenter() 	//required by andrea pivetta	JFrog shutdown in February 2021
	flatDir{ dirs("../unibolibs")   }   //Our libraries
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")


//Webjars See https://www.baeldung.com/maven-webjars
//WebJars have nothing to do with Spring
	implementation("org.webjars:webjars-locator-core")
	implementation("org.webjars:sockjs-client:1.0.2")
	implementation("org.webjars:stomp-websocket:2.3.3")
	implementation("org.webjars:bootstrap:3.3.7")
	implementation("org.webjars:jquery:3.1.1-1")

	// This dependency is used by the application.
	implementation("com.google.guava:guava:29.0-jre")
//COROUTINE
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.1.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.0")

//MQTT
// https://mvnrepository.com/artifact/org.eclipse.paho/org.eclipse.paho.client.mqttv3
	implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.1")

//JSON
	// https://mvnrepository.com/artifact/org.json/json
	implementation("org.json:json:20201115" )

//COAP
	// https://mvnrepository.com/artifact/org.eclipse.californium/californium-core
	//FOR A MORE RECENT VERSION, WE MUST INTRODUCE SOME Exception handling in the code
	implementation("org.eclipse.californium:californium-core:2.0.0-M12")
	// https://mvnrepository.com/artifact/org.eclipse.californium/californium-proxy
	implementation("org.eclipse.californium:californium-proxy:2.0.0-M12")


//OkHttp library for websockets with Kotlin
	implementation( "com.squareup.okhttp3:okhttp:4.9.0" )

//ADDED FOR THE HTTP CLIENT
	// https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
	implementation ("org.apache.httpcomponents:httpclient:4.5")
	// https://mvnrepository.com/artifact/commons-io/commons-io
	implementation ("commons-io:commons-io:2.6")
 	//implementation("com.googlecode.aima-java:aima-core:3.0.0")

//STRING COLORS
	// https://mvnrepository.com/artifact/com.andreapivetta.kolor/kolor
	implementation( "com.andreapivetta.kolor:kolor:1.0.0" )

//UNIBO
	implementation("IssActorKotlinRobotSupport:IssActorKotlinRobotSupport:2.0")
	//implementation("uniboIssSupport:IssWsHttpJavaSupport:1.0")
	implementation("uniboInterfaces:uniboInterfaces")
	implementation("uniboProtocolSupport:unibonoawtsupports")
	//implementation("uniboplanner20:it.unibo.planner20:1.0")
	implementation("qak:it.unibo.qakactor:2.4")
	implementation("tuprolog:2p301")
	//implementation("basicrobot:it.unibo.qak21.basicrobot:1.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}



application {
	// Define the main class for the application.
	mainClass.set("it.unibo.webBasicrobotqak.WebBasicrobotqakApplicationKt")
}

version = "1.0"

tasks.jar {
	manifest {
		attributes["Main-Class"] = "it.unibo.webspring.demo.ApplicationKt"
		attributes(mapOf("Implementation-Title" to project.name,
			"Implementation-Version" to project.version))
	}
}