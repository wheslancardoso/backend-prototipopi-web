# 🔧 Correção dos Endpoints - Sistema de Teatro

## 🎯 Problema Identificado

O erro 404 estava ocorrendo porque havia duplicação do path `/api`:

-   **Context-path**: `/api` (configurado no `application.yml`)
-   **RequestMapping**: `/api/usuarios` (nos controllers)

Isso resultava em URLs como: `/api/api/usuarios/cadastro` ❌

## ✅ Correção Aplicada

Removido `/api` dos `@RequestMapping` dos controllers:

### Antes:

```java
@RequestMapping("/api/usuarios")
```

### Depois:

```java
@RequestMapping("/usuarios")
```

## 🚀 Como Aplicar a Correção

### 1. **Parar a aplicação atual**

No terminal onde está rodando o Spring Boot, pressione `Ctrl+C`

### 2. **Reiniciar a aplicação**

```bash
mvn spring-boot:run
```

### 3. **Aguardar inicialização**

Aguarde a mensagem: `Started TeatroWebApplication`

## 🧪 Teste dos Endpoints Corrigidos

### URLs Corretas:

-   **Swagger UI**: http://localhost:8080/api/swagger-ui.html
-   **Cadastro**: http://localhost:8080/api/usuarios/cadastro
-   **Listagem**: http://localhost:8080/api/usuarios
-   **Health Check**: http://localhost:8080/api/actuator/health

### Teste via Swagger:

1. Acesse: http://localhost:8080/api/swagger-ui.html
2. Clique em `POST /usuarios/cadastro`
3. Clique em "Try it out"
4. Cole o JSON:

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

5. Clique em "Execute"

### Teste via cURL:

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

## 📋 Endpoints Corrigidos

### Usuários:

-   `POST /api/usuarios/cadastro` ✅
-   `GET /api/usuarios` ✅
-   `GET /api/usuarios/{id}` ✅

### Eventos:

-   `GET /api/eventos` ✅
-   `POST /api/eventos` ✅

### Sessões:

-   `GET /api/sessoes` ✅
-   `POST /api/sessoes` ✅

### Áreas:

-   `GET /api/areas` ✅
-   `POST /api/areas` ✅

### Ingressos:

-   `GET /api/ingressos` ✅
-   `POST /api/ingressos` ✅

## 🎉 Resultado Esperado

Após a correção, todos os endpoints devem funcionar corretamente e retornar:

-   **201** para criação bem-sucedida
-   **200** para consultas bem-sucedidas
-   **404** apenas para recursos não encontrados (não para endpoints inexistentes)

---

**💡 Dica**: Sempre que houver problemas com endpoints, verifique se não há duplicação de paths entre context-path e RequestMapping!
