# 🎭 Sistema de Teatro - API REST

## 📋 Visão Geral

Sistema de gerenciamento de eventos teatrais, sessões, áreas e venda de ingressos desenvolvido com Spring Boot.

## 🚀 Status do Projeto

### ✅ **IMPLEMENTADO (Backend - Etapa 1)**

#### 📦 **Configuração Base**

-   [x] **Maven (pom.xml)**: Dependências Spring Boot, JPA, Security, JWT, MySQL, Swagger
-   [x] **application.yml**: Configurações completas da aplicação
-   [x] **TeatroWebApplication**: Classe principal da aplicação

#### 🗄️ **Entidades JPA (Model)**

-   [x] **Usuario**: Autenticação, tipos (ADMIN/COMUM), validações
-   [x] **Evento**: Gerenciamento de eventos teatrais
-   [x] **Sessao**: **Lógica de horários dinâmicos** implementada
-   [x] **Area**: Configuração de preços e capacidades
-   [x] **Ingresso**: **Lógica de ocupação de poltronas** implementada

#### 🔍 **Repositories (Interfaces JPA)**

-   [x] **UsuarioRepository**: Autenticação por CPF/email
-   [x] **EventoRepository**: Busca de eventos ativos
-   [x] **SessaoRepository**: **Horários dinâmicos** e sessões disponíveis
-   [x] **AreaRepository**: Cálculo de disponibilidade
-   [x] **IngressoRepository**: **Ocupação de poltronas** com chave única

### 🔄 **PRÓXIMAS ETAPAS**

#### 🛠️ **Backend - Etapa 2**

-   [ ] **DTOs**: Objetos de transferência de dados
-   [ ] **Services**: Lógica de negócio
-   [ ] **Controllers**: Endpoints REST
-   [ ] **Security**: Autenticação JWT
-   [ ] **Exceptions**: Tratamento de erros

#### 🖥️ **Frontend - Etapa 3**

-   [ ] **React/Vue.js**: Interface do usuário
-   [ ] **Componentes**: Seleção de poltronas visual
-   [ ] **Autenticação**: Login/logout
-   [ ] **Integração**: Consumo da API

## 🎯 **Funcionalidades Implementadas**

### ⏰ **Lógica de Horários Dinâmicos**

✅ **Horários Fixos por Período:**

-   **Manhã**: 08:00, 09:30, 11:00
-   **Tarde**: 13:00, 14:30, 16:00
-   **Noite**: 18:00, 19:30, 21:00

✅ **Disponibilidade Inteligente:**

-   Se hoje é 13:00, apenas horários a partir das 14:30 ficam disponíveis
-   Datas futuras mostram todos os 9 horários
-   Datas passadas não podem ser selecionadas

### 🪑 **Lógica de Ocupação de Poltronas**

✅ **Chave Única de Ocupação:**

```
evento_id + data + horário + area_id + poltrona_id
```

✅ **Comportamento Implementado:**

-   Uma poltrona ocupada fica indisponível para TODOS os usuários
-   Se qualquer parâmetro muda (data, horário, área), a poltrona fica disponível novamente
-   Verificação em tempo real de disponibilidade

### 💰 **Configuração de Preços**

✅ **Áreas e Preços:**

-   **Plateia A**: 25 poltronas - R$ 40,00
-   **Plateia B**: 100 poltronas - R$ 60,00
-   **Camarotes**: 10 poltronas cada - R$ 80,00
-   **Frisas**: 5 poltronas cada - R$ 120,00
-   **Balcão Nobre**: 50 poltronas - R$ 250,00

## 🛠️ **Tecnologias Utilizadas**

### **Backend**

-   **Java 17**
-   **Spring Boot 3.2.0**
-   **Spring Data JPA**
-   **Spring Security**
-   **MySQL 8.0**
-   **JWT (JSON Web Tokens)**
-   **Swagger/OpenAPI**
-   **Lombok**
-   **Maven**

### **Frontend (Próximas Etapas)**

-   **React.js** ou **Vue.js**
-   **Axios/Fetch**
-   **TailwindCSS**
-   **React Router**

## 🚀 **Como Executar**

### **Pré-requisitos**

-   Java 17+
-   MySQL 8.0+
-   Maven 3.6+

