package CPSF.com.demo.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenApi() {
        return  new OpenAPI().
                info(new Info().title("Parceo api scheme"))
                .addSecurityItem(new SecurityRequirement().addList("ParceoSecurityScheme"))
                .components(new Components().addSecuritySchemes("", new SecurityScheme()
                        .name("ParceoSecurityScheme")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"))
                );

    }
}
