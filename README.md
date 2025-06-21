# 🎭 Sistema de Teatro - API REST

## 📋 Visão Geral

Sistema de gerenciamento de eventos teatrais desenvolvido com **Spring Boot** e **MySQL**. O projeto implementa uma API REST completa para venda de ingressos com lógica avançada de ocupação de poltronas e horários dinâmicos.

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
