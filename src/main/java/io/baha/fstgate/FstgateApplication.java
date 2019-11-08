package io.baha.fstgate;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EntityScan(basePackageClasses = {
		FstgateApplication.class,
		Jsr310JpaConverters.class
})
public class FstgateApplication {
	private static final Logger LOGGER = LogManager.getLogger(FstgateApplication.class);
	//PropertiesConfigurator is used to configure logger from properties file
	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {
		SpringApplication.run(FstgateApplication.class, args);
		LOGGER.trace("Log4j appender configuration is successful !!");
	}

}
