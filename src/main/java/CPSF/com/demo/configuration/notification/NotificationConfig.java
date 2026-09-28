package CPSF.com.demo.configuration.notification;

import co.novu.Novu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
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
        final var finalUrl = serverUrl != null ? serverUrl : DEFAULT_NOVU_SERVER;
        final var novu = Novu.builder()
                .secretKey(novuSecretKey)
                .serverURL(finalUrl)
                .build();

        log.info(
                "Novu configured with env variables: {} with integration params: {}",
                novu.environmentVariables().listDirect(),
                novu.integrations().listDirect()
        );

        return novu;
    }

}
