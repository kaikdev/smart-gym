package com.smartgym.backend.repository

import com.smartgym.backend.entity.Pagamento
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PagamentoRepository : JpaRepository<Pagamento, Long> {

    // Consulta JPQL com LIKE: busca parcial pelo método de pagamento, sem diferenciar maiúsculas/minúsculas
    @Query("SELECT p FROM Pagamento p WHERE LOWER(p.metodo) LIKE LOWER(CONCAT('%', :termo, '%'))")
    fun buscarPorMetodoParcial(@Param("termo") termo: String): List<Pagamento>

    // Usado para impedir dois pagamentos com o mesmo código de transação
    fun findByCodigoTransacao(codigoTransacao: String): Pagamento?
}
