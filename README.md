# Gerenciador de Projetos

API REST para gerenciamento de projetos, desenvolvida com Spring Boot como parte do desafio técnico.

## Tecnologias

- Java 17
- Spring Boot 4.0.3
- Spring Data JPA + Hibernate
- Spring Security (HTTP Basic)
- PostgreSQL
- Swagger/OpenAPI (Springdoc)
- JUnit 5 + Mockito
- Maven

## Pré-requisitos

- Java 17+
- PostgreSQL (local ou via Docker)
- Maven 3.9+

## Configuração do Banco de Dados

### Opção 1 — Criar manualmente

Execute o script `bancodedados.sql` no PostgreSQL:

```bash
psql -U postgres -f bancodedados.sql
```

Isso cria o usuário `gerenciador`, o banco `gerenciadorprojetos` e as tabelas `membro`, `projeto` e `projeto_membro`.

### Configuração de conexão

Edite `src/main/resources/application.properties` se necessário:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/gerenciadorprojetos
spring.datasource.username=gerenciador
spring.datasource.password=gerenciador
```

## Executando

```bash
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

## Swagger UI

Acesse a documentação interativa da API em:

```
http://localhost:8080/swagger-ui.html
```

O Swagger é público. Os demais endpoints exigem autenticação HTTP Basic:

| Usuário | Senha |
|---------|-------|
| `admin` | `admin` |

## Endpoints

### Projetos — `/api/projetos`

| Método | Path | Descrição |
|--------|------|-----------|
| `POST` | `/api/projetos` | Criar projeto |
| `PUT` | `/api/projetos/{id}` | Atualizar projeto |
| `GET` | `/api/projetos/{id}` | Buscar por ID |
| `GET` | `/api/projetos?nome=&status=&page=0&size=10` | Listar (paginado + filtros) |
| `DELETE` | `/api/projetos/{id}` | Excluir projeto |
| `PATCH` | `/api/projetos/{id}/status?novoStatus=` | Alterar status |

### Membros (API mock externa) — `/api/membros-externos`

| Método | Path | Descrição |
|--------|------|-----------|
| `POST` | `/api/membros-externos` | Criar membro |
| `GET` | `/api/membros-externos` | Listar membros |
| `GET` | `/api/membros-externos/{id}` | Buscar membro por ID |

### Alocação de Membros — `/api/projetos/{projetoId}/membros`

| Método | Path | Descrição |
|--------|------|-----------|
| `POST` | `/api/projetos/{projetoId}/membros/{membroId}?papel=` | Alocar membro |
| `DELETE` | `/api/projetos/{projetoId}/membros/{membroId}` | Desalocar membro |

### Relatório — `/api/relatorios/portfolio`

| Método | Path | Descrição |
|--------|------|-----------|
| `GET` | `/api/relatorios/portfolio` | Resumo do portfólio |

## Regras de Negócio

### Classificação de Risco (calculado automaticamente)

| Risco | Condição |
|-------|----------|
| BAIXO | Orçamento ≤ R$ 100.000 **e** prazo ≤ 3 meses |
| MÉDIO | Orçamento entre R$ 100.001 e R$ 500.000 **ou** prazo entre 3 e 6 meses |
| ALTO | Orçamento > R$ 500.000 **ou** prazo > 6 meses |

### Fluxo de Status

```
EM_ANALISE → ANALISE_REALIZADA → ANALISE_APROVADA → INICIADO → PLANEJADO → EM_ANDAMENTO → ENCERRADO
```

- **CANCELADO** pode ser aplicado a partir de qualquer status ativo (exceto ENCERRADO e CANCELADO).
- Não é permitido pular etapas.
- Projetos com status **INICIADO**, **EM_ANDAMENTO** ou **ENCERRADO** não podem ser excluídos.

### Alocação de Membros

- Apenas membros com atribuição **"funcionário"** podem ser associados a projetos.
- Mínimo de **1** e máximo de **10** membros por projeto.
- Um membro pode estar em no máximo **3 projetos ativos** simultaneamente (status ≠ ENCERRADO/CANCELADO).

### Relatório de Portfólio

Retorna:
- Quantidade de projetos por status
- Total orçado por status
- Média de duração dos projetos encerrados (em dias)
- Total de membros únicos alocados

## Testes

```bash
./mvnw test
```

Testes unitários cobrindo:
- Cálculo de risco (5 cenários)
- Regras de exclusão (2 cenários)
- Transição de status (4 cenários)
- Regras de alocação de membros (6 cenários)

## Estrutura do Projeto

```
src/main/java/com/br/gerenciadorprojetos/
├── config/              # SecurityConfig, OpenApiConfig, GlobalExceptionHandler
├── controller/          # ProjetoController, MembroMockController, etc.
├── domain/
│   ├── entity/          # Projeto, Membro, ProjetoMembro
│   ├── enums/           # ProjetoStatus, ProjetoRisco
│   ├── repository/      # JPA Repositories
│   └── specification/   # Filtros dinâmicos (Specification)
├── dto/                 # Request/Response DTOs
│   └── relatorio/       # DTOs do relatório
└── service/             # Regras de negócio
```