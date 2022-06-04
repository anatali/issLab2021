package it.unibo.generator;

import it.unibo.qactork.Context;
import it.unibo.qactork.QActorSystemSpec;
import it.unibo.qactork.generator.common.GenUtils;
import it.unibo.qactork.generator.common.SysKb;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class GenQActorCtxSystem {
  public void doGenerate(final QActorSystemSpec system, final Context ctx, final SysKb kb) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method println(Object) is undefined"
      + "\n+ cannot be resolved."
      + "\nThe method or field toFirstUpper is undefined for the type String"
      + "\n+ cannot be resolved."
      + "\n! cannot be resolved."
      + "\nThe method genFileDir(String, String, String, String, CharSequence) from the type GenUtils refers to the missing type Object"
      + "\nThe method genFileDir(String, String, String, String, CharSequence) from the type GenUtils refers to the missing type Object"
      + "\nThe method genFileDir(String, String, String, String, CharSequence) from the type GenUtils refers to the missing type Object"
      + "\nThe method genFileDir(String, String, String, String, CharSequence) from the type GenUtils refers to the missing type Object"
      + "\nThe method genFileDir(String, String, String, String, CharSequence) from the type GenUtils refers to the missing type Object"
      + "\nThe method genFileDir(String, String, String, String, CharSequence) from the type GenUtils refers to the missing type Object"
      + "\n+ cannot be resolved"
      + "\n+ cannot be resolved");
  }
  
  /**
   * Log properties
   */
  public CharSequence genLogProperties() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("# Root logger option");
    _builder.newLine();
    _builder.append("#### log4j.rootLogger=INFO, stdout ");
    _builder.newLine();
    _builder.append("log4j.rootLogger=INFO, file");
    _builder.newLine();
    _builder.newLine();
    _builder.append("# Direct log messages to a log file");
    _builder.newLine();
    _builder.append("log4j.appender.file=org.apache.log4j.RollingFileAppender");
    _builder.newLine();
    _builder.append("log4j.appender.file.File=log4jInfo.log");
    _builder.newLine();
    _builder.append("log4j.appender.file.MaxFileSize=10MB");
    _builder.newLine();
    _builder.append("log4j.appender.file.MaxBackupIndex=10");
    _builder.newLine();
    _builder.append("log4j.appender.file.layout=org.apache.log4j.PatternLayout");
    _builder.newLine();
    _builder.append("log4j.appender.file.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");
    _builder.newLine();
    _builder.newLine();
    _builder.append("# Direct log messages to stdout");
    _builder.newLine();
    _builder.append("### log4j.appender.stdout=org.apache.log4j.ConsoleAppender");
    _builder.newLine();
    _builder.append("### log4j.appender.stdout.Target=System.out");
    _builder.newLine();
    _builder.append("### log4j.appender.stdout.layout=org.apache.log4j.PatternLayout");
    _builder.newLine();
    _builder.append("### log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");
    _builder.newLine();
    return _builder;
  }
  
  /**
   * ACTOR CONTEXT
   */
  public CharSequence genCtxQActor(final String sysName, final String packageName, final String className, final Context ctx) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(GenUtils.logo);
    _builder.newLineIfNotEmpty();
    _builder.append("package ");
    _builder.append(packageName);
    _builder.newLineIfNotEmpty();
    _builder.append("import it.unibo.kactor.QakContext");
    _builder.newLine();
    _builder.append("import it.unibo.kactor.sysUtil");
    _builder.newLine();
    _builder.append("import kotlinx.coroutines.runBlocking");
    _builder.newLine();
    _builder.newLine();
    _builder.append("fun main() = runBlocking {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("QakContext.createContexts(");
    _builder.newLine();
    _builder.append("\t        ");
    _builder.append("\"");
    String _host = ctx.getIp().getHost();
    _builder.append(_host, "\t        ");
    _builder.append("\", this, \"");
    _builder.append(sysName, "\t        ");
    _builder.append(".pl\", \"sysRules.pl\"");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append(")");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence genBuildGradle(final String className, final Context ctx, final boolean ddr) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/*");
    _builder.newLine();
    _builder.append("================================================================================");
    _builder.newLine();
    _builder.append("build.gradle");
    _builder.newLine();
    _builder.append("GENERATED ONLY ONCE");
    _builder.newLine();
    _builder.append("================================================================================");
    _builder.newLine();
    _builder.append("*/");
    _builder.newLine();
    _builder.append("plugins {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// Apply the application plugin to add support for building a CLI application in Java.");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("id \'application\'");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("id \'java\'");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("id \'eclipse\'");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("id \'org.jetbrains.kotlin.jvm\' version \"$kotlinVersion\"");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("version \'1.0\'");
    _builder.newLine();
    _builder.append("sourceCompatibility = 11");
    _builder.newLine();
    _builder.newLine();
    _builder.append("repositories {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// Use Maven Central for resolving dependencies.");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("mavenCentral()");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("flatDir {   dirs \'../unibolibs\'\t }");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("dependencies {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// Align versions of all Kotlin components");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("implementation platform(\'org.jetbrains.kotlin:kotlin-bom\')");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// Use the Kotlin JDK 8 standard library.");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("implementation \'org.jetbrains.kotlin:kotlin-stdlib-jdk8\'");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// This dependency is used by the application.");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("implementation \'com.google.guava:guava:30.1.1-jre\'");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// Use the Kotlin test library.");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("testImplementation \'org.jetbrains.kotlin:kotlin-test\'");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// Use the Kotlin JUnit integration.");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("testImplementation \'org.jetbrains.kotlin:kotlin-test-junit\'");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/* COROUTINES ********************************************************************************************************** */");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("implementation group: \'org.jetbrains.kotlinx\', name: \'kotlinx-coroutines-core\', version: \"$kotlinVersion\", ext: \'pom\'");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core-jvm");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("implementation group: \'org.jetbrains.kotlinx\', name: \'kotlinx-coroutines-core-jvm\', version: \"$kotlinVersion\"");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/*  MQTT *************************************************************************************************************** */");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// https://mvnrepository.com/artifact/org.eclipse.paho/org.eclipse.paho.client.mqttv3");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("implementation group: \'org.eclipse.paho\', name: \'org.eclipse.paho.client.mqttv3\', version: \'1.2.5\'");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/* JSON **************************************************************************************************************** */");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// https://mvnrepository.com/artifact/org.json/json");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("implementation group: \'org.json\', name: \'json\', version: \'20220320\'");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/* COAP **************************************************************************************************************** */");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// https://mvnrepository.com/artifact/org.eclipse.californium/californium-core");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("implementation group: \'org.eclipse.californium\', name: \'californium-core\', version: \'3.5.0\'");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// https://mvnrepository.com/artifact/org.eclipse.californium/californium-proxy2");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("implementation group: \'org.eclipse.californium\', name: \'californium-proxy2\', version: \'3.5.0\'");
    _builder.newLine();
    _builder.newLine();
    _builder.append("//OkHttp library for websockets with Kotlin");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//implementation group: \'com.squareup.okhttp3\', name: \'okhttp\', version: \'3.14.0\'    ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("implementation group: \'com.squareup.okhttp3\', name: \'okhttp\', version: \'4.9.3\' ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// https://mvnrepository.com/artifact/com.squareup.okhttp3/mockwebserver");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("testImplementation group: \'com.squareup.okhttp3\', name: \'mockwebserver\', version: \'4.9.3\'");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/* LOG4J *************************************************************************************************************** */");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// https://mvnrepository.com/artifact/org.slf4j/slf4j-reload4j");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("implementation group: \'org.slf4j\', name: \'slf4j-reload4j\', version: \'2.0.0-alpha7\'");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/* HTTP **************************************************************************************************************** */");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("implementation group: \'org.apache.httpcomponents\', name: \'httpclient\', version: \'4.5.13\'");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// https://mvnrepository.com/artifact/commons-io/commons-io");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("implementation group: \'commons-io\', name: \'commons-io\', version: \'2.11.0\'");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/* UNIBO *************************************************************************************************************** */");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("implementation name: \'uniboInterfaces\'");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("implementation name: \'2p301\'");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("implementation name: \'it.unibo.qakactor-2.7\'");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("implementation name: \'unibonoawtsupports\'  //required by the old infrastructure");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("implementation name: \'unibo.actor22-1.1\'");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("/* AIMA **************************************************************************************************************** */");
    _builder.newLine();
    _builder.append(" \t");
    _builder.append("//PLANNER  ");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("implementation name: \'unibo.planner22-1.0\'");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// https://mvnrepository.com/artifact/com.googlecode.aima-java/aima-core");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("implementation group: \'com.googlecode.aima-java\', name: \'aima-core\', version: \'3.0.0\'");
    _builder.newLine();
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("sourceSets {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("main.java.srcDirs += \'src\'");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("main.java.srcDirs += \'resources\'");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("test.java.srcDirs += \'test\'\t\t//test is specific");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("  ");
    _builder.newLine();
    _builder.newLine();
    _builder.append("eclipse {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("classpath {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("sourceSets -= [sourceSets.main, sourceSets.test]\t");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}\t");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.newLine();
    _builder.append("application {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// Define the main class for the application.");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("mainClass = \'it.unibo.");
    String _name = ctx.getName();
    _builder.append(_name, "    ");
    _builder.append(".");
    _builder.append(className, "    ");
    _builder.append("Kt\'");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append(" ");
    _builder.append("jar {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("println(\"building jar\")");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("from sourceSets.main.allSource");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("manifest {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("attributes \'Main-Class\': \"$mainClassName\"");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence genGradleProperties() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("kotlinVersion = 1.6.0");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence genBuildGitIgnore() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("bin");
    _builder.newLine();
    _builder.append("bin/default");
    _builder.newLine();
    _builder.append(".metadata");
    _builder.newLine();
    _builder.append(".classpath");
    _builder.newLine();
    _builder.append(".project");
    _builder.newLine();
    _builder.append("build");
    _builder.newLine();
    _builder.append("logs ");
    _builder.newLine();
    _builder.newLine();
    _builder.append("log4jInfo.log");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#node");
    _builder.newLine();
    _builder.append("nodeCode/*/node_modules/");
    _builder.newLine();
    _builder.append("*.lck");
    _builder.newLine();
    _builder.append("package-lock.json");
    _builder.newLine();
    _builder.newLine();
    _builder.append("# MQTT generated");
    _builder.newLine();
    _builder.append("*-tcp*");
    _builder.newLine();
    _builder.newLine();
    _builder.newLine();
    _builder.append("/ReadMe.html");
    _builder.newLine();
    _builder.append(".lck/ ");
    _builder.newLine();
    _builder.append("docker/");
    _builder.newLine();
    _builder.append("/.metadata/");
    _builder.newLine();
    _builder.append("/src-gen/");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence genSettingsGradle(final String name) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("rootProject.name = \"it.unibo.");
    _builder.append(name);
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
}
