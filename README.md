# Smart Gym

Sistema de gestão inteligente para academias. O projeto tem três partes: um backend em Kotlin + Spring Boot, um frontend multiplataforma (Android, Desktop e Web) em Kotlin Multiplatform + Compose Multiplatform, e um dispositivo IoT com ESP32 e sensor de presença que envia leituras por MQTT.

**Landing page do projeto:** https://kaikdev.github.io/smart-gym/

## 1. Projeto

| | |
|---|---|
| **Projeto** | Smart Gym |
| **Tema** | Gestão inteligente de academias com monitoramento IoT da ocupação dos equipamentos |
| **Curso / Disciplina** | FATEC – DSM – Laboratório de Desenvolvimento Multiplataforma (Entrega Final – AV2) |
| **Repositório** | https://github.com/kaikdev/smart-gym |

## 2. Integrantes e divisão de responsabilidades

Cada integrante foi responsável por duas entidades, do backend (entidade, repositório, serviço, controller, DTOs e validações) ao frontend (modelo, RepositorioRemoto, ViewModel, telas de listagem e formulário).

| Integrante | Entidades |
|---|---|
| Kaik Silva Sousa | `Usuario` e `Plano`, e a parte IoT: dispositivo ESP32, entidade `LeituraSensor` e listener MQTT |
| Robert Guimarães Silva | `Exercicio` e `Equipamento` |
| Gabriel Vasques de Abreu | `Treino` e `AvaliacaoFisica` |
| Andrey de Mendonça | `Agendamento` e `Pagamento` |

## 3. Sobre a aplicação

O **Smart Gym** é um sistema de gestão inteligente para academias que integra software e IoT. Sensores de presença instalados nos aparelhos informam, em tempo real, se cada equipamento está **livre ou em uso**. Além disso, a plataforma centraliza o cadastro de alunos, planos, pagamentos, agendamentos, fichas de treino, exercícios e avaliações físicas.

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

## 4. Entidades implementadas

| Entidade | Tabela | Campos | Responsável |
|---|---|---|---|
| `Usuario` | `usuarios` | id (Long), nome (String), email (String), senha (String), dataNascimento (LocalDate), pesoAtual (Float), ativo (Boolean), perfil (String) | Kaik |
| `Plano` | `planos` | id (Long), descricao (String), valorMensal (Double), mesesFidelidade (Int), taxaMatricula (Double), promocional (Boolean) | Kaik |
| `Exercicio` | `exercicios` | id (Long), nome (String), grupoMuscular (String), series (Int), repeticoes (Int), cargaSugerida (Float), precisaAcompanhamento (Boolean) | Robert |
| `Equipamento` | `equipamentos` | id (Long), nome (String), fabricante (String), dataCompra (LocalDate), pesoSuportado (Double), codigoSensorIot (String), emUsoIot (Boolean) | Robert |
| `Treino` | `treinos` | id (Long), titulo (String), objetivo (String), dataCriacao (LocalDate), duracaoEstimadaMin (Int), publico (Boolean) | Gabriel |
| `AvaliacaoFisica` | `avaliacoes_fisicas` | id (Long), avaliador (String), dataAvaliacao (LocalDate), altura (Float), imc (Float), percentualGordura (Double) | Gabriel |
| `Agendamento` | `agendamentos` | id (Long), tipoAula (String), instrutor (String), dataHoraAgendada (LocalDateTime), capacidadeMaxima (Int), cancelado (Boolean) | Andrey |
| `Pagamento` | `pagamentos` | id (Long), metodo (String), codigoTransacao (String), dataProcessamento (LocalDateTime), valorFinal (Double), confirmado (Boolean) | Andrey |
| `LeituraSensor` (IoT) | `leituras_sensor` | id (Long), codigoSensor (String), emUso (Boolean), dataHoraLeitura (LocalDateTime), equipamentoId (Long) | Kaik |

A `LeituraSensor` não tem CRUD no frontend: ela é gravada automaticamente pelo listener MQTT a cada mensagem do sensor e pode ser consultada pela API.

## 5. Perfis de acesso

