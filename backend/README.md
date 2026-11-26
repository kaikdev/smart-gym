# Smart Gym – Backend

API REST do **Smart Gym**, desenvolvida em Kotlin com Spring Boot, Spring Security, JWT, Spring Data JPA e MySQL. Inclui o listener MQTT que recebe as leituras do sensor IoT.

## 1. Projeto

| | |
|---|---|
| **Projeto** | Smart Gym – Backend |
| **Tema** | Gestão inteligente de academias com monitoramento IoT da ocupação dos equipamentos |
| **Curso / Disciplina** | FATEC – DSM – Laboratório de Desenvolvimento Multiplataforma (Entrega Final – AV2) |
| **Repositório** | https://github.com/kaikdev/smart-gym |

## 2. Integrantes e divisão de responsabilidades

| Integrante | Entidades (entidade, repositório, serviço, controller, DTOs, mapper e validações) |
|---|---|
| Kaik Silva Sousa | `Usuario` e `Plano`, e a parte IoT: entidade `LeituraSensor`, repositório, serviço, controller e listener MQTT (`SensorMqttListener`) |
| Robert Guimarães Silva | `Exercicio` e `Equipamento` |
| Gabriel Vasques de Abreu | `Treino` e `AvaliacaoFisica` |
| Andrey de Mendonça | `Agendamento` e `Pagamento` |

## 3. Problema resolvido

Academias costumam controlar alunos, planos e pagamentos em planilhas e cadernos, e o aluno só descobre que um aparelho está ocupado quando chega nele. O backend do Smart Gym centraliza esses dados em uma API segura, protegida por login com JWT, e recebe em tempo real as leituras dos sensores de presença instalados nos equipamentos, informando se cada aparelho está livre ou em uso.

## 4. Entidades implementadas

| Entidade | Tabela | Campos | Validações (Bean Validation) |
|---|---|---|---|
| `Usuario` | `usuarios` | id (Long), nome (String), email (String), senha (String), dataNascimento (LocalDate), pesoAtual (Float), ativo (Boolean), perfil (String) | nome obrigatório (até 100), e-mail obrigatório e válido, senha de 6 a 100 caracteres, nascimento no passado, peso maior que zero, perfil `ADMIN` ou `ALUNO` |
| `Plano` | `planos` | id (Long), descricao (String), valorMensal (Double), mesesFidelidade (Int), taxaMatricula (Double), promocional (Boolean) | descrição obrigatória (até 100), valor mensal maior que zero, fidelidade e taxa não negativas |
| `Exercicio` | `exercicios` | id (Long), nome (String), grupoMuscular (String), series (Int), repeticoes (Int), cargaSugerida (Float), precisaAcompanhamento (Boolean) | nome e grupo muscular obrigatórios, mínimo de 1 série e 1 repetição, carga não negativa |
| `Equipamento` | `equipamentos` | id (Long), nome (String), fabricante (String), dataCompra (LocalDate), pesoSuportado (Double), codigoSensorIot (String), emUsoIot (Boolean) | nome obrigatório (até 100), data de compra não futura, peso suportado mínimo de 1, código do sensor até 50 caracteres e único |
| `Treino` | `treinos` | id (Long), titulo (String), objetivo (String), dataCriacao (LocalDate), duracaoEstimadaMin (Int), publico (Boolean) | título obrigatório (até 80), objetivo até 100, duração mínima de 1 minuto |
| `AvaliacaoFisica` | `avaliacoes_fisicas` | id (Long), avaliador (String), dataAvaliacao (LocalDate), altura (Float), imc (Float), percentualGordura (Double) | avaliador obrigatório, data não futura, altura e IMC maiores que zero, gordura entre 0 e 100 |
| `Agendamento` | `agendamentos` | id (Long), tipoAula (String), instrutor (String), dataHoraAgendada (LocalDateTime), capacidadeMaxima (Int), cancelado (Boolean) | tipo de aula obrigatório (até 60), data e hora obrigatórias e não passadas, capacidade mínima de 1 |
| `Pagamento` | `pagamentos` | id (Long), metodo (String), codigoTransacao (String), dataProcessamento (LocalDateTime), valorFinal (Double), confirmado (Boolean) | método obrigatório (até 40), código da transação até 100, valor final maior que zero |
| `LeituraSensor` (IoT) | `leituras_sensor` | id (Long), codigoSensor (String), emUso (Boolean), dataHoraLeitura (LocalDateTime), equipamentoId (Long) | gravada pelo listener MQTT; mensagens sem `codigoSensor` ou `emUso` são ignoradas |

## 5. Arquitetura em camadas

Código em `src/main/kotlin/com/smartgym/backend/`:

| Camada | Pasta | Responsabilidade |
|---|---|---|
| Controller | `controller/` | Rotas REST (`/api/...`) que recebem as requisições e devolvem as respostas |
| Service | `service/` | Regras de negócio: criar, listar, buscar por id, atualizar e remover, além de conflitos e registro não encontrado |
| Repository | `repository/` | Acesso ao banco com Spring Data JPA, incluindo as consultas por LIKE |
| Entity | `entity/` | Classes mapeadas para as tabelas do banco (`@Entity`, `@Id`, `@Column` etc.) |
| DTO | `dto/` | Objetos de entrada (`Request`, com as validações) e de saída (`Response`) |
| Mapper | `mapper/` | Conversões entre Entity e DTO |
| Security | `security/` | `SecurityConfig` (beans `passwordEncoder`, `AuthenticationManager` e regras do `HttpSecurity`), `JwtUtil` (gera e valida tokens e lê o usuário), filtro JWT e carregamento do usuário |
| MQTT | `mqtt/` | `SensorMqttListener`: conecta ao broker, assina o tópico e grava cada leitura |
| Exception | `exception/` | Exceções da aplicação e tratamento global de erros |
| Config | `config/` | Configuração do Swagger / OpenAPI e criação do usuário administrador inicial (`CargaInicial`) |

