package edu.projeto.smartgymfront.navegacao

sealed interface Rota {
    data object Login : Rota
    data object Registro : Rota
    data object Menu : Rota
    data object Usuarios : Rota
    data object Planos : Rota
    data object Exercicios : Rota
    data object Equipamentos : Rota
    data object Treinos : Rota
    data object Avaliacoes : Rota
    data object Agendamentos : Rota
    data object Pagamentos : Rota
}
