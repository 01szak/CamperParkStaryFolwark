package CPSF.com.demo.configuration;

import CPSF.com.demo.configuration.auth.JwtAuthenticationFilter;
import CPSF.com.demo.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize

                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                        .requestMatchers("/auth/register","/auth/authenticate").permitAll()
                        .requestMatchers("/reservations/createReservation","/reservations/updateReservation","/reservations/findAll").hasAnyAuthority(Role.ADMIN.toString(), Role.GUEST.toString())
                        .requestMatchers("/users/**","/camperPlace/**","/reservations/find","/reservations/find/user","/reservations/findByReservationStatus").hasAuthority(Role.ADMIN.toString())
                        .requestMatchers("/camperPlace/**").hasAuthority(Role.ADMIN.toString())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }


}