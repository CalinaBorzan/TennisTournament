package org.example.tennisapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "org/example/tennisapp/entity") // Add this to ensure that entities are picked up
public class TennisAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TennisAppApplication.class, args);
    }

}
