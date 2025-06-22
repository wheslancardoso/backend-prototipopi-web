# 🎭 Guia de Compra de Ingresso - Sistema de Teatro

## 🎯 Objetivo

Testar o fluxo completo de compra de ingresso para o evento **Hamlet** no dia **22/06/2025**, horário da **manhã**, na **Plateia A**.

## 📋 Pré-requisitos

### 1. **Dados no Banco**

Execute o script SQL para garantir que temos os dados necessários:

```bash
mysql -u root -p teatro_db < verificar-dados-banco.sql
```

### 2. **Usuário Logado**

-   ✅ Usuário cadastrado (ID: 2)
-   ✅ Login funcionando

## 🧪 Fluxo de Teste

### **Passo 1: Verificar Eventos**

```bash
curl -X GET http://localhost:8080/api/eventos
```

**Resposta Esperada:**

```json
[
    {
        "id": 1,
        "nome": "Hamlet"
    },
    {
        "id": 2,
        "nome": "O Fantasma da Opera"
    },
    {
        "id": 3,
        "nome": "O Auto da Compadecida"
    }
]
```

### **Passo 2: Verificar Sessões do Hamlet**

```bash
curl -X GET http://localhost:8080/api/sessoes/evento/1
```

**Resposta Esperada:**

```json
[
    {
        "id": 1,
        "nome": "Hamlet - Manhã",
        "tipoSessao": "MANHA",
        "dataSessao": "2025-06-22T08:00:00",
        "eventoId": 1
    },
    {
        "id": 2,
        "nome": "Hamlet - Tarde",
        "tipoSessao": "TARDE",
        "dataSessao": "2025-06-22T14:30:00",
        "eventoId": 1
    },
    {
        "id": 3,
        "nome": "Hamlet - Noite",
        "tipoSessao": "NOITE",
        "dataSessao": "2025-06-22T19:30:00",
        "eventoId": 1
    }
]
```

### **Passo 3: Verificar Áreas Disponíveis**

```bash
curl -X GET http://localhost:8080/api/areas
```

**Resposta Esperada:**

```json
[
    {
        "id": 1,
        "nome": "Plateia A",
        "preco": 40.0,
        "capacidadeTotal": 25
    },
    {
        "id": 2,
        "nome": "Plateia B",
        "preco": 60.0,
        "capacidadeTotal": 100
    }
]
```

### **Passo 4: Comprar Ingresso**

#### **Dados da Compra:**

-   **Evento**: Hamlet (ID: 1)
-   **Sessão**: Manhã (ID: 1)
-   **Data**: 22/06/2025 às 08:00
-   **Área**: Plateia A (ID: 1)
-   **Poltrona**: 1
-   **Valor**: R$ 40,00
-   **Usuário**: ID 2

#### **Comando de Compra:**

```bash
curl -X POST http://localhost:8080/api/ingressos \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 2,
    "sessaoId": 1,
    "areaId": 1,
    "numeroPoltrona": 1,
    "valor": 40.00
  }'
```

#### **Resposta Esperada (HTTP 201):**

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

### **Passo 5: Verificar Ingresso Comprado**

```bash
curl -X GET http://localhost:8080/api/ingressos
```

### **Passo 6: Verificar Ingressos do Usuário**

```bash
curl -X GET http://localhost:8080/api/ingressos/usuario/2
```

## 🎯 Teste via Swagger UI

### **1. Acesse o Swagger:**

http://localhost:8080/api/swagger-ui.html

### **2. Teste os Endpoints:**

1. **GET /eventos** - Listar eventos
2. **GET /sessoes/evento/{eventoId}** - Sessões do Hamlet
3. **GET /areas** - Áreas disponíveis
4. **POST /ingressos** - Comprar ingresso
5. **GET /ingressos** - Listar ingressos

### **3. Dados para Compra no Swagger:**

```json
{
    "usuarioId": 2,
    "sessaoId": 1,
    "areaId": 1,
    "numeroPoltrona": 1,
    "valor": 40.0
}
```

## 🔍 Validações Esperadas

### **✅ Compra Bem-sucedida:**

-   HTTP 201 (Created)
-   Ingresso salvo no banco
-   Código único gerado
-   Data de compra registrada

### **❌ Possíveis Erros:**

-   **HTTP 400**: Dados inválidos
-   **HTTP 409**: Poltrona já ocupada
-   **HTTP 404**: Sessão/área não encontrada

## 🎫 Estrutura do Ingresso

### **Campos Obrigatórios:**

-   `usuarioId`: ID do usuário comprador
-   `sessaoId`: ID da sessão
-   `areaId`: ID da área
-   `numeroPoltrona`: Número da poltrona
-   `valor`: Valor do ingresso

### **Campos Gerados Automaticamente:**

-   `id`: ID único do ingresso
-   `dataCompra`: Data/hora da compra
-   `codigo`: Código único do ingresso

## 🚀 Próximos Passos

Após testar com sucesso:

1. ✅ **Implementar JWT** para autenticação real
2. ✅ **Criar frontend React** para interface visual
3. ✅ **Implementar pagamentos** (PIX, cartão)
4. ✅ **Adicionar validações** de poltronas disponíveis

---

**💡 Dica**: Use o script `teste-compra-ingresso.sh` para automatizar todo o processo de teste!
