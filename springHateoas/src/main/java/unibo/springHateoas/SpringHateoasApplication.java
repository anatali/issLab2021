package unibo.springHateoas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@Configuration
//EnableAutoConfiguration
//ComponentScan
@SpringBootApplication
public class SpringHateoasApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringHateoasApplication.class, args);
	}

}

//https://www.baeldung.com/spring-hateoas-tutorial
//
//gradlew bootRun  gradlew run
//spring run
/*
https://www.springcloud.io/post/2022-05/springboot-hateoas/#gsc.tab=0
HATEOAS comes with the corresponding resource links, through a resource you can get other resources
that can be accessed from this resource, just like a visit to a page, you can then go through this page
to access other pages.

HATEOAS in REST APIs is like hyperlinks in web pages:
 HAL (Hypertext Application Language)
 */