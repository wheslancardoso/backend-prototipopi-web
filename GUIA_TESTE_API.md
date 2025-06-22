# 🧪 Guia de Teste da API - Sistema de Teatro

## ✅ Status Atual

-   **Aplicação Spring Boot**: ✅ Rodando na porta 8080
-   **Banco de Dados**: ✅ Conectado (MySQL)
-   **Swagger UI**: ✅ Disponível em http://localhost:8080/api/swagger-ui.html

## 🎯 Como Testar a API

### 1. **Acesse o Swagger UI**

Abra no navegador: http://localhost:8080/api/swagger-ui.html

### 2. **Teste o Endpoint de Cadastro**

#### Via Swagger UI:

1. Clique em `POST /api/usuarios/cadastro`
2. Clique em "Try it out"
3. Cole o JSON abaixo:

```json
{
    "nome": "João Silva",
    "cpf": "123.456.789-00",
    "endereco": "Rua das Flores, 123",
    "telefone": "(11) 99999-9999",
    "email": "joao@email.com",
    "senha": "123456",
    "tipoUsuario": "COMUM"
}
```

4. Clique em "Execute"

#### Via cURL (no terminal):

```bash
curl -X POST http://localhost:8080/api/usuarios/cadastro \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "cpf": "123.456.789-00",
    "endereco": "Rua das Flores, 123",
    "telefone": "(11) 99999-9999",
    "email": "joao@email.com",
    "senha": "123456",
    "tipoUsuario": "COMUM"
  }'
```

### 3. **Teste o Endpoint de Listagem**

#### Via Swagger UI:

1. Clique em `GET /api/usuarios`
2. Clique em "Try it out"
3. Clique em "Execute"

#### Via cURL:

```bash
curl -X GET http://localhost:8080/api/usuarios
```

### 4. **Teste o Health Check**

```bash
curl http://localhost:8080/api/actuator/health
```

## 📊 Respostas Esperadas

### ✅ Cadastro Bem-sucedido (HTTP 201):

```json
{
    "id": 1,
    "nome": "João Silva",
    "cpf": "123.456.789-00",
    "email": "joao@email.com",
    "tipoUsuario": "COMUM"
}
```

### ⚠️ Usuário Já Existe (HTTP 409):

```json
{
    "timestamp": "2025-06-21T22:30:00",
    "status": 409,
    "error": "Conflict",
    "message": "Usuário já existe com este CPF ou email"
}
```

### ❌ Dados Inválidos (HTTP 400):

```json
{
    "timestamp": "2025-06-21T22:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Dados inválidos"
}
```

## 🔍 Endpoints Disponíveis

### Usuários:

-   `POST /api/usuarios/cadastro` - Cadastrar usuário
-   `GET /api/usuarios` - Listar usuários
-   `GET /api/usuarios/{id}` - Buscar usuário por ID
-   `PUT /api/usuarios/{id}` - Atualizar usuário
-   `DELETE /api/usuarios/{id}` - Deletar usuário

### Eventos:

-   `GET /api/eventos` - Listar eventos
-   `POST /api/eventos` - Criar evento
-   `GET /api/eventos/{id}` - Buscar evento por ID

### Sessões:

-   `GET /api/sessoes` - Listar sessões
-   `POST /api/sessoes` - Criar sessão
-   `GET /api/sessoes/{id}` - Buscar sessão por ID

### Áreas:

-   `GET /api/areas` - Listar áreas
-   `POST /api/areas` - Criar área
-   `GET /api/areas/{id}` - Buscar área por ID

### Ingressos:

-   `GET /api/ingressos` - Listar ingressos
-   `POST /api/ingressos` - Comprar ingresso
-   `GET /api/ingressos/{id}` - Buscar ingresso por ID

## 🛠️ Solução de Problemas

### Se o endpoint retornar 404:

1. Verifique se a aplicação está rodando: `mvn spring-boot:run`
2. Aguarde a mensagem "Started TeatroWebApplication"
3. Teste o health check primeiro

### Se o banco não conectar:

1. Verifique se o MySQL está rodando
2. Execute o script SQL: `mysql -u root -p < database_moderno.sql`
3. Verifique as configurações em `application.yml`

### Se o Swagger não abrir:

1. Tente: http://localhost:8080/api/swagger-ui/index.html
2. Verifique se não há erro de CORS
3. Limpe o cache do navegador

## 🎉 Próximos Passos

Após testar com sucesso:

1. ✅ Implementar autenticação JWT
2. ✅ Criar frontend React
3. ✅ Implementar sistema de pagamentos
4. ✅ Adicionar funcionalidades avançadas

---

**💡 Dica**: Use o Swagger UI para explorar todos os endpoints e testar a API de forma interativa!
