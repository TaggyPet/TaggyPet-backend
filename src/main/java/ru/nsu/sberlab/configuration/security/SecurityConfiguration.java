package ru.nsu.sberlab.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private static final String[] ENDPOINTS_WHITELIST = {
            "/",
            "/api/v1/user/registration",
            "/api/v1/pet/find/**",
            "/api/v1/pet/images/**",
            "/css/**",
            "/img/**",
            "/js/**"
    };
    private static final String[] SWAGGER_OPENAPI_ENDPOINTS = {
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers(ENDPOINTS_WHITELIST)
                        .permitAll()
                        .requestMatchers(SWAGGER_OPENAPI_ENDPOINTS)
                        .permitAll()
                        .requestMatchers("/api/v1/pet/privileged-list")
                        .hasAnyRole("PRIVILEGED_ACCESS", "ADMIN")
                        .anyRequest()
                        .authenticated())
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        .loginPage("/api/v1/security/auth")
                        .permitAll())
        ;
        return httpSecurity.build();
    }
}
