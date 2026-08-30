package CPSF.com.demo.configuration.notification;

import co.novu.Novu;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfig {

    @Bean
    public Novu novu () {
        return Novu.builder()
                .secretKey("")
                .build();
    }

}
