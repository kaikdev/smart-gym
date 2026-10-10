package com.smartgym.backend.service

import com.smartgym.backend.dto.UsuarioRequest
import com.smartgym.backend.dto.UsuarioResponse
import com.smartgym.backend.exception.ConflictException
import com.smartgym.backend.exception.NotFoundException
import com.smartgym.backend.mapper.UsuarioMapper
import com.smartgym.backend.repository.UsuarioRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Regras de negócio (CRUD) do Usuario.
 */
@Service
class UsuarioService(
    private val usuarioRepository: UsuarioRepository,
    private val usuarioMapper: UsuarioMapper,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun criar(request: UsuarioRequest): UsuarioResponse {
        if (usuarioRepository.existsByEmail(request.email!!)) {
            throw ConflictException("Já existe um usuário com o e-mail ${request.email}")
        }
        val usuario = usuarioMapper.toEntity(request, passwordEncoder.encode(request.senha!!)!!)
        return usuarioMapper.toResponse(usuarioRepository.save(usuario))
    }

    @Transactional(readOnly = true)
    fun listar(): List<UsuarioResponse> =
        usuarioRepository.findAll().map { usuarioMapper.toResponse(it) }

    @Transactional(readOnly = true)
    fun buscarPorId(id: Long): UsuarioResponse =
        usuarioMapper.toResponse(buscarEntidade(id))

    @Transactional(readOnly = true)
    fun buscarPorNome(termo: String): List<UsuarioResponse> =
        usuarioRepository.buscarPorNomeParcial(termo).map { usuarioMapper.toResponse(it) }

    @Transactional
    fun atualizar(id: Long, request: UsuarioRequest): UsuarioResponse {
        val usuario = buscarEntidade(id)
        val outro = usuarioRepository.findByEmail(request.email!!)
        if (outro != null && outro.id != id) {
            throw ConflictException("Já existe um usuário com o e-mail ${request.email}")
        }
        usuarioMapper.updateEntity(usuario, request, passwordEncoder.encode(request.senha!!)!!)
        return usuarioMapper.toResponse(usuarioRepository.save(usuario))
    }

    @Transactional
    fun remover(id: Long) {
        usuarioRepository.delete(buscarEntidade(id))
    }

    private fun buscarEntidade(id: Long) =
        usuarioRepository.findById(id).orElseThrow { NotFoundException("Usuário $id não encontrado") }
}
