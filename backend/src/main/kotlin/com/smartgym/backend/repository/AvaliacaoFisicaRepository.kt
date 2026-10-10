package com.smartgym.backend.repository

import com.smartgym.backend.entity.AvaliacaoFisica
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AvaliacaoFisicaRepository : JpaRepository<AvaliacaoFisica, Long> {

    // Consulta JPQL com LIKE: busca parcial pelo nome do avaliador, sem diferenciar maiúsculas/minúsculas
    @Query("SELECT a FROM AvaliacaoFisica a WHERE LOWER(a.avaliador) LIKE LOWER(CONCAT('%', :termo, '%'))")
    fun buscarPorAvaliadorParcial(@Param("termo") termo: String): List<AvaliacaoFisica>
}
