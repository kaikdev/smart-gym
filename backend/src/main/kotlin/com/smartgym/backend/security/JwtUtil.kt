package com.smartgym.backend.security

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

/**
 * Utilitários para gerar e validar tokens JWT.
 * A chave secreta e a validade ficam no application.properties.
 */
@Component
class JwtUtil(
    @Value("\${jwt.secret}") secret: String,
    @Value("\${jwt.expiration-ms}") private val expiracaoMs: Long
) {

    private val chave: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))

    // Gera um token com o e-mail do usuário como "subject" e data de expiração
    fun gerarToken(email: String): String {
        val agora = Date()
        return Jwts.builder()
            .subject(email)
            .issuedAt(agora)
            .expiration(Date(agora.time + expiracaoMs))
            .signWith(chave)
            .compact()
    }

    // Retorna true se a assinatura for válida e o token não estiver expirado
    fun validarToken(token: String): Boolean =
        try {
            Jwts.parser().verifyWith(chave).build().parseSignedClaims(token)
            true
        } catch (ex: JwtException) {
            false
        } catch (ex: IllegalArgumentException) {
            false
        }

    // Lê o e-mail (usuário) de dentro de um token válido
    fun obterEmail(token: String): String =
        Jwts.parser().verifyWith(chave).build().parseSignedClaims(token).payload.subject
}
