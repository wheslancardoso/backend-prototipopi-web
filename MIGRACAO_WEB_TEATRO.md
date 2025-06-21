# Migração do Sistema de Teatro para Web

## Índice

1. [Visão Geral](#visão-geral)
2. [Arquitetura Geral](#arquitetura-geral)
3. [Backend (Spring Boot)](#backend-spring-boot)
4. [Frontend (React/Vue)](#frontend-reactvue)
5. [Banco de Dados](#banco-de-dados)
6. [Funcionalidades Principais](#funcionalidades-principais)
7. [Funcionalidades Avançadas e Melhorias](#funcionalidades-avançadas-e-melhorias)
8. [Segurança](#segurança)
9. [Testes](#testes)
10. [Deploy e Monitoramento](#deploy-e-monitoramento)

---

## Visão Geral

-   Migrar o sistema desktop JavaFX + MySQL para uma aplicação web moderna.
-   Backend: Java + Spring Boot (REST API)
-   Frontend: React ou Vue.js
-   Banco: MySQL/MariaDB
-   Objetivo: Aprender tecnologias web, APIs, integração front-back e entregar um sistema robusto para apresentação acadêmica.

---

## Arquitetura Geral

```
Usuário <-> Frontend (React/Vue) <-> API REST (Spring Boot) <-> MySQL
```

-   **Separação total** entre front e back (API RESTful)
-   **Autenticação JWT**
-   **Padrão MVC + Service + Repository** no backend
-   **Componentização** no frontend

---

## Backend (Spring Boot)

### Estrutura de Pacotes

```
com.teatro.web
├── controller
├── service
├── model
├── repository
├── dto
├── security
├── exception
└── config
```

### Esqueleto de Implementação

-   **Configuração do projeto**: Spring Initializr, dependências Web, JPA, Security, Lombok, Validation, JWT, MySQL
-   **Entidades JPA**: Usuario, Evento, Sessao, Area, Poltrona, Ingresso
-   **DTOs**: Para comunicação entre front e back
-   **Controllers REST**: CRUD, autenticação, compra de ingresso, estatísticas
-   **Services**: Lógica de negócio
-   **Repositories**: Interfaces JPA
-   **Segurança**: JWT, roles, CORS
-   **Validações**: Bean Validation (javax.validation)
-   **Tratamento de exceções**: @ControllerAdvice
-   **Documentação**: Swagger/OpenAPI

#### Exemplo de Controller

```java
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginRequest req) {...}
    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioDTO> cadastrar(@RequestBody UsuarioDTO dto) {...}
}
```

#### Exemplo de Service

```java
@Service
public class IngressoService {
    public IngressoDTO comprarIngresso(CompraRequest req) {...}
    public List<IngressoDTO> listarPorUsuario(Long usuarioId) {...}
}
```

#### Exemplo de Repository

```java
public interface IngressoRepository extends JpaRepository<Ingresso, Long> {
    List<Ingresso> findByUsuarioId(Long usuarioId);
}
```

---

## Frontend (React/Vue)

### Estrutura de Componentes

-   **Auth**: Login, Cadastro, Recuperação de Senha
-   **Dashboard**: Usuário comum e admin
-   **Eventos**: Lista, CRUD (admin)
-   **Sessões**: Seleção de data/horário
-   **Áreas e Poltronas**: Seleção visual
-   **Compra**: Resumo, métodos de pagamento
-   **Pagamento**: Cartão, PIX (com QRCode)
-   **Ingressos**: Visualização e impressão (PDF)
-   **Estatísticas**: Gráficos e relatórios (admin)

### Esqueleto de Rotas

```js
<Route path="/login" component={Login} />
<Route path="/cadastro" component={Cadastro} />
<Route path="/dashboard" component={Dashboard} />
<Route path="/eventos" component={ListaEventos} />
<Route path="/eventos/:id" component={DetalheEvento} />
<Route path="/compra" component={CompraIngresso} />
<Route path="/ingressos" component={MeusIngressos} />
<Route path="/admin/estatisticas" component={Estatisticas} />
```

### Integração com API

-   **Axios/Fetch** para requisições
-   **Armazenamento do token JWT** (localStorage)
-   **Guards de rota** para proteger páginas

---

## Banco de Dados

-   **Tabelas**: usuarios, eventos, sessoes, areas, ingressos
-   **Relacionamentos**: conforme modelo atual
-   **Views**: estatísticas de vendas, ocupação, faturamento
-   **Scripts de migração**: Flyway/Liquibase
-   **Sugestão de melhorias**:
    -   Adicionar tabela de pagamentos
    -   Adicionar tabela de logs/auditoria
    -   Melhorar índices para performance

---

## Funcionalidades Principais

-   Cadastro/login/recuperação de senha
-   CRUD de eventos (admin)
-   CRUD de sessões (admin)
-   CRUD de áreas (admin)
-   Seleção de data e horário (com horários dinâmicos)
-   Seleção visual de poltronas
-   Compra de ingressos (múltiplas poltronas)
-   Pagamento (cartão, PIX)
-   Impressão de ingresso (PDF)
-   Visualização de ingressos comprados
-   Estatísticas e relatórios (admin)

---

## Funcionalidades Avançadas e Melhorias

-   **Reserva temporária de poltronas** (timeout)
-   **Notificações por email/SMS** (ex: SendGrid, Twilio)
-   **Sistema de pontos/fidelidade**
-   **Upload automático de posters de eventos** (ex: integração com API de imagens)
-   **Relatórios exportáveis (PDF, Excel)**
-   **Dashboard com gráficos interativos** (ex: Chart.js, Recharts)
-   **Internacionalização (i18n)**
-   **Tema escuro/claro**
-   **Acessibilidade (WCAG)**
-   **Responsividade mobile**
-   **API pública/documentada (Swagger)**
-   **Cache (Redis) para estatísticas**
-   **Auditoria de ações**
-   **Rate limiting**

---

## Segurança

-   Autenticação JWT
-   Hash de senhas (BCrypt)
-   Validação de entrada (backend e frontend)
-   CORS configurado
-   Proteção contra CSRF/XSS
-   Permissões por perfil (admin/comum)
-   Logs de acesso e auditoria

---

## Testes

-   **Backend**: JUnit + Mockito (unitários e integração)
-   **Frontend**: Jest/Testing Library/Cypress
-   **Testes de API**: Postman/Insomnia
-   **Testes de carga**: JMeter/K6

---

## Deploy e Monitoramento

-   **Docker** para backend e frontend
-   **CI/CD** (GitHub Actions, GitLab CI)
-   **Monitoramento**: Prometheus, Grafana
-   **Logs centralizados**: ELK Stack
-   **Backup automático do banco**
-   **Documentação viva**: Swagger/OpenAPI

---

## Roadmap Sugerido

1. Setup do repositório e ambiente
2. Modelagem do banco e entidades JPA
3. Implementação dos endpoints REST
4. Implementação do frontend (telas principais)
5. Integração front-back
6. Implementação de pagamentos
7. Estatísticas e relatórios
8. Funcionalidades avançadas
9. Testes e validação
10. Deploy e apresentação

---

## Anexos

-   [Script SQL original do banco](src/main/resources/database.sql)
-   [Views de estatísticas](src/main/resources/db/views_estatisticas.sql)

---

> **Dica:** Use este esqueleto como checklist e guia de implementação. Cada seção pode ser expandida em tarefas menores para facilitar o acompanhamento do desenvolvimento.
