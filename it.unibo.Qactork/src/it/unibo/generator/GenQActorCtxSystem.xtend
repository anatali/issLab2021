package it.unibo.generator

import it.unibo.qactork.QActorSystemSpec
import it.unibo.qactork.generator.common.SysKb
import it.unibo.qactork.Context
import it.unibo.qactork.generator.common.GenUtils

class GenQActorCtxSystem {
	def void doGenerate(QActorSystemSpec system, Context ctx, SysKb kb) {
		println(" *** GenQActorCtxSystem starts for " + system.name + " ctx=" + ctx.name )
		
		val sysName 	     = kb.getActorSystemName().toLowerCase ;
		val ctxClassName     = ctx.name.toFirstUpper ;
		val mainClassName    = "Main"+ctxClassName;
		val packageName      = GenUtils.packageName ;

		//Generate the context-specific
		GenUtils.genFileDir( "../src/", GenUtils.packageName,  mainClassName,  "kt", 
										genCtxQActor(sysName, packageName, mainClassName, ctx  ) )	
		GenUtils.genFileDir( "../src/", "",  "log4j",  "properties", genLogProperties( ) )	
		//if( ! SysKb.existFile( "../build2022.gradle" ) ){
			GenUtils.genFileDir(  "..", "", "build2022", "gradle", genBuildGradle(mainClassName,ctx,false ) )
			GenUtils.genFileDir(  "..", "", ".gitignore", "",      genBuildGitIgnore() ) 
//genSettingsGradle spostato in GenQActorSystem			
			//GenUtils.genFileDir(  "..", "", "settings", "gradle",  genSettingsGradle(system.name) ) 	
			//GenUtils.genFileDir(  "..", "", "settings", "gradle",  genSettingsGradle(GenUtils.packageName)	)	
		//}
		if( ! SysKb.existFile( "../gradle.properties" ) ){
			GenUtils.genFileDir(  "..", "", "gradle", "properties",genGradleProperties() )
		}
//		if( ! SysKb.existFile( "../build_"+ctx.name+".gradle" ) ){
//genSettingsGradle spostato in GenQActorSystem		
//			GenUtils.genFileDir(  "..", "", "settings", "gradle",  genSettingsGradle(system.name) ) 			
//			GenUtils.genFileDir(  "..", "", "build_"+ctx.name, "gradle",  genBuildGradle(mainClassName,ctx,false ) )
//			GenUtils.genFileDir(  "..", "", "build_"+ctx.name, "gradle.kts",  genBuildGradleKts(mainClassName,ctx,false ) ) //2021
//		}
	}
	
/*
 * Log properties
 */
def genLogProperties()'''
# Root logger option
#### log4j.rootLogger=INFO, stdout 
log4j.rootLogger=INFO, file

# Direct log messages to a log file
log4j.appender.file=org.apache.log4j.RollingFileAppender
log4j.appender.file.File=log4jInfo.log
log4j.appender.file.MaxFileSize=10MB
log4j.appender.file.MaxBackupIndex=10
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n

# Direct log messages to stdout
### log4j.appender.stdout=org.apache.log4j.ConsoleAppender
### log4j.appender.stdout.Target=System.out
### log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
### log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n
'''
/*
 * ACTOR CONTEXT
 */	
def  genCtxQActor( String sysName, String packageName, String className, Context ctx  )'''
	«GenUtils.logo»
	package «packageName»
	import it.unibo.kactor.QakContext
	import it.unibo.kactor.sysUtil
	import kotlinx.coroutines.runBlocking
	
	fun main() = runBlocking {
		QakContext.createContexts(
		        "«ctx.ip.host»", this, "«sysName».pl", "sysRules.pl"
		)
	}
	
'''


def genBuildGradle(String className, Context ctx, boolean ddr)''' 
/*
================================================================================
build.gradle
GENERATED ONLY ONCE
================================================================================
*/
plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    id 'application'
    id 'java'
    id 'eclipse'

    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id 'org.jetbrains.kotlin.jvm' version "$kotlinVersion"
}

version '1.0'
sourceCompatibility = 11

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    flatDir {   dirs '../unibolibs'	 }
}

dependencies {
    // Align versions of all Kotlin components
    implementation platform('org.jetbrains.kotlin:kotlin-bom')

    // Use the Kotlin JDK 8 standard library.
    implementation 'org.jetbrains.kotlin:kotlin-stdlib-jdk8'

    // This dependency is used by the application.
    implementation 'com.google.guava:guava:30.1.1-jre'

    // Use the Kotlin test library.
    testImplementation 'org.jetbrains.kotlin:kotlin-test'

    // Use the Kotlin JUnit integration.
    testImplementation 'org.jetbrains.kotlin:kotlin-test-junit'

    /* COROUTINES ********************************************************************************************************** */
    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
    implementation group: 'org.jetbrains.kotlinx', name: 'kotlinx-coroutines-core', version: "$kotlinVersion", ext: 'pom'
    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core-jvm
    implementation group: 'org.jetbrains.kotlinx', name: 'kotlinx-coroutines-core-jvm', version: "$kotlinVersion"

    /*  MQTT *************************************************************************************************************** */
    // https://mvnrepository.com/artifact/org.eclipse.paho/org.eclipse.paho.client.mqttv3
    implementation group: 'org.eclipse.paho', name: 'org.eclipse.paho.client.mqttv3', version: '1.2.5'

    /* JSON **************************************************************************************************************** */
    // https://mvnrepository.com/artifact/org.json/json
    implementation group: 'org.json', name: 'json', version: '20220320'

    /* COAP **************************************************************************************************************** */
    // https://mvnrepository.com/artifact/org.eclipse.californium/californium-core
    implementation group: 'org.eclipse.californium', name: 'californium-core', version: '3.5.0'
    // https://mvnrepository.com/artifact/org.eclipse.californium/californium-proxy2
    implementation group: 'org.eclipse.californium', name: 'californium-proxy2', version: '3.5.0'

//OkHttp library for websockets with Kotlin
	//implementation group: 'com.squareup.okhttp3', name: 'okhttp', version: '3.14.0'    
	implementation group: 'com.squareup.okhttp3', name: 'okhttp', version: '4.9.3' 
	// https://mvnrepository.com/artifact/com.squareup.okhttp3/mockwebserver
	testImplementation group: 'com.squareup.okhttp3', name: 'mockwebserver', version: '4.9.3'

    /* LOG4J *************************************************************************************************************** */
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-reload4j
    implementation group: 'org.slf4j', name: 'slf4j-reload4j', version: '2.0.0-alpha7'

    /* HTTP **************************************************************************************************************** */
    // https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
    implementation group: 'org.apache.httpcomponents', name: 'httpclient', version: '4.5.13'
    // https://mvnrepository.com/artifact/commons-io/commons-io
    implementation group: 'commons-io', name: 'commons-io', version: '2.11.0'

    /* UNIBO *************************************************************************************************************** */
    implementation name: 'uniboInterfaces'
    implementation name: '2p301'
    implementation name: 'it.unibo.qakactor-2.7'
    implementation name: 'unibonoawtsupports'  //required by the old infrastructure
	implementation name: 'unibo.actor22-1.1'

    /* AIMA **************************************************************************************************************** */
 	//PLANNER  
    implementation name: 'unibo.planner22-1.0'
    // https://mvnrepository.com/artifact/com.googlecode.aima-java/aima-core
    implementation group: 'com.googlecode.aima-java', name: 'aima-core', version: '3.0.0'

}

sourceSets {
    main.java.srcDirs += 'src'
    main.java.srcDirs += 'resources'
    test.java.srcDirs += 'test'		//test is specific
}
  

eclipse {
    classpath {
        sourceSets -= [sourceSets.main, sourceSets.test]	
    }	
 }


application {
    // Define the main class for the application.
    mainClass = 'it.unibo.«ctx.name».«className»Kt'
}

 jar {
    println("building jar")
    from sourceSets.main.allSource
    manifest {
        attributes 'Main-Class': "$mainClassName"
    }
}
'''

def genGradleProperties()'''
kotlinVersion = 1.6.0
'''

def genBuildGitIgnore()'''
bin
bin/default
.metadata
.classpath
.project
build
logs 

log4jInfo.log

#node
nodeCode/*/node_modules/
*.lck
package-lock.json

# MQTT generated
*-tcp*


/ReadMe.html
.lck/ 
docker/
/.metadata/
/src-gen/
'''

def genSettingsGradle(String name)'''
rootProject.name = "it.unibo.«name»"
'''

}