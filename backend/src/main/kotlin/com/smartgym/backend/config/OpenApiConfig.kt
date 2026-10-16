package com.smartgym.backend.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuração do Swagger (OpenAPI).
 * Tela disponível em http://localhost:8080/swagger-ui.html
 */
@Configuration
class OpenApiConfig {

    @Bean
    fun openApi(): OpenAPI = OpenAPI()
        .info(
            Info()
                .title("Smart Gym API")
                .version("1.0.0")
                .description(
                    "API do Smart Gym. Para testar as rotas protegidas: " +
                        "1) crie uma conta em POST /api/auth/signup; " +
                        "2) faça login em POST /api/auth/signin e copie o token; " +
                        "3) clique em Authorize e cole o token."
                )
        )
        // Botão "Authorize" para informar o token JWT
        .components(
            Components().addSecuritySchemes(
                "bearerAuth",
                SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
            )
        )
        // Envia o token em todas as rotas
        .addSecurityItem(SecurityRequirement().addList("bearerAuth"))
}
