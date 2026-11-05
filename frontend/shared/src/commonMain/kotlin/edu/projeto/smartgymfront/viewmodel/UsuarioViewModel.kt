package edu.projeto.smartgymfront.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.projeto.smartgymfront.api.ApiException
import edu.projeto.smartgymfront.model.Usuario
import edu.projeto.smartgymfront.repositorio.UsuarioRepositorioRemoto
import edu.projeto.smartgymfront.ui.componentes.dataParaTexto
import edu.projeto.smartgymfront.ui.componentes.hoje
import edu.projeto.smartgymfront.ui.componentes.textoParaData
import edu.projeto.smartgymfront.ui.componentes.textoParaFloat
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repositorio: UsuarioRepositorioRemoto) : ViewModel() {

    var nome by mutableStateOf("")
    var email by mutableStateOf("")
    var senha by mutableStateOf("")
    var dataNascimento by mutableStateOf("")
    var pesoAtual by mutableStateOf("")
    var ativo by mutableStateOf(true)

    var lista by mutableStateOf(listOf<Usuario>())
        private set
    var idEdicao by mutableStateOf<Long?>(null)
        private set
    var erros by mutableStateOf(mapOf<String, String>())
        private set
    var mensagem by mutableStateOf<String?>(null)
        private set
    var sucesso by mutableStateOf(false)
        private set
    var carregando by mutableStateOf(false)
        private set

    init {
        carregar()
    }

    fun carregar() {
        viewModelScope.launch {
            try {
                lista = repositorio.listar()
            } catch (e: ApiException) {
                mostrarErro(e)
            }
        }
    }

    fun limpar() {
        nome = ""
        email = ""
        senha = ""
        dataNascimento = ""
        pesoAtual = ""
        ativo = true
        idEdicao = null
        erros = emptyMap()
        mensagem = null
    }

    fun editar(id: Long) {
        limpar()
        viewModelScope.launch {
            try {
                val registro = repositorio.buscarPorId(id)
                nome = registro.nome
                email = registro.email
                senha = ""
                dataNascimento = dataParaTexto(registro.dataNascimento)
                pesoAtual = registro.pesoAtual?.toString() ?: ""
                ativo = registro.ativo
                idEdicao = id
            } catch (e: ApiException) {
                mostrarErro(e)
            }
        }
    }

    fun gravar(aoConcluir: () -> Unit) {
        mensagem = null
        val usuario = validar() ?: return
        carregando = true
        viewModelScope.launch {
            try {
                val id = idEdicao
                val gravado = if (id == null) repositorio.criar(usuario) else repositorio.atualizar(id, usuario)
                limpar()
                sucesso = true
                mensagem = if (id == null) "Usuário gravado com sucesso (id ${gravado.id})"
                else "Usuário atualizado com sucesso (id ${gravado.id})"
                carregar()
                aoConcluir()
            } catch (e: ApiException) {
                erros = e.erros
                mostrarErro(e)
            } finally {
                carregando = false
            }
        }
    }

    fun apagar(id: Long) {
        viewModelScope.launch {
            try {
                repositorio.remover(id)
                if (idEdicao == id) limpar()
                sucesso = true
                mensagem = "Usuário apagado com sucesso (id $id)"
                carregar()
            } catch (e: ApiException) {
                mostrarErro(e)
            }
        }
    }

    private fun validar(): Usuario? {
        val novos = mutableMapOf<String, String>()
        if (nome.isBlank()) novos["nome"] = "O nome é obrigatório"
        else if (nome.length > 100) novos["nome"] = "O nome deve ter no máximo 100 caracteres"

        if (email.isBlank()) novos["email"] = "O e-mail é obrigatório"
        else if (!Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$").matches(email.trim())) novos["email"] = "E-mail inválido"
        else if (email.length > 150) novos["email"] = "O e-mail deve ter no máximo 150 caracteres"

        if (senha.isBlank()) novos["senha"] = "A senha é obrigatória"
        else if (senha.length !in 6..100) novos["senha"] = "A senha deve ter entre 6 e 100 caracteres"

        val data = if (dataNascimento.isBlank()) null else textoParaData(dataNascimento)
        if (dataNascimento.isNotBlank() && data == null) novos["dataNascimento"] = "Data inválida, use dd/mm/aaaa"
        else if (data != null && data >= hoje()) novos["dataNascimento"] = "A data de nascimento deve estar no passado"

        val peso = if (pesoAtual.isBlank()) null else textoParaFloat(pesoAtual)
        if (pesoAtual.isNotBlank() && peso == null) novos["pesoAtual"] = "Peso inválido"
        else if (peso != null && peso <= 0f) novos["pesoAtual"] = "O peso deve ser maior que zero"

        erros = novos
        if (novos.isNotEmpty()) return null
        return Usuario(
            nome = nome.trim(),
            email = email.trim(),
            senha = senha,
            dataNascimento = data,
            pesoAtual = peso,
            ativo = ativo
        )
    }

    private fun mostrarErro(e: ApiException) {
        sucesso = false
        mensagem = e.mensagem
    }
}