| Perfil | Como é criado | O que pode fazer |
|---|---|---|
| **Administrador** (`ADMIN`) | `admin@smartgym.com` é criado automaticamente ao iniciar o backend. Outros usuários podem ser marcados como "Administrador" no cadastro de usuários | Tudo: cadastrar, editar e excluir todos os registros, incluindo usuários e pagamentos |
| **Aluno** (`ALUNO`) | Pela tela de registro do app (`POST /api/auth/register`) | Apenas consultar equipamentos (livre/em uso), exercícios, treinos, avaliações, planos e agendamentos. Não vê usuários nem pagamentos |

A regra é aplicada no backend (Spring Security responde `403` para ações não permitidas) e no frontend (menu e botões de criar, editar e apagar ficam ocultos para o aluno).

## 6. Usuários e senhas para teste

| Perfil | E-mail | Senha | Observação |
|---|---|---|---|
| Administrador | `admin@smartgym.com` | `admin123` | Já vem pronto: o backend cria esse usuário automaticamente ao iniciar, caso ele ainda não exista no banco |
| Aluno | `aluno@smartgym.com` | `aluno123` | Não vem pronto: crie pela tela **Ir para o registro** do app antes de usar |

O administrador é o único usuário que já vem cadastrado. As demais contas criadas pela tela de registro sempre recebem o perfil Aluno; para tornar alguém administrador, entre como admin, edite a pessoa em **Usuários** e marque **Administrador**.

## 7. Tecnologias utilizadas

**Backend**
- Kotlin 2.3 e Java 17
- Spring Boot 4.1.1 (Spring Web MVC, Spring Data JPA, Bean Validation)
- Spring Security + JWT (biblioteca JJWT 0.13) com senhas criptografadas em BCrypt
- MySQL 8 (banco `smart_gym`)
- Swagger / OpenAPI (springdoc-openapi) para documentação da API
- Eclipse Paho MQTT 1.2.5 (listener do broker)
- Gradle 9.7.1 (Kotlin DSL)

**Frontend**
- Kotlin Multiplatform (KMP) 2.4 e Compose Multiplatform 1.12 (Material 3)
- Alvos: Android, Desktop (JVM) e Web (Kotlin/Wasm e Kotlin/JS)
- Ktor Client (requisições HTTP) e kotlinx-serialization (JSON)
- kotlinx-datetime (datas)
- Navigation3 (navegação entre as telas, em dois níveis)
- ViewModel (lifecycle-viewmodel-compose) para o estado de cada CRUD
- Material Icons (core e extended)

**IoT**
- ESP32 DevKit C v4 e sensor de presença PIR HC-SR501
- Arduino (C++) com as bibliotecas WiFi e PubSubClient
- MQTT com o broker público HiveMQ (`broker.hivemq.com:1883`)
- Simulação no Wokwi

**Infraestrutura**
- Docker e Docker Compose
- Git, GitHub e GitHub Pages (landing page)

## 8. Arquitetura e pastas

```
smart-gym/
├── backend/    API REST em Spring Boot (README próprio)
├── frontend/   Aplicativo Kotlin Multiplatform (README próprio)
├── iot/        Código do ESP32 e circuito do Wokwi (README próprio)
├── landing/    Landing page de apresentação (HTML, CSS e JavaScript)
└── docker-compose.yml
```

### Fluxo do IoT

```
ESP32 + PIR --publica--> broker.hivemq.com --entrega--> Listener Spring Boot --grava--> MySQL --consulta--> App
            tópico: fatec-dsm/smartgym/sensores/presenca
```

1. O ESP32 lê o sensor PIR e publica, a cada 2 segundos e a cada mudança, a mensagem `{"codigoSensor":"SENSOR-001","emUso":true}`.
2. O `SensorMqttListener` do backend assina o tópico e, para cada mensagem, grava uma `LeituraSensor` no banco.
3. Se existir um `Equipamento` com o mesmo `codigoSensorIot`, o campo `emUsoIot` dele é atualizado.
4. No app, a tela de Equipamentos mostra cada aparelho como **Livre** ou **Em uso** (botão **Atualizar status dos equipamentos**).

Os detalhes de cada camada estão nos READMEs de [backend](backend/README.md), [frontend](frontend/README.md) e [iot](iot/README.md).

## 9. Variáveis de ambiente

Nenhuma variável é obrigatória: os valores padrão ficam em `backend/src/main/resources/application.properties`. Para sobrescrever algum valor, defina a variável de ambiente correspondente antes de iniciar o backend.

