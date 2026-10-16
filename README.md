# Smart Gym

Sistema de gestão inteligente para academias, com backend em Kotlin + Spring Boot e frontend multiplataforma (Android, Desktop e Web) em Kotlin Multiplatform + Compose Multiplatform.

## 1. Projeto

| | |
|---|---|
| **Projeto** | Smart Gym |
| **Curso / Disciplina** | FATEC – DSM – Laboratório de Desenvolvimento Multiplataforma (AV1) |

## 2. Repositório

https://github.com/kaikdev/smart-gym

## 3. Integrantes e responsabilidades

| Integrante | Entidades |
|---|---|
| Kaik Silva Sousa | `Usuario` e `Plano` |
| Robert Guimarães Silva | `Exercicio` e `Equipamento` |
| Gabriel Vasques de Abreu | `Treino` e `AvaliacaoFisica` |
| Andrey de Mendonça | `Agendamento` e `Pagamento` |

## 4. Sobre a aplicação

O **Smart Gym** é um sistema de gestão inteligente para academias que integra software e IoT: sensores de presença instalados nos aparelhos informam, em tempo real, se cada equipamento está **livre ou em uso**. Além disso, a plataforma centraliza o cadastro de alunos, planos, pagamentos, agendamentos, fichas de treino, exercícios e avaliações físicas.

### Problemas que a solução resolve

- **Filas e espera pelos aparelhos:** o aluno não sabe se um equipamento está ocupado até chegar nele. Com o status de cada aparelho em tempo real, informado pelos sensores, ele planeja a ordem do treino e perde menos tempo.
- **Lotação e horários de pico:** os agendamentos permitem distribuir melhor a frequência ao longo do dia, evitando superlotação.
- **Controle manual e descentralizado:** planilhas e cadernos são substituídos por um sistema único, com acesso protegido por login (JWT), para planos, pagamentos e cadastros.
- **Acompanhamento da evolução do aluno:** treinos e avaliações físicas (altura, IMC e percentual de gordura) ficam registrados, permitindo ajustar o treino com base em dados.
- **Gestão dos equipamentos:** cadastro com fabricante, data de compra e carga suportada, e dados de uso que ajudam a identificar aparelhos mais e menos utilizados, apoiando manutenção e futuras compras.

### Melhorias que traz

- Melhor experiência do aluno (menos espera e treino mais organizado).
- Decisões da gestão baseadas em dados de uso real dos aparelhos.
- Acesso pelo celular (Android), pelo computador (Desktop) e pelo navegador (Web) com o mesmo código.

### Alinhamento com os Objetivos de Desenvolvimento Sustentável (ODS – ONU)

- **ODS 3 – Saúde e Bem-Estar:** incentiva e organiza a prática regular de atividade física, com acompanhamento sistemático por meio de treinos e avaliações físicas.
- **ODS 9 – Indústria, Inovação e Infraestrutura:** leva inovação (IoT e software multiplataforma) para a gestão de pequenas e médias academias.
- **ODS 12 – Consumo e Produção Responsáveis:** o monitoramento de uso apoia a manutenção preventiva e o melhor aproveitamento dos aparelhos, prolongando sua vida útil e evitando compras desnecessárias.

## 5. Tecnologias utilizadas

**Backend**
- Kotlin 2.3 e Java 17
- Spring Boot 4.1.1 (Spring Web MVC, Spring Data JPA, Bean Validation)
- Spring Security + JWT (biblioteca JJWT 0.13) com senhas criptografadas em BCrypt
- MySQL 8 (banco `smart_gym`)
- Swagger / OpenAPI (springdoc-openapi) para documentação da API
- Gradle 9.7.1 (Kotlin DSL)

**Frontend**
- Kotlin Multiplatform (KMP) 2.4 e Compose Multiplatform 1.12 (Material 3)
- Alvos: Android, Desktop (JVM) e Web (Kotlin/Wasm e Kotlin/JS)
- Ktor Client (requisições HTTP) e kotlinx-serialization (JSON)
- kotlinx-datetime (datas)

**Infraestrutura**
- Docker e Docker Compose
- Git e GitHub

## 6. Arquitetura e pastas

O repositório tem duas pastas principais: `backend/` (API REST) e `frontend/` (aplicativo multiplataforma).

### Backend – arquitetura em camadas

Código em `backend/src/main/kotlin/com/smartgym/backend/`:

