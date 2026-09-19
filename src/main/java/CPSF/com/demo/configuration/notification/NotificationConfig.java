package CPSF.com.demo.configuration.notification;

import co.novu.Novu;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfig {

    @Value("${novu.secret-key}")
    private String novuSecretKey;

    @Bean
    public Novu novu () {
        return Novu.builder()
                .secretKey(novuSecretKey)
                .build();
    }

}
