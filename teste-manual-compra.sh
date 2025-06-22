#!/bin/bash

echo "🎭 Teste Manual de Compra de Ingresso"
echo "====================================="

# 1. Verificar se a aplicação está rodando
echo "🔍 1. Verificando se a aplicação está rodando..."
curl -s -o /dev/null -w "Status: %{http_code}\n" http://localhost:8080/api/eventos

# 2. Buscar sessões do Hamlet
echo ""
echo "📅 2. Buscando sessões do Hamlet..."
curl -s -X GET http://localhost:8080/api/sessoes/evento/1 | jq '.[0]'

# 3. Testar compra com sessão ID 7
echo ""
echo "🛒 3. Testando compra com sessão ID 7..."
curl -X POST http://localhost:8080/api/ingressos \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 2,
    "sessaoId": 7,
    "areaId": 1,
    "numeroPoltrona": 1,
    "valor": 40.00
  }'

echo ""
echo "✅ Teste concluído!"
