package com.mialeds.Config;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.mialeds.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        
        return httpSecurity
                .csrf(csrf -> csrf.disable()) // NOTA: Deshabilitado para la API REST y simplicidad. Considerar habilitar en producción para apps web tradicionales.
                .formLogin(form -> form
                    .loginPage("/login")
                    .defaultSuccessUrl("/principal", true)
                    .failureUrl("/login?error=true")
                    .permitAll())
                .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login"))
                
                .sessionManagement(session -> 
                    session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                
                .authorizeHttpRequests(http -> {
                    // Endpoints públicos
                    http.requestMatchers("/login").permitAll(); 
                    http.requestMatchers("/crearUsuario").permitAll();
                    http.requestMatchers("/restaurarClave").permitAll();
                    http.requestMatchers("/css/**", "/js/**", "/images/**").permitAll();
                    
                    // Endpoints para rol USER
                    http.requestMatchers("/inventario/movimiento").hasRole("USER");
                    http.requestMatchers("/usuario/editarUsuario/**").hasRole("USER");
                    http.requestMatchers("/usuario//cambiarClave/**").hasRole("USER");

                    // Endpoints para rol ADMIN
                    http.requestMatchers("/usuario/crearUsuario").hasRole("ADMIN");
                    http.requestMatchers("/inventario/editar").hasRole("ADMIN");
                    http.requestMatchers("/inventario/nuevo").hasRole("ADMIN");
                    http.requestMatchers("/inventario/eliminar").hasRole("ADMIN");
                    http.requestMatchers("/venta/editar").hasRole("ADMIN");
                    http.requestMatchers("/venta/eliminar").hasRole("ADMIN");
                    http.requestMatchers("/proveedor/**").hasRole("ADMIN");

                    // Todas las demás peticiones requieren autenticación
                    http.anyRequest().authenticated();
                })
                .build();
    }

    @Bean 
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
