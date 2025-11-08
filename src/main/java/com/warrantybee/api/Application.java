package com.warrantybee.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * Entry point for the WarrantyBee Spring Boot application.
 */
@SpringBootApplication(
    exclude = {
        SecurityAutoConfiguration.class
    }
)
public class Application {	

	/**
	 * Starts the Spring Boot application.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
