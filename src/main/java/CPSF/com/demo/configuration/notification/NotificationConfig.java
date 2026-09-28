package CPSF.com.demo.configuration.notification;

import co.novu.Novu;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class NotificationConfig {

    @Value("${novu.secret-key}")
    private String novuSecretKey;
    @Value("${novu.server-url}")
    private String serverUrl;

    private static final String DEFAULT_NOVU_SERVER = "https://api.novu.co";

    //TODO Novu should be inside NotificationService to not correlate business logic with external service
    @Bean
    public Novu novu () {
        return Novu.builder()
                .secretKey(novuSecretKey)
                .serverURL(serverUrl != null ? serverUrl : DEFAULT_NOVU_SERVER)
                .build();
    }

}
