package example.migration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Migration {

	public static void main(String[] args) {
		var applicationContext =SpringApplication.run(Migration.class, args);
		applicationContext.close();	}

}
