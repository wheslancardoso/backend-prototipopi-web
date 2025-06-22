# 🎭 Teste Manual - Compra de Ingresso Hamlet

## 🎯 Objetivo

Comprar ingresso para **Hamlet** no dia **22/06/2025**, horário da **manhã**, na **Plateia A**.

## 📋 Passo a Passo

### **1. Primeiro, execute o script SQL no banco:**

```bash
mysql -u root -p teatro_db < verificar-dados-banco.sql
```

### **2. Acesse o Swagger UI:**

http://localhost:8080/api/swagger-ui.html

### **3. Teste os Endpoints na Ordem:**

#### **3.1 - Listar Eventos**

-   Clique em `GET /eventos`
-   Clique em "Try it out"
-   Clique em "Execute"
-   **Esperado**: Lista com Hamlet, O Fantasma da Opera, O Auto da Compadecida

#### **3.2 - Listar Sessões do Hamlet**

-   Clique em `GET /sessoes/evento/{eventoId}`
-   Clique em "Try it out"
-   Digite `1` no campo `eventoId`
-   Clique em "Execute"
-   **Esperado**: 3 sessões (Manhã, Tarde, Noite) para 22/06/2025

#### **3.3 - Listar Áreas**

-   Clique em `GET /areas`
-   Clique em "Try it out"
-   Clique em "Execute"
-   **Esperado**: Plateia A (R$ 40,00), Plateia B (R$ 60,00), etc.

#### **3.4 - Comprar Ingresso**

-   Clique em `POST /ingressos`
-   Clique em "Try it out"
-   Cole este JSON:

```json
{
    "usuarioId": 2,
    "sessaoId": 1,
    "areaId": 1,
    "numeroPoltrona": 1,
    "valor": 40.0
}
```

-   Clique em "Execute"
-   **Esperado**: HTTP 201 com dados do ingresso criado

#### **3.5 - Verificar Ingressos**

-   Clique em `GET /ingressos`
-   Clique em "Try it out"
-   Clique em "Execute"
-   **Esperado**: Lista com o ingresso comprado

## 🎫 Dados da Compra

### **Informações:**

-   **Evento**: Hamlet (ID: 1)
-   **Sessão**: Manhã (ID: 1) - 22/06/2025 às 08:00
-   **Área**: Plateia A (ID: 1) - R$ 40,00
-   **Poltrona**: 1
-   **Usuário**: ID 2 (user)

### **JSON para Compra:**

```json
{
    "usuarioId": 2,
    "sessaoId": 1,
    "areaId": 1,
    "numeroPoltrona": 1,
    "valor": 40.0
}
```

## 🔍 Respostas Esperadas

### **✅ Compra Bem-sucedida (HTTP 201):**

```json
{
    "id": 1,
    "usuarioId": 2,
    "sessaoId": 1,
    "areaId": 1,
    "numeroPoltrona": 1,
    "valor": 40.0,
    "dataCompra": "2025-06-21T22:45:00",
    "codigo": "ING-2025-001"
}
```

### **❌ Possíveis Erros:**

-   **HTTP 400**: Dados inválidos
-   **HTTP 404**: Sessão/área não encontrada
-   **HTTP 409**: Poltrona já ocupada

## 🚀 Próximos Testes

Após comprar com sucesso:

1. **Teste comprar a mesma poltrona** (deve dar erro)
2. **Teste comprar poltrona 2** (deve funcionar)
3. **Teste listar ingressos do usuário**

## 💡 Dicas

-   Se algum endpoint der 404, verifique se a aplicação está rodando
-   Se der erro de dados, execute o script SQL primeiro
-   Use o Swagger UI para facilitar os testes
-   Verifique os logs da aplicação para detalhes de erro

---

**🎭 Boa sorte com o teste! Se funcionar, você terá um sistema de compra de ingressos funcionando!**
