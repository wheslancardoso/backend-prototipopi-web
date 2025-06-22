# 🎭 Guia Manual para Teste de Compra de Ingresso

## 📋 Pré-requisitos

1. **Aplicação rodando**: `mvn spring-boot:run`
2. **Banco de dados**: MySQL com dados corretos
3. **Sessões corrigidas**: Execute o script `corrigir-sessoes-final.sql`

## 🔧 Passo 1: Verificar se a aplicação está rodando

```bash
curl -X GET http://localhost:8080/api/eventos
```

**Resposta esperada**: Lista de eventos em JSON

## 📅 Passo 2: Verificar sessões do Hamlet

```bash
curl -X GET http://localhost:8080/api/sessoes/evento/1
```

**Resposta esperada**:

```json
[
    {
        "id": 7,
        "nome": "Hamlet - Manhã",
        "tipoSessao": "MANHA",
        "dataSessao": "2025-06-22",
        "horario": "08:00:00",
        "ativo": true,
        "eventoId": 1,
        "eventoNome": "Hamlet"
    },
    {
        "id": 8,
        "nome": "Hamlet - Tarde",
        "tipoSessao": "TARDE",
        "dataSessao": "2025-06-22",
        "horario": "14:30:00",
        "ativo": true,
        "eventoId": 1,
        "eventoNome": "Hamlet"
    },
    {
        "id": 9,
        "nome": "Hamlet - Noite",
        "tipoSessao": "NOITE",
        "dataSessao": "2025-06-22",
        "horario": "19:30:00",
        "ativo": true,
        "eventoId": 1,
        "eventoNome": "Hamlet"
    }
]
```

## 🛒 Passo 3: Testar compra de ingresso

**Comando**:

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

**Resposta esperada** (HTTP 201):

```json
{
    "id": 1,
    "usuarioId": 2,
    "sessaoId": 7,
    "areaId": 1,
    "numeroPoltrona": 1,
    "valor": 40.0,
    "dataCompra": "2025-06-21T22:45:08.123Z",
    "codigo": "ING-2025-001",
    "status": "VALIDO"
}
```

## 🎫 Passo 4: Verificar ingressos criados

```bash
curl -X GET http://localhost:8080/api/ingressos
```

**Resposta esperada**: Lista com o ingresso criado

## 🪑 Passo 5: Verificar poltronas disponíveis

```bash
curl -X POST "http://localhost:8080/api/ingressos/verificar-disponibilidade?sessaoId=7&areaId=1" \
  -H "Content-Type: application/json"
```

**Resposta esperada**: Lista de poltronas disponíveis (poltrona 1 não deve aparecer)

## 🔍 Passo 6: Testar compra da mesma poltrona (deve falhar)

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

**Resposta esperada** (HTTP 400): Erro de poltrona ocupada

## 🎯 Testes Adicionais

### Teste com sessão diferente

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

### Teste com área diferente

```bash
curl -X POST http://localhost:8080/api/ingressos \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 2,
    "sessaoId": 7,
    "areaId": 2,
    "numeroPoltrona": 1,
    "valor": 60.00
  }'
```

## 🚨 Possíveis Erros e Soluções

### Erro 404 - Endpoint não encontrado

-   Verifique se a aplicação está rodando
-   Verifique se o context-path está correto (`/api`)

### Erro 400 - Bad Request

-   Verifique se os IDs existem no banco
-   Verifique se a sessão está ativa
-   Verifique se a poltrona está disponível

### Erro 500 - Internal Server Error

-   Verifique os logs da aplicação
-   Verifique se o banco está acessível

## 📊 Verificação no Banco

```sql
-- Verificar sessões
SELECT id, nome, ativo, horario FROM sessoes WHERE evento_id = 1;

-- Verificar ingressos
SELECT * FROM ingressos;

-- Verificar poltronas ocupadas
SELECT i.numero_poltrona, s.nome as sessao, a.nome as area
FROM ingressos i
JOIN sessoes s ON i.sessao_id = s.id
JOIN areas a ON i.area_id = a.id
WHERE i.sessao_id = 7 AND i.area_id = 1;
```

## 🎉 Sucesso!

Se todos os testes passaram, significa que:

-   ✅ Sessões estão corretas e ativas
-   ✅ Compra de ingressos funciona
-   ✅ Validação de poltronas funciona
-   ✅ Sistema está pronto para o frontend!

## 🔗 Links Úteis

-   **Swagger UI**: http://localhost:8080/api/swagger-ui.html
-   **Health Check**: http://localhost:8080/api/actuator/health