### **1. Configurar Banco de Dados**

```sql
CREATE DATABASE teatro_db;
CREATE USER 'root'@'localhost' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON teatro_db.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

### **2. Executar o Projeto**

```bash
# Compilar
mvn clean compile

# Executar
mvn spring-boot:run
```

### **3. Acessar a Aplicação**

-   **API Base**: http://localhost:8080/api
-   **Swagger UI**: http://localhost:8080/api/swagger-ui.html
-   **Health Check**: http://localhost:8080/api/actuator/health

## 📁 **Estrutura do Projeto**

```
src/main/java/com/teatro/
├── TeatroWebApplication.java          # Classe principal
├── model/                             # Entidades JPA
│   ├── Usuario.java
│   ├── Evento.java
│   ├── Sessao.java                    # ✅ Horários dinâmicos
│   ├── Area.java
│   └── Ingresso.java                  # ✅ Ocupação de poltronas
├── repository/                        # Interfaces JPA
│   ├── UsuarioRepository.java
│   ├── EventoRepository.java
│   ├── SessaoRepository.java          # ✅ Consultas dinâmicas
│   ├── AreaRepository.java
│   └── IngressoRepository.java        # ✅ Lógica de ocupação
├── dto/                               # 🔄 Próxima etapa
├── service/                           # 🔄 Próxima etapa
├── controller/                        # 🔄 Próxima etapa
├── security/                          # 🔄 Próxima etapa
└── exception/                         # 🔄 Próxima etapa
```

## 🎯 **Próximos Passos**

### **Etapa 2: Completar Backend**

1. **DTOs**: Criar objetos de transferência
2. **Services**: Implementar lógica de negócio
3. **Controllers**: Criar endpoints REST
4. **Security**: Configurar autenticação JWT
5. **Exceptions**: Tratamento de erros
6. **Testes**: Testes unitários e integração

### **Etapa 3: Frontend**

1. **Setup React/Vue**: Configurar projeto frontend
2. **Autenticação**: Tela de login/cadastro
3. **Seleção de Eventos**: Lista de eventos disponíveis
4. **Seleção de Sessões**: Horários dinâmicos
5. **Seleção de Poltronas**: Interface visual
6. **Compra**: Fluxo de compra completo

## 📊 **Funcionalidades Core Implementadas**

### ✅ **Sistema de Horários**

-   Horários fixos por período (Manhã/Tarde/Noite)
-   Disponibilidade baseada na data/hora atual
-   Validação de datas passadas

### ✅ **Sistema de Poltronas**

-   Ocupação única por combinação de chaves
-   Verificação em tempo real
-   Disponibilidade dinâmica

### ✅ **Modelo de Dados**

-   Entidades bem estruturadas
-   Relacionamentos corretos
-   Validações implementadas

## 🔧 **Configurações**

### **application.yml**

```yaml
# Banco de Dados
spring.datasource.url: jdbc:mysql://localhost:3306/teatro_db
spring.datasource.username: root
spring.datasource.password: root

# JWT
jwt.secret: teatro_web_api_secret_key_2024
jwt.expiration: 86400000 # 24 horas

# Swagger
springdoc.swagger-ui.path: /swagger-ui.html
```

## 📝 **Comandos Úteis**

```bash
# Compilar
mvn clean compile

# Executar
mvn spring-boot:run

# Testes
mvn test

# Package
mvn clean package

