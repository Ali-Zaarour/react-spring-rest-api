package example.migration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		var applicationContext =SpringApplication.run(Application.class, args);
		applicationContext.close();	}

}
