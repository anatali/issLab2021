package unibo.SpringDataRest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/*
@Configuration:
  Tags the class as a source of bean definitions for the application context.
@EnableAutoConfiguration:
   Tells Spring Boot to start adding beans based on classpath settings,
   other beans, and various property settings.
   For example, if spring-webmvc is on the classpath,
   this annotation flags the application as a web application and
   activates key behaviors, such as setting up a DispatcherServlet.
@ComponentScan:
  Tells Spring to look for other components, configurations, and services in the
  unibo.SpringDataRest package, letting it find the controllers.
*/

@SpringBootApplication
public class SpringDataRestApplication {

private static ConfigurableApplicationContext context = null;

	public static void main(String[] args) {
		if( context == null) {
			context = SpringApplication.run(SpringDataRestApplication.class, args);
		}
	}
	public static void closeAppl( ) {
		if( context != null){
			System.out.println("&&& Application closed");
			context.close();
		}
	}

}