## 6. Rotas da API

Todas as rotas, exceto as de autenticação e do Swagger, exigem o cabeçalho `Authorization: Bearer <token>`.

| Recurso | Rotas | Acesso |
|---|---|---|
| Autenticação | `POST /api/auth/login`, `POST /api/auth/register` | Público |
| Usuários | `GET /api/usuarios`, `GET /api/usuarios/{id}`, `GET /api/usuarios/buscar?termo=`, `POST`, `PUT /{id}`, `DELETE /{id}` | Somente administrador |
| Pagamentos | `GET /api/pagamentos`, `GET /api/pagamentos/{id}`, `GET /api/pagamentos/buscar?termo=`, `POST`, `PUT /{id}`, `DELETE /{id}` | Somente administrador |
| Planos | `/api/planos` (mesmas rotas) | GET: qualquer usuário logado. POST, PUT e DELETE: administrador |
| Exercícios | `/api/exercicios` (mesmas rotas) | GET: qualquer usuário logado. POST, PUT e DELETE: administrador |
| Equipamentos | `/api/equipamentos` (mesmas rotas) | GET: qualquer usuário logado. POST, PUT e DELETE: administrador |
| Treinos | `/api/treinos` (mesmas rotas) | GET: qualquer usuário logado. POST, PUT e DELETE: administrador |
| Avaliações físicas | `/api/avaliacoes` (mesmas rotas) | GET: qualquer usuário logado. POST, PUT e DELETE: administrador |
| Agendamentos | `/api/agendamentos` (mesmas rotas) | GET: qualquer usuário logado. POST, PUT e DELETE: administrador |
| Leituras do sensor | `GET /api/leituras-sensor` (últimas 100), `GET /api/leituras-sensor/sensor/{codigoSensor}` | Qualquer usuário logado |

O login e o registro devolvem `{"token": "...", "perfil": "ADMIN" | "ALUNO", "tipo": "Bearer"}`. O registro sempre cria um usuário com perfil `ALUNO`. Sem token, a API responde `401`; sem permissão, responde `403`.

### Listener MQTT

| | |
|---|---|
| Broker | `tcp://broker.hivemq.com:1883` |
| Tópico | `fatec-dsm/smartgym/sensores/presenca` |
| Mensagem | `{"codigoSensor":"SENSOR-001","emUso":true}` |

Para cada mensagem recebida, o `LeituraSensorService` grava uma `LeituraSensor` e, se existir um equipamento com o mesmo `codigoSensorIot`, atualiza o `emUsoIot` dele. O listener reconecta sozinho se a conexão com o broker cair.

## 7. Documentação Swagger

Com o backend rodando, acesse http://localhost:8080/swagger-ui.html.

1. Faça `POST /api/auth/login` com `{"email": "admin@smartgym.com", "senha": "admin123"}`.
2. Copie o `token` da resposta, clique em **Authorize** e cole o token.
3. Teste as demais rotas. As leituras do sensor ficam no grupo **leitura-sensor-controller**.

As capturas de tela dos endpoints funcionando ficam na pasta `capturas-swagger/`.

## 8. Variáveis de ambiente

Nenhuma variável é obrigatória: os valores padrão ficam em `src/main/resources/application.properties`. Para sobrescrever, defina a variável correspondente antes de iniciar o backend.

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

## 9. Usuários e senhas para teste

| Perfil | E-mail | Senha | Observação |
|---|---|---|---|
| Administrador | `admin@smartgym.com` | `admin123` | Já vem pronto: o backend cria esse usuário automaticamente ao iniciar, caso ele ainda não exista no banco (classe `config/CargaInicial.kt`) |
| Aluno | `aluno@smartgym.com` | `aluno123` | Não vem pronto: crie com `POST /api/auth/register` ou pela tela de registro do app antes de usar |

O administrador é o único usuário que já vem cadastrado. As demais contas criadas pelo registro (`POST /api/auth/register`) sempre recebem o perfil Aluno; para tornar alguém administrador, entre como admin, edite a pessoa em **Usuários** e marque **Administrador**.

## 10. Instruções para execução

**Pré-requisitos:** Java 17 (JDK) e MySQL 8.

1. No MySQL, crie o banco:
   ```sql
   CREATE DATABASE smart_gym;
   ```
2. Se o usuário e a senha do MySQL não forem `root` / `admin`, ajuste o `application.properties` ou use as variáveis de ambiente da seção 8.
3. Na pasta `backend`, execute:
   ```bash
   ./gradlew bootRun
   ```
   No Windows: `.\gradlew.bat bootRun`.
4. As tabelas e o usuário administrador são criados automaticamente. No log devem aparecer `Started BackendApplication`, `Conectado ao broker MQTT` e `Inscrito no topico fatec-dsm/smartgym/sensores/presenca`.
5. A API fica em http://localhost:8080 e o Swagger em http://localhost:8080/swagger-ui.html.

**Com Docker:** na raiz do repositório, `docker compose up --build` sobe o MySQL e o backend juntos, sem precisar instalar Java nem MySQL.

**Tecnologias:** Kotlin 2.3, Java 17, Spring Boot 4.1.1 (Web MVC, Data JPA, Validation, Security), JJWT 0.13, BCrypt, MySQL 8, springdoc-openapi 3.1, Eclipse Paho MQTT 1.2.5 e Gradle 9.7.1.
