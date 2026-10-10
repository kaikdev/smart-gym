package com.smartgym.backend.repository

import com.smartgym.backend.entity.Equipamento
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface EquipamentoRepository : JpaRepository<Equipamento, Long> {

    // Consulta JPQL com LIKE: busca parcial pelo nome, sem diferenciar maiúsculas/minúsculas
    @Query("SELECT e FROM Equipamento e WHERE LOWER(e.nome) LIKE LOWER(CONCAT('%', :termo, '%'))")
    fun buscarPorNomeParcial(@Param("termo") termo: String): List<Equipamento>

    // Usado para impedir dois equipamentos com o mesmo sensor
    fun findByCodigoSensorIot(codigoSensorIot: String): Equipamento?
}
