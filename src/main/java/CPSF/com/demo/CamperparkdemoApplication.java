package CPSF.com.demo;
import CPSF.com.demo.A_security.config.RsaConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
@EnableConfigurationProperties(RsaConfig.class)
public class CamperparkdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamperparkdemoApplication.class, args);

	}
}