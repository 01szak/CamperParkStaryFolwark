package CPSF.com.demo.configuration;

import CPSF.com.demo.service.core.OrganisationService;
import CPSF.com.demo.service.core.UserService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.DefaultSecurityFilterChain;

import static CPSF.com.demo.model.constant.UserRole.ADMIN;
import static CPSF.com.demo.model.constant.UserRole.OWNER;
import static CPSF.com.demo.model.constant.UserRole.REGULAR;
import static CPSF.com.demo.model.constant.UserRole.WEB_APP;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final RsaConfig rsaConfig;
    private final UserService userService;
    private final OrganisationService organisationService;

    @Bean
    AuthenticationManager authenticationManager() {
        var authProvider = new DaoAuthenticationProvider(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    DefaultSecurityFilterChain configure(HttpSecurity http) {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/error/**", "/auth/login", "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/reservation").hasAnyAuthority(OWNER.getAuthority(), ADMIN.getAuthority(), REGULAR.getAuthority(), WEB_APP.getAuthority())
                        .requestMatchers(HttpMethod.GET, "/camperPlace", "/camperPlace/calcPrice/**", "/camperPlace/occupancy/**").hasAnyAuthority(OWNER.getAuthority(), ADMIN.getAuthority(), REGULAR.getAuthority(), WEB_APP.getAuthority())
                        .anyRequest().hasAnyAuthority(OWNER.getAuthority(), ADMIN.getAuthority(), REGULAR.getAuthority())
                )
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer((oauth2) -> oauth2.jwt((jwt) -> jwt.decoder(decoder())))
                .addFilterBefore(new WebAppAuthFilter(organisationService, userService), BearerTokenAuthenticationFilter.class)
                .userDetailsService(userService)
                .build();
    }

    @Bean
    JwtDecoder decoder() {
        return NimbusJwtDecoder.withPublicKey(this.rsaConfig.publicKey()).build();
    }

    @Bean
    JwtEncoder encoder() {
        RSAKey jwk = new RSAKey.Builder(this.rsaConfig.publicKey()).privateKey(this.rsaConfig.privateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
