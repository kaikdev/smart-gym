# Smart Gym – Frontend (Kotlin Multiplatform)

Aplicativo do **Smart Gym** para **Android, Desktop e Web**, feito com Kotlin Multiplatform e Compose Multiplatform. Ele consome a API REST do backend, com login por JWT, e mostra em tempo real quais equipamentos estão livres ou em uso, de acordo com os sensores IoT.

## 1. Projeto

| | |
|---|---|
| **Projeto** | Smart Gym – Frontend KMP |
| **Tema** | Gestão inteligente de academias com monitoramento IoT da ocupação dos equipamentos |
| **Curso / Disciplina** | FATEC – DSM – Laboratório de Desenvolvimento Multiplataforma (Entrega Final – AV2) |
| **Repositório** | https://github.com/kaikdev/smart-gym |

## 2. Integrantes e divisão de responsabilidades

| Integrante | CRUDs (modelo, RepositorioRemoto, ViewModel, listagem e formulário) |
|---|---|
| Kaik Silva Sousa | `Usuario` e `Plano` |
| Robert Guimarães Silva | `Exercicio` e `Equipamento` |
| Gabriel Vasques de Abreu | `Treino` e `AvaliacaoFisica` |
| Andrey de Mendonça | `Agendamento` e `Pagamento` |

## 3. Problema resolvido

O aluno não sabe se um aparelho está ocupado até chegar nele, e a gestão da academia depende de controles manuais. O aplicativo reúne em um só lugar, no celular, no computador e no navegador, o cadastro de alunos, planos, pagamentos, agendamentos, treinos, exercícios e avaliações físicas, além do status de cada equipamento informado pelos sensores.

## 4. Entidades implementadas

| Entidade | Campos | Tela de listagem (card) |
|---|---|---|
| `Usuario` | id (Long), nome (String), email (String), senha (String), dataNascimento (LocalDate), pesoAtual (Float), ativo (Boolean), perfil (String) | e-mail, nascimento, peso, situação e perfil |
| `Plano` | id (Long), descricao (String), valorMensal (Double), mesesFidelidade (Int), taxaMatricula (Double), promocional (Boolean) | valor, fidelidade, taxa e promoção |
| `Exercicio` | id (Long), nome (String), grupoMuscular (String), series (Int), repeticoes (Int), cargaSugerida (Float), precisaAcompanhamento (Boolean) | grupo muscular, séries x repetições, carga e acompanhamento |
| `Equipamento` | id (Long), nome (String), fabricante (String), dataCompra (LocalDate), pesoSuportado (Double), codigoSensorIot (String), emUsoIot (Boolean) | fabricante, data de compra, peso, sensor e status Livre/Em uso |
| `Treino` | id (Long), titulo (String), objetivo (String), dataCriacao (LocalDate), duracaoEstimadaMin (Int), publico (Boolean) | objetivo, duração, data de criação e visibilidade |
| `AvaliacaoFisica` | id (Long), avaliador (String), dataAvaliacao (LocalDate), altura (Float), imc (Float), percentualGordura (Double) | data, altura, IMC e gordura |
| `Agendamento` | id (Long), tipoAula (String), instrutor (String), dataHoraAgendada (LocalDateTime), capacidadeMaxima (Int), cancelado (Boolean) | data e hora, instrutor, capacidade e situação |
| `Pagamento` | id (Long), metodo (String), codigoTransacao (String), dataProcessamento (LocalDateTime), valorFinal (Double), confirmado (Boolean) | valor, código da transação, data e situação |

## 5. Funcionalidades e organização do código

Código compartilhado em `shared/src/commonMain/kotlin/edu/projeto/smartgymfront/`:

| Pasta | Conteúdo |
|---|---|
| `ui/LoginScreen.kt` | Tela de login: e-mail, senha oculta (com botão para mostrar), botões **Entrar** e **Ir para o registro**, logo e link para a landing page |
| `ui/RegistroScreen.kt` | Tela de registro: nome, e-mail, senha e confirmação ocultas, botões **Registrar** e **Ir para o login** |
| `api/` | `ApiClient` (Ktor com o token JWT em cada requisição) e `AuthApi` (rotas `/login` e `/register`) |
| `sessao/SessaoUsuario.kt` | Estado compartilhado da aplicação com o token JWT, o e-mail e o perfil do usuário logado |
| `navegacao/` | Navigation3 em dois níveis: `Rota` (Login, Registro, Menu e os CRUDs) e `RotaCrud` (Listagem e Formulário de cada CRUD) |
| `model/` | Classes de dados das 8 entidades |
| `repositorio/` | Um `RepositorioRemoto` por entidade, com GET (listar e buscar por id), POST, PUT e DELETE |
| `viewmodel/` | Um ViewModel por CRUD, com estado para cada campo, a lista de registros e as funções carregar, gravar, editar, apagar e limpar |
| `ui/<entidade>/` | Telas de **Listagem** (cards com ícones de editar e apagar) e **Formulário** (botões **Gravar** e **Limpar Campos**) |
| `ui/componentes/` | Campos de texto, botões, card de registro e conversões de data |
| `ui/tema/Tema.kt` | Esquema de cores do app (Material 3) |

