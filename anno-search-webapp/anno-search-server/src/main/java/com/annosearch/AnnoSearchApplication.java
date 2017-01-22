package com.annosearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Main Spring Boot application class for the Anno Search application.
 *
 * @author Tsvetan Dimitrov <tsvetan.dimitrov@ontotext.com>
 * @since 22 January 2017
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class AnnoSearchApplication {

    public static void main(String args[]) {
        SpringApplication.run(AnnoSearchApplication.class, args);
    }
}
