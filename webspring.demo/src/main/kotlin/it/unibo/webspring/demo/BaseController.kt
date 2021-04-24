package it.unibo.webspring.demo

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ExceptionHandler
import java.lang.Exception

//@Controller
class BaseController {
    @Value("\${spring.application.name}")
    var appName: String? = null
    @GetMapping("/")
    fun homePage(model: Model): String {
        println("------------------- BaseController homePage model=$model")
        model.addAttribute("arg", appName)
        println("------------------- BaseController homePage model=$model")
        return "welcome"
    }

    @ExceptionHandler
    fun handle(ex: Exception): ResponseEntity<*> {
        val responseHeaders = HttpHeaders()
        return ResponseEntity<Any?>(
            "BaseController ERROR ${ex.message}",
            responseHeaders, HttpStatus.CREATED
        )
    }
}