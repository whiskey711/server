package ca.uvic.ecg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableJpaRepositories(basePackages = "ca.uvic.ecg.repository")
@EnableScheduling
public class SpringJpaHibernateUvicEcgApplication {
    private static String freq = "1";

    public static void main(String[] args) {
        SpringApplication.run(SpringJpaHibernateUvicEcgApplication.class, args);

    }

}
