package CPSF.com.demo.Configuration.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
//@Bean
//public SecurityFilterChain filterChain(HttpSecurity httpSecurity)throws Exception{
//
//    httpSecurity.authorizeHttpRequests(config -> config
//
//            .requestMatchers(HttpMethod.GET,"/reservation/findAll").hasRole("admin")
//            .requestMatchers(HttpMethod.GET,"user/findAll").hasRole("admin")
//
//    );
//    httpSecurity.httpBasic(Customizer.withDefaults());
//
//    httpSecurity.csrf(csrf -> csrf.disable());
//    return httpSecurity.build();
//
//
//}

}
