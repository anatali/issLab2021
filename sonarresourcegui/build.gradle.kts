import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.0"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.10"
	kotlin("plugin.spring") version "1.5.10"
	java
	//application
	//jacoco
	//distribution
}

group = "it.unibo"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	jcenter() 	//required by andrea pivetta
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
//Webjars See
	implementation("org.webjars:webjars-locator-core")
	implementation("org.webjars:sockjs-client:1.0.2")
	implementation("org.webjars:stomp-websocket:2.3.3")
	implementation("org.webjars:bootstrap:3.3.7")
	implementation("org.webjars:jquery:3.1.1-1")

//JSON
	// https://mvnrepository.com/artifact/org.json/json
	implementation("org.json:json:20201115" )

//COAP
	// https://mvnrepository.com/artifact/org.eclipse.californium/californium-core
	//FOR A MORE RECENT VERSION, WE MUST INTRODUCE SOME Exception handling in the code
	implementation("org.eclipse.californium:californium-core:2.0.0-M12")
	// https://mvnrepository.com/artifact/org.eclipse.californium/californium-proxy
	implementation("org.eclipse.californium:californium-proxy:2.0.0-M12")

//STRING COLORS
	// https://mvnrepository.com/artifact/com.andreapivetta.kolor/kolor
	implementation( "com.andreapivetta.kolor:kolor:1.0.0" )

//UNIBO
	implementation("IssActorKotlinRobotSupport:IssActorKotlinRobotSupport:2.0")
	implementation("uniboIssSupport:IssWsHttpJavaSupport:1.0")
	implementation("uniboInterfaces:uniboInterfaces")
	implementation("uniboProtocolSupport:unibonoawtsupports")
	implementation("uniboplanner20:it.unibo.planner20:1.0")
	implementation("qak:it.unibo.qakactor:2.4")



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
