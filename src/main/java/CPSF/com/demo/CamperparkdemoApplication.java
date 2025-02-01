package CPSF.com.demo;
import CPSF.com.demo.A_security.config.RsaConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories
@EnableConfigurationProperties(RsaConfig.class)
public class CamperparkdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamperparkdemoApplication.class, args);

	}
}