package com.tpo.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        // 1. ENDPOINTS PÚBLICOS (Cualquiera entra)
                .requestMatchers("/api/auth/**").permitAll()                .requestMatchers(HttpMethod.GET, "/productos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/resenias/producto/**").permitAll()

                // 2. ENDPOINTS EXCLUSIVOS DEL ADMINISTRADOR (Gestión)
                .requestMatchers(HttpMethod.GET, "/usuarios/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/usuarios/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PATCH, "/facturas/*/anular").hasAuthority("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PATCH, "/envios/*/estado").hasAuthority("ADMINISTRADOR")

                // 3. ENDPOINTS EXCLUSIVOS DEL USUARIO (Comprador/Vendedor)
                .requestMatchers("/carrito/**").hasAuthority("USUARIO")
                .requestMatchers(HttpMethod.POST, "/ordenes/**").hasAuthority("USUARIO")
                .requestMatchers(HttpMethod.POST, "/productos/**").hasAuthority("USUARIO")
                .requestMatchers(HttpMethod.PATCH, "/productos/**").hasAuthority("USUARIO")
                .requestMatchers(HttpMethod.DELETE, "/productos/**").hasAuthority("USUARIO")
                .requestMatchers(HttpMethod.POST, "/resenias/**").hasAuthority("USUARIO")
                .requestMatchers("/direcciones/**").hasAuthority("USUARIO")
                .requestMatchers("/pagos/**").hasAuthority("USUARIO")
                        // Todo lo demas requiere autenticacion
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
