package com.smartgym.backend.repository

import com.smartgym.backend.entity.Exercicio
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ExercicioRepository : JpaRepository<Exercicio, Long> {

    // Consulta JPQL com LIKE: busca parcial pelo nome, sem diferenciar maiúsculas/minúsculas
    @Query("SELECT e FROM Exercicio e WHERE LOWER(e.nome) LIKE LOWER(CONCAT('%', :termo, '%'))")
    fun buscarPorNomeParcial(@Param("termo") termo: String): List<Exercicio>
}
