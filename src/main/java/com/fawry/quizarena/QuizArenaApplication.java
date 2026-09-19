package com.fawry.quizarena;

import com.fawry.quizarena.config.CacheConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(CacheConfig.class)
public class QuizArenaApplication {

    static void main(String[] args) {
        SpringApplication.run(QuizArenaApplication.class, args);
    }

}
