package myTest2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan
public class ApplicationConfiguration {

	@Bean
	public Logger logger() {
		return LoggerFactory.getLogger("application");
	}
	
}
