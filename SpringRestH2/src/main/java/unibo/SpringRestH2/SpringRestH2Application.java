package unibo.SpringRestH2;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
//@EnableSwagger2 See the video https://www.youtube.com/watch?v=utRxyPfFlDw
public class SpringRestH2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestH2Application.class, args);
	}
}
