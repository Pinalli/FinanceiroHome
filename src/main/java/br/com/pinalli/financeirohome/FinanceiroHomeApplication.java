package br.com.pinalli.financeirohome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "br.com.pinalli.financeirohome.repository")
public class FinanceiroHomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceiroHomeApplication.class, args);
    }
}