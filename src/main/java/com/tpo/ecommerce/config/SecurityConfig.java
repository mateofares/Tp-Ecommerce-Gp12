package com.tpo.ecommerce.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/authenticate").permitAll()
                        .requestMatchers(HttpMethod.GET, "/productos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/resenias/producto/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/envios/rastrear").permitAll()
                        .requestMatchers(HttpMethod.GET, "/descuentos/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/usuarios/**").hasAuthority("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/**").hasAuthority("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/facturas/*/anular").hasAuthority("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/envios/**").hasAuthority("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/envios/**").hasAuthority("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/descuentos/**").hasAuthority("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/descuentos/**").hasAuthority("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/descuentos/**").hasAuthority("ADMINISTRADOR")

                        .requestMatchers("/carrito/**").hasAuthority("USUARIO")
                        .requestMatchers(HttpMethod.POST, "/ordenes/**").hasAuthority("USUARIO")
                        .requestMatchers(HttpMethod.POST, "/productos/**").hasAuthority("USUARIO")
                        .requestMatchers(HttpMethod.PATCH, "/productos/**").hasAuthority("USUARIO")
                        .requestMatchers(HttpMethod.DELETE, "/productos/**").hasAuthority("USUARIO")
                        .requestMatchers(HttpMethod.POST, "/resenias/**").hasAuthority("USUARIO")
                        .requestMatchers("/direcciones/**").hasAuthority("USUARIO")
                        .requestMatchers("/pagos/**").hasAuthority("USUARIO")
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                writeSecurityError(
                                        response,
                                        request.getRequestURI(),
                                        HttpStatus.UNAUTHORIZED,
                                        "No autenticado o credenciales invalidas"
                                )
                        )
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                writeSecurityError(
                                        response,
                                        request.getRequestURI(),
                                        HttpStatus.FORBIDDEN,
                                        "No tenes permisos para realizar esta accion"
                                )
                        )
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private void writeSecurityError(
            HttpServletResponse response,
            String path,
            HttpStatus status,
            String message
    ) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(
                response.getWriter(),
                new SecurityErrorResponse(
                        LocalDateTime.now(),
                        status.value(),
                        status.name(),
                        message,
                        path
                )
        );
    }

    private record SecurityErrorResponse(
            LocalDateTime timestamp,
            int statusCode,
            String httpStatus,
            String message,
            String path
    ) {
    }
}
