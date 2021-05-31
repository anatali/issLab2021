package it.unibo.sonarguigpring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootApplication
//@EnableWebMvc		//DO NOT USE: disbale satic
//@EnableWebMvc will disable org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration
class SonarguigpringApplication

fun main(args: Array<String>) {
	runApplication<SonarguigpringApplication>(*args)
}
