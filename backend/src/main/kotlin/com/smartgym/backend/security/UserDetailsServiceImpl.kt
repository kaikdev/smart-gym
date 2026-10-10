package com.smartgym.backend.security

import com.smartgym.backend.repository.UsuarioRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Ensina o Spring Security a buscar o usuário no banco pelo e-mail.
 */
@Service
class UserDetailsServiceImpl(private val usuarioRepository: UsuarioRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val usuario = usuarioRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("Usuário não encontrado: $username")

        return User.withUsername(usuario.email)
            .password(usuario.senha)
            .disabled(!usuario.ativo)
            .build()
    }
}
