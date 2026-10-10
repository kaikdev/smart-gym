package com.smartgym.backend.mapper

import com.smartgym.backend.dto.UsuarioRequest
import com.smartgym.backend.dto.UsuarioResponse
import com.smartgym.backend.entity.Usuario
import org.springframework.stereotype.Component

/**
 * Converte Usuario <-> DTOs.
 * A senha recebida aqui já deve vir criptografada pelo serviço.
 */
@Component
class UsuarioMapper {

    fun toEntity(request: UsuarioRequest, senhaCriptografada: String): Usuario =
        Usuario(
            nome = request.nome!!,
            email = request.email!!,
            senha = senhaCriptografada,
            dataNascimento = request.dataNascimento,
            pesoAtual = request.pesoAtual,
            ativo = request.ativo ?: true
        )

    fun updateEntity(usuario: Usuario, request: UsuarioRequest, senhaCriptografada: String) {
        usuario.nome = request.nome!!
        usuario.email = request.email!!
        usuario.senha = senhaCriptografada
        usuario.dataNascimento = request.dataNascimento
        usuario.pesoAtual = request.pesoAtual
        usuario.ativo = request.ativo ?: true
    }

    fun toResponse(usuario: Usuario): UsuarioResponse =
        UsuarioResponse(
            id = usuario.id,
            nome = usuario.nome,
            email = usuario.email,
            dataNascimento = usuario.dataNascimento,
            pesoAtual = usuario.pesoAtual,
            ativo = usuario.ativo
        )
}
