package com.smartgym.backend.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * Configuração do Spring Security.
 */
@Configuration
@EnableWebSecurity
class SecurityConfig(private val jwtAuthenticationFilter: JwtAuthenticationFilter) {

    // Criptografia das senhas com BCrypt
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    // Gerenciador usado pelo /signin para validar e-mail e senha
    @Bean
    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager =
        configuration.authenticationManager

    // Regras de autorização
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // API REST com token não usa sessão nem CSRF
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it
                    // Rotas públicas
                    .requestMatchers("/api/auth/signin", "/api/auth/signup", "/error").permitAll()
                    // Todas as demais exigem token
                    .anyRequest().authenticated()
            }
            // Sem token (ou token inválido) -> 401 Unauthorized
            .exceptionHandling { it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) }
            // Nosso filtro JWT roda antes do filtro padrão de login
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
