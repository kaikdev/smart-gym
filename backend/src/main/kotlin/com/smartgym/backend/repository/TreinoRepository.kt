package com.smartgym.backend.repository

import com.smartgym.backend.entity.Treino
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TreinoRepository : JpaRepository<Treino, Long> {

    // Consulta JPQL com LIKE: busca parcial pelo título, sem diferenciar maiúsculas/minúsculas
    @Query("SELECT t FROM Treino t WHERE LOWER(t.titulo) LIKE LOWER(CONCAT('%', :termo, '%'))")
    fun buscarPorTituloParcial(@Param("termo") termo: String): List<Treino>
}
