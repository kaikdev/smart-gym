package com.smartgym.backend.repository

import com.smartgym.backend.entity.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UsuarioRepository : JpaRepository<Usuario, Long> {

    // Consulta JPQL com LIKE: busca parcial pelo nome, sem diferenciar maiúsculas/minúsculas
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :termo, '%'))")
    fun buscarPorNomeParcial(@Param("termo") termo: String): List<Usuario>

    // Usado pelo login (Spring Security) e para impedir e-mails repetidos
    fun findByEmail(email: String): Usuario?

    fun existsByEmail(email: String): Boolean
}
