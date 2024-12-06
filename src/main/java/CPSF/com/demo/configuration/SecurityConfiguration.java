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

                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/users/**").hasRole(Role.ADMIN.name())
                        .requestMatchers("/camperPlace/**").hasRole(Role.ADMIN.name())
                        .requestMatchers("/reservations/createReservation").hasAnyRole(Role.ADMIN.name(), Role.GUEST.name())
                        .requestMatchers("/reservations/updateReservation").hasAnyRole(Role.ADMIN.name(), Role.GUEST.name())
                        .requestMatchers("/reservations/find").hasRole(Role.ADMIN.name())
                        .requestMatchers("/reservations/find/user").hasRole(Role.ADMIN.name())
                        .requestMatchers("reservations/findByReservationStatus").hasRole(Role.ADMIN.name())
                        .requestMatchers("/swagger-ui/**").permitAll()
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