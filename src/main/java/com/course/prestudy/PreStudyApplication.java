package com.course.prestudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PreStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(PreStudyApplication.class, args);
    }

}