| Variável | Valor padrão | Descrição |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/smart_gym` | Endereço do banco MySQL |
| `SPRING_DATASOURCE_USERNAME` | `root` | Usuário do banco |
| `SPRING_DATASOURCE_PASSWORD` | `admin` | Senha do banco |
| `JWT_SECRET` | chave Base64 do `application.properties` | Chave usada para assinar os tokens JWT (mínimo de 32 bytes) |
| `JWT_EXPIRATIONMS` | `3600000` | Validade do token em milissegundos (1 hora) |
| `MQTT_BROKERURL` | `tcp://broker.hivemq.com:1883` | Endereço do broker MQTT |
| `MQTT_TOPICO` | `fatec-dsm/smartgym/sensores/presenca` | Tópico assinado pelo listener |
| `MQTT_CLIENTID` | `smartgym-backend` | Prefixo do identificador do cliente MQTT |

No Docker Compose, as variáveis `SPRING_DATASOURCE_*` já estão configuradas para o container do MySQL.

No frontend, o endereço do backend fica em `frontend/shared/src/<plataforma>Main/kotlin/edu/projeto/smartgymfront/Platform.<plataforma>.kt` (`http://localhost:8080` no Desktop e na Web). No dispositivo IoT, o Wi-Fi, o broker, o tópico e o código do sensor são constantes no início de `iot/sketch.ino`.

## 10. Instalação, execução e acesso

### Opção A – Docker

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
2. Se o usuário/senha do seu MySQL não forem `root`/`admin`, ajuste `backend/src/main/resources/application.properties` ou use as variáveis de ambiente da seção 9.
3. Execute:
   ```bash
   cd backend
   ./gradlew bootRun
   ```
   (no Windows: `.\gradlew.bat bootRun`). As tabelas e o usuário administrador são criados automaticamente.

**Frontend** (com o backend rodando)
```bash
cd frontend
./gradlew :desktopApp:run                    # Desktop
./gradlew :webApp:wasmJsBrowserDevelopmentRun # Web, em http://localhost:3000
```
(no Windows: `.\gradlew.bat`). Para Android, ajuste o IP do computador em `frontend/shared/src/androidMain/kotlin/edu/projeto/smartgymfront/Platform.android.kt` e rode o módulo `androidApp` pelo Android Studio.

**IoT**

Abra o projeto no Wokwi, inicie a simulação e acione o sensor. O passo a passo completo está em [iot/README.md](iot/README.md).

### Primeiro acesso

1. Não é preciso cadastrar o administrador: ao iniciar, o backend cria `admin@smartgym.com` / `admin123` se ele ainda não existir. Entre com esse usuário para ter acesso completo.
2. Para testar o perfil de aluno, clique em **Ir para o registro** e crie uma conta (por exemplo, `aluno@smartgym.com` / `aluno123`). O registro sempre cria um aluno, já devolve o token e abre o menu.
3. O backend devolve um token JWT, exigido em todas as rotas, exceto as de autenticação.
4. No Swagger: faça `POST /api/auth/login`, copie o `token`, clique em **Authorize** e cole o token. A partir daí as demais rotas podem ser testadas.

### Teste completo com o sensor

1. Com o backend rodando, confira no log as linhas `Conectado ao broker MQTT` e `Inscrito no topico fatec-dsm/smartgym/sensores/presenca`.
2. Entre como administrador e cadastre um equipamento com **Código do sensor IoT = `SENSOR-001`**.
3. Inicie a simulação no Wokwi e clique em **Simulate motion** no sensor PIR.
4. No log do backend aparece `Leitura gravada: sensor=SENSOR-001 emUso=true`. No app, clique em **Atualizar status dos equipamentos** para ver o aparelho **Em uso**. Ele volta para **Livre** 30 segundos depois do último movimento.
5. Consulte o histórico em `GET /api/leituras-sensor` no Swagger ou na tabela `leituras_sensor` do banco.

## 11. Entregáveis

| Arquivo | Conteúdo |
|---|---|
| `backend.zip` | Pasta `backend/` com o projeto Spring Boot, o [README do backend](backend/README.md) e as capturas de tela do Swagger |
| `frontend-kmp.zip` | Pasta `frontend/` com o projeto Kotlin Multiplatform, o [README do frontend](frontend/README.md) e o vídeo de demonstração |
| `iot.zip` | Pasta `iot/` com o código do ESP32, o circuito do Wokwi e o [README do IoT](iot/README.md) |
