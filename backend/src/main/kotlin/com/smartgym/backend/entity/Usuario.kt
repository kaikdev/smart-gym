package com.smartgym.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

/**
 * Usuário da academia. Também é usado para o login (email + senha).
 */
@Entity
@Table(name = "usuarios")
class Usuario(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "nome", length = 100, nullable = false)
    var nome: String = "",

    @Column(name = "email", length = 150, nullable = false, unique = true)
    var email: String = "",

    // Guardada sempre criptografada (BCrypt)
    @Column(name = "senha", length = 255, nullable = false)
    var senha: String = "",

    @Column(name = "data_nascimento")
    var dataNascimento: LocalDate? = null,

    @Column(name = "peso_atual")
    var pesoAtual: Float? = null,

    @Column(name = "ativo", nullable = false)
    var ativo: Boolean = true
)
