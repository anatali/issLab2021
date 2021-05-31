package it.unibo.sonarresourcegui

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootApplication
//@EnableWebMvc		//DO NOT USE: will disable org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration
class SonarresourceguiApplication

fun main(args: Array<String>) {
	runApplication<SonarresourceguiApplication>(*args)
}
