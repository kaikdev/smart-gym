package com.smartgym.backend.repository

import com.smartgym.backend.entity.Plano
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PlanoRepository : JpaRepository<Plano, Long> {

    // Consulta JPQL com LIKE: busca parcial pela descrição, sem diferenciar maiúsculas/minúsculas
    @Query("SELECT p FROM Plano p WHERE LOWER(p.descricao) LIKE LOWER(CONCAT('%', :termo, '%'))")
    fun buscarPorDescricaoParcial(@Param("termo") termo: String): List<Plano>
}
