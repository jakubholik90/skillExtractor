
package com.skillextractor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SkillExtractorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkillExtractorApplication.class, args);
    }
}