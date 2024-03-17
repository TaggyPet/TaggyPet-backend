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
            "/user/registration",
            "/pet/find/**",
            "/pet/images/**",
            "/css/**",
            "/img/**",
            "/js/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers(ENDPOINTS_WHITELIST)
                        .permitAll()
                        .requestMatchers("/pet/privileged-list")
                        .hasAnyRole("PRIVILEGED_ACCESS", "ADMIN")
                        .anyRequest()
                        .authenticated())
                .httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
    }
}
