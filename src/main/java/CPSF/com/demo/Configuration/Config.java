package CPSF.com.demo.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
//@EnableSwagger2
public class Config {

    @Autowired
    private ObjectMapper objectMapper;

    void customizedObjectMapper(){
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.restapi.rest_demo"))
//                .paths(PathSelectors.any())
//                .build();
//    }
}