# Verificar dependências
mvn dependency:tree
```

## 🎉 **Conquistas da Etapa 1**

✅ **Backend base completo**
✅ **Modelo de dados robusto**
✅ **Lógica de horários dinâmicos**
✅ **Sistema de ocupação de poltronas**
✅ **Repositories otimizados**
✅ **Configurações prontas**

O projeto está **pronto para a Etapa 2** com uma base sólida e todas as funcionalidades core implementadas! 🚀

---

**Próximo passo**: Implementar DTOs, Services e Controllers para expor a API REST.

## 📚 Documentação da API

### Swagger UI

Acesse a documentação interativa da API em:

-   **URL**: http://localhost:8080/api/swagger-ui.html
-   **Descrição**: Interface gráfica para testar todos os endpoints

### OpenAPI JSON

Especificação OpenAPI 3.0 disponível em:

-   **URL**: http://localhost:8080/api/api-docs
-   **Formato**: JSON para integração com ferramentas externas

### Endpoints Principais

#### 🔐 Autenticação

-   `POST /api/usuarios/login` - Login de usuário
-   `POST /api/usuarios/cadastro` - Cadastro de usuário
-   `POST /api/usuarios/recuperar-senha` - Recuperação de senha

#### 🎭 Eventos

-   `GET /api/eventos` - Listar eventos
-   `GET /api/eventos/ativos` - Listar eventos ativos
-   `POST /api/eventos` - Cadastrar evento
-   `PUT /api/eventos/{id}` - Atualizar evento
-   `DELETE /api/eventos/{id}` - Remover evento

#### 🎫 Sessões

-   `GET /api/sessoes` - Listar sessões
-   `GET /api/sessoes/evento/{eventoId}` - Sessões por evento
-   `POST /api/sessoes` - Cadastrar sessão
-   `PUT /api/sessoes/{id}` - Atualizar sessão

#### 🏛️ Áreas

-   `GET /api/areas` - Listar áreas
-   `GET /api/areas/sessao/{sessaoId}` - Áreas por sessão
-   `POST /api/areas` - Cadastrar área
-   `PUT /api/areas/{id}` - Atualizar área

#### 🛒 Ingressos

-   `POST /api/ingressos` - Comprar ingresso
-   `GET /api/ingressos/usuario/{usuarioId}` - Ingressos por usuário
-   `GET /api/ingressos/sessao/{sessaoId}` - Ingressos por sessão
-   `POST /api/ingressos/verificar-disponibilidade` - Verificar poltronas disponíveis
-   `GET /api/ingressos/validar/{codigo}` - Validar ingresso por código

## 🔧 Configurações

### application.yml

```yaml
server:
    port: 8080
    servlet:
        context-path: /api

spring:
    datasource:
        url: jdbc:mysql://localhost:3306/teatro_db
        username: root
        password: root

jwt:
    secret: sua_chave_secreta_aqui
    expiration: 86400000 # 24 horas
```

### Swagger/OpenAPI

```yaml
springdoc:
    api-docs:
        path: /api-docs
    swagger-ui:
        path: /swagger-ui.html
```

## 🧪 Testando a API

### 1. Acesse o Swagger UI

-   Abra http://localhost:8080/api/swagger-ui.html
-   Explore os endpoints disponíveis

### 2. Teste com Postman/Insomnia

-   Importe a especificação OpenAPI
-   Teste os endpoints individualmente

### 3. Exemplo de Login

```bash
curl -X POST "http://localhost:8080/api/usuarios/login" \
  -H "Content-Type: application/json" \
  -d '{
    "identificador": "admin@teatro.com",
    "senha": "admin123"
  }'
```

## 📊 Dados de Exemplo

### Usuários Padrão

-   **Admin**: admin@teatro.com / admin123
-   **Usuário**: usuario@teatro.com / usuario123

### Eventos Fixos

-   Hamlet
-   O Fantasma da Opera
-   O Auto da Compadecida

### Áreas e Preços

-   Plateia A: R$ 40,00 (25 poltronas)
-   Plateia B: R$ 60,00 (100 poltronas)
-   Camarotes: R$ 80,00 (10 poltronas cada)
-   Frisas: R$ 120,00 (5 poltronas cada)
-   Balcão Nobre: R$ 250,00 (50 poltronas)

## 🔒 Segurança

-   Autenticação JWT
-   Hash de senhas com BCrypt
-   Validação de entrada (Bean Validation)
-   Controle de acesso por roles
-   CORS configurado

## 🚀 Próximos Passos

1. **Frontend React**: Desenvolvimento da interface web
2. **Sistema de Pagamento**: Integração com PIX e cartão
3. **Notificações**: Email e SMS
4. **Reservas Temporárias**: Timeout para poltronas selecionadas
5. **Relatórios Avançados**: Exportação PDF/Excel
6. **Cache Redis**: Otimização de performance
7. **Testes Automatizados**: Cobertura completa
8. **Deploy**: Configuração de produção

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

## 👥 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Push para a branch
5. Abra um Pull Request

---

**Desenvolvido com ❤️ para o aprendizado de tecnologias web modernas**
