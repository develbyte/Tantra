package com.tantra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TantraApplication {
    public static void main(String[] args) {
        SpringApplication.run(TantraApplication.class, args);
    }
}