Ao apagar, o app envia um DELETE e chama `carregar()` para atualizar a lista. Ao editar, o formulário abre preenchido e o **Gravar** envia um PUT, também seguido de `carregar()`.

### Perfis de acesso

- **Administrador:** vê os 8 CRUDs e pode criar, editar e apagar registros. No cadastro de usuários, pode marcar outra pessoa como **Administrador**.
- **Aluno:** vê apenas Planos, Treinos, Avaliações Físicas, Agendamentos, Exercícios e Equipamentos, somente para consulta. O botão de novo registro e os ícones de editar e apagar ficam ocultos.

Na tela de **Equipamentos**, o botão **Atualizar status dos equipamentos** busca de novo os dados do backend para mostrar o status mais recente enviado pelo sensor.

## 6. Variáveis de ambiente

O frontend não usa variáveis de ambiente. O endereço do backend é definido por plataforma:

| Plataforma | Arquivo | Valor |
|---|---|---|
| Desktop | `shared/src/jvmMain/kotlin/edu/projeto/smartgymfront/Platform.jvm.kt` | `http://localhost:8080` |
| Web (Wasm) | `shared/src/wasmJsMain/kotlin/edu/projeto/smartgymfront/Platform.wasmJs.kt` | `http://localhost:8080` |
| Web (JS) | `shared/src/jsMain/kotlin/edu/projeto/smartgymfront/Platform.js.kt` | `http://localhost:8080` |
| Android | `shared/src/androidMain/kotlin/edu/projeto/smartgymfront/Platform.android.kt` | IP do computador na rede, por exemplo `http://192.168.0.10:8080` |

A versão Web precisa rodar em `http://localhost:3000`, que é a origem liberada no CORS do backend.

## 7. Usuários e senhas para teste

| Perfil | E-mail | Senha | Observação |
|---|---|---|---|
| Administrador | `admin@smartgym.com` | `admin123` | Criado automaticamente pelo backend |
| Aluno | `aluno@smartgym.com` | `aluno123` | Exemplo: crie pela tela **Ir para o registro** |

## 8. Instruções para execução

**Pré-requisitos:** o backend rodando em `http://localhost:8080` (veja o README do backend) e um JDK instalado. Na primeira execução, o Gradle baixa as dependências e o JDK 21 configurado no projeto, o que pode levar alguns minutos. Para Android, também é preciso o Android Studio.

Na pasta `frontend`:

| Plataforma | Comando |
|---|---|
| Desktop | `./gradlew :desktopApp:run` |
| Web (Wasm) | `./gradlew :webApp:wasmJsBrowserDevelopmentRun` e acesse http://localhost:3000 |
| Web (JS) | `./gradlew :webApp:jsBrowserDevelopmentRun` |
| Android | ajuste o IP em `Platform.android.kt` e rode o módulo `androidApp` pelo Android Studio (ou `./gradlew :androidApp:assembleDebug`) |

No Windows, use `.\gradlew.bat` no lugar de `./gradlew`.

Também é possível subir a versão Web com Docker, a partir da raiz do repositório: `docker compose --profile web up --build`.

### Roteiro de teste

1. Entre como administrador e abra **Equipamentos**. Cadastre um equipamento com **Código do sensor IoT = `SENSOR-001`**.
2. Inicie a simulação do ESP32 no Wokwi e acione o sensor PIR.
3. Clique em **Atualizar status dos equipamentos**: o equipamento aparece **Em uso**.
4. Saia, crie uma conta de aluno pelo registro e confira que o menu e as listas ficam somente para consulta.

## 9. Vídeo de demonstração

O vídeo de até 5 minutos com o sistema completo em execução acompanha este projeto no arquivo `frontend-kmp.zip`.

**Tecnologias:** Kotlin Multiplatform 2.4, Compose Multiplatform 1.12 (Material 3), Navigation3, ViewModel (lifecycle-viewmodel-compose), Ktor Client, kotlinx-serialization, kotlinx-datetime e Material Icons.
