package com.jaguar_shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Reglas de acceso:
 * - Público: home, catálogo, detalle de producto, login/registro y estáticos.
 * - Requiere login: carrito/checkout y "mis pedidos" (/pedidos/**).
 * - Solo ADMIN: gestión de productos, roles y usuarios.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Recursos públicos
                        .requestMatchers(
                                "/uploads/**", "/css/**", "/js/**",
                                "/images/**", "/webjars/**", "/favicon.ico",
                                "/login", "/registro",
                                "/horarios", "/contacto", "/cambios"
                        ).permitAll()
                        // Zona de administración (solo ADMIN)
                        .requestMatchers("/roles/**", "/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(
                                "/productos/nuevo", "/productos/guardar",
                                "/productos/editar/**", "/productos/actualizar/**",
                                "/productos/eliminar/**"
                        ).hasRole("ADMIN")
                        // Carrito/checkout y pedidos requieren sesión
                        .requestMatchers("/pedidos/**").authenticated()
                        // Navegación pública (home, catálogo, detalle)
                        .requestMatchers(HttpMethod.GET, "/", "/productos", "/productos/*").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
