package com.smartgym.backend.config

import com.smartgym.backend.entity.Usuario
import com.smartgym.backend.repository.UsuarioRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class CargaInicial(
    private val usuarioRepository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String) {
        if (usuarioRepository.existsByEmail("admin@smartgym.com")) return
        usuarioRepository.save(
            Usuario(
                nome = "Administrador",
                email = "admin@smartgym.com",
                senha = passwordEncoder.encode("admin123")!!,
                perfil = "ADMIN"
            )
        )
    }
}
