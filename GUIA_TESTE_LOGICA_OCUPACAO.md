# 🎭 Guia para Testar a Lógica de Ocupação de Poltronas

## 🎯 Objetivo

Testar se a lógica de ocupação está funcionando corretamente, permitindo que a mesma poltrona seja vendida em diferentes sessões/áreas, mas impedindo vendas duplicadas na mesma sessão/área.

## 📋 Cenários de Teste

### ✅ **CENÁRIO 1: Mesma poltrona, sessões diferentes**

**Deve funcionar** - A poltrona 1 pode ser vendida em todas as sessões do Hamlet

#### Teste 1.1: Hamlet - Manhã (Sessão ID 7)

```bash
curl -X POST http://localhost:8080/api/ingressos \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 2,
    "sessaoId": 7,
    "areaId": 1,
    "numeroPoltrona": 1,
    "valor": 40.00
  }'
```

**Resultado esperado**: ✅ SUCESSO (HTTP 200)

#### Teste 1.2: Hamlet - Tarde (Sessão ID 8)

```bash
curl -X POST http://localhost:8080/api/ingressos \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 2,
    "sessaoId": 8,
    "areaId": 1,
    "numeroPoltrona": 1,
    "valor": 40.00
  }'
```

**Resultado esperado**: ✅ SUCESSO (HTTP 200)

#### Teste 1.3: Hamlet - Noite (Sessão ID 9)

```bash
curl -X POST http://localhost:8080/api/ingressos \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 2,
    "sessaoId": 9,
    "areaId": 1,
    "numeroPoltrona": 1,
    "valor": 40.00
  }'
```

**Resultado esperado**: ✅ SUCESSO (HTTP 200)

---

### ✅ **CENÁRIO 2: Mesma poltrona, áreas diferentes**

**Deve funcionar** - A poltrona 2 pode ser vendida em áreas diferentes da mesma sessão

#### Teste 2.1: Hamlet Manhã - Plateia A (Área ID 1)

```bash
curl -X POST http://localhost:8080/api/ingressos \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 2,
    "sessaoId": 7,
    "areaId": 1,
    "numeroPoltrona": 2,
    "valor": 40.00
  }'
```

**Resultado esperado**: ✅ SUCESSO (HTTP 200)

#### Teste 2.2: Hamlet Manhã - Plateia B (Área ID 2)

```bash
curl -X POST http://localhost:8080/api/ingressos \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 2,
    "sessaoId": 7,
    "areaId": 2,
    "numeroPoltrona": 2,
    "valor": 60.00
  }'
```

**Resultado esperado**: ✅ SUCESSO (HTTP 200)

---

### ❌ **CENÁRIO 3: Mesma poltrona, mesma sessão/área**

**Deve falhar** - A segunda tentativa deve ser rejeitada

#### Teste 3.1: Primeira compra (deve funcionar)

```bash
curl -X POST http://localhost:8080/api/ingressos \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 2,
    "sessaoId": 7,
    "areaId": 1,
    "numeroPoltrona": 3,
    "valor": 40.00
  }'
```

**Resultado esperado**: ✅ SUCESSO (HTTP 200)

#### Teste 3.2: Segunda compra (deve falhar)

```bash
curl -X POST http://localhost:8080/api/ingressos \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 2,
    "sessaoId": 7,
    "areaId": 1,
    "numeroPoltrona": 3,
    "valor": 40.00
  }'
```

**Resultado esperado**: ❌ ERRO (HTTP 400) - "Poltrona já está ocupada"

---

## 🔍 Como Verificar no Swagger UI

1. **Acesse**: http://localhost:8080/api/swagger-ui.html
2. **Encontre**: `POST /ingressos`
3. **Clique**: "Try it out"
4. **Cole o JSON** de cada teste
5. **Clique**: "Execute"
6. **Verifique**: O código de resposta (200 = sucesso, 400 = erro)

## 📊 Resultados Esperados

| Cenário | Poltrona | Sessão    | Área  | Resultado  |
| ------- | -------- | --------- | ----- | ---------- |
| 1.1     | 1        | 7 (Manhã) | 1 (A) | ✅ Sucesso |
| 1.2     | 1        | 8 (Tarde) | 1 (A) | ✅ Sucesso |
| 1.3     | 1        | 9 (Noite) | 1 (A) | ✅ Sucesso |
| 2.1     | 2        | 7 (Manhã) | 1 (A) | ✅ Sucesso |
| 2.2     | 2        | 7 (Manhã) | 2 (B) | ✅ Sucesso |
| 3.1     | 3        | 7 (Manhã) | 1 (A) | ✅ Sucesso |
| 3.2     | 3        | 7 (Manhã) | 1 (A) | ❌ Erro    |

## 🎯 Conclusão

Se todos os testes seguirem o padrão esperado, significa que a **lógica de ocupação está funcionando corretamente**:

-   ✅ **Poltronas podem ser vendidas em sessões diferentes**
-   ✅ **Poltronas podem ser vendidas em áreas diferentes**
-   ❌ **Poltronas não podem ser vendidas duas vezes na mesma sessão/área**

Esta é a lógica correta para um sistema de teatro! 🎭
