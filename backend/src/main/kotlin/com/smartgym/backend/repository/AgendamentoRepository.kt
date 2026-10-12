package com.smartgym.backend.repository

import com.smartgym.backend.entity.Agendamento
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AgendamentoRepository : JpaRepository<Agendamento, Long> {

    // Consulta JPQL com LIKE: busca parcial pelo tipo de aula, sem diferenciar maiúsculas/minúsculas
    @Query("SELECT a FROM Agendamento a WHERE LOWER(a.tipoAula) LIKE LOWER(CONCAT('%', :termo, '%'))")
    fun buscarPorTipoAulaParcial(@Param("termo") termo: String): List<Agendamento>
}
