package org.example.tennisapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity   // enables @PreAuthorize
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                /* ---------- PUBLIC ENDPOINTS ---------- */
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST , "/api/users/login"    ).permitAll()
                        .requestMatchers(HttpMethod.POST , "/api/users/register" ).permitAll()
                        .requestMatchers(HttpMethod.GET  , "/api/tournaments/available").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()


                        /* ---------- ROLE-BASED ENDPOINTS ---------- */
                        // Admin
                        .requestMatchers("/api/users/**").hasAnyRole("ADMIN","PLAYER","REFEREE")
                        .requestMatchers("/api/matches/export"                 ).hasRole("ADMIN")
                        .requestMatchers("/api/tournaments/registrations/**"   ).hasRole("ADMIN")

                        // Referee
                        .requestMatchers("/api/matches/**"                     ).hasAnyRole("REFEREE","ADMIN","PLAYER")
                        .requestMatchers("/api/users/filter-players"           ).hasRole("REFEREE")

                        // Player
                        .requestMatchers("/api/tournaments/request"            ).hasRole("PLAYER")
                        .requestMatchers("/api/tournaments/registered"         ).hasRole("PLAYER")



                        .anyRequest().authenticated()
                )

                /* ---------- JWT FILTER ---------- */
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /* ---------- CORS + PASSWORD BEANS ---------- */

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowCredentials(true);
        cfg.addAllowedOrigin("http://localhost:3000");
        cfg.addAllowedHeader("*");
        cfg.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return src;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