| Camada | Pasta | Responsabilidade |
|---|---|---|
| Controller | `controller/` | Rotas REST (`/api/...`) que recebem as requisições e devolvem as respostas |
| Service | `service/` | Regras de negócio (validações extras, conflitos, registro não encontrado) |
| Repository | `repository/` | Acesso ao banco com Spring Data JPA, incluindo as consultas por LIKE |
| Entity | `entity/` | Classes mapeadas para as tabelas do banco (`@Entity`, `@Column` etc.) |
| DTO | `dto/` | Objetos de entrada (`Request`, com as validações) e de saída (`Response`) |
| Mapper | `mapper/` | Conversões entre Entity e DTO |
| Security | `security/` | Spring Security, filtro e utilitário JWT, carregamento do usuário |
| Exception | `exception/` | Exceções da aplicação e tratamento global de erros |
| Config | `config/` | Configuração do Swagger / OpenAPI |

Configurações (banco e JWT) ficam em `backend/src/main/resources/application.properties`.

### Frontend – Kotlin Multiplatform

| Camada / módulo | Pasta | Responsabilidade |
|---|---|---|
| Código compartilhado | `frontend/shared/src/commonMain/kotlin/edu/projeto/smartgymfront/` | Código usado por todas as plataformas |
| Comunicação com a API | `.../smartgymfront/api/` | Cliente HTTP com JWT (`ApiClient`) e autenticação (`AuthApi`) |
| Modelos | `.../smartgymfront/model/` | Classes de dados das 8 entidades |
| Telas (UI) | `.../smartgymfront/ui/` | Login, menu e formulários de cada entidade |
| Componentes de UI | `.../smartgymfront/ui/componentes/` | Campos, botões, lista de registros e conversões de data |
| Código por plataforma | `frontend/shared/src/androidMain`, `jvmMain`, `jsMain`, `wasmJsMain` | Endereço do backend em cada plataforma |
| App Android | `frontend/androidApp/` | Ponto de entrada Android |
| App Desktop | `frontend/desktopApp/` | Ponto de entrada Desktop |
| App Web | `frontend/webApp/` | Ponto de entrada Web (porta 3000) |

## 7. Instalação, execução e acesso

### Opção A – Docker (recomendada)

Não é preciso ter Java nem MySQL instalados, apenas o **Docker Desktop** (no Windows, com WSL 2 habilitado).

1. Clone o repositório:
   ```bash
   git clone https://github.com/kaikdev/smart-gym.git
   cd smart-gym
   ```
2. Suba o banco e o backend (a primeira vez demora alguns minutos, porque baixa as imagens e compila o projeto):
   ```bash
   docker compose up --build
   ```
3. Acesse:
   - API: http://localhost:8080
   - Swagger (documentação e testes da API): http://localhost:8080/swagger-ui.html
4. (Opcional) Para subir também o frontend Web, em outro terminal:
   ```bash
   docker compose --profile web up --build
   ```
   e acesse http://localhost:3000. Essa etapa compila o frontend e é bem mais demorada.
5. Para parar: `Ctrl + C` e depois `docker compose down`. Para apagar também os dados do banco: `docker compose down -v`.

### Opção B – Execução manual

**Pré-requisitos:** Java 17 (JDK), MySQL 8 e, para o Android, o Android Studio.

**Backend**
1. No MySQL, crie o banco: `CREATE DATABASE smart_gym;`
2. Se o usuário/senha do seu MySQL não forem `root`/`admin`, ajuste `backend/src/main/resources/application.properties`.
3. Execute:
   ```bash
   cd backend
   ./gradlew bootRun
   ```
   (no Windows: `.\gradlew.bat bootRun`). As tabelas são criadas automaticamente.

**Frontend** (com o backend rodando)
```bash
cd frontend
./gradlew :desktopApp:run                    # Desktop
./gradlew :webApp:wasmJsBrowserDevelopmentRun # Web, em http://localhost:3000
```
Para Android, ajuste o IP do computador em `frontend/shared/src/androidMain/kotlin/edu/projeto/smartgymfront/Platform.android.kt` e rode o módulo `androidApp` pelo Android Studio.

### Primeiro acesso

1. No app, use **Criar conta** (ou `POST /api/auth/signup` no Swagger).
2. Faça login. O backend devolve um token JWT, exigido em todas as rotas, exceto as de autenticação.
3. No Swagger: faça `POST /api/auth/signin`, copie o `token`, clique em **Authorize** e cole o token. A partir daí as demais rotas podem ser testadas.
