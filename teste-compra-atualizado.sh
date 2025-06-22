#!/bin/bash

echo "🎭 Testando Compra de Ingresso - Versão Atualizada"
echo "=================================================="

# 1. Primeiro, corrigir as sessões no banco
echo "🔧 1. Corrigindo sessões no banco..."
mysql -u root -p teatro_db <corrigir-sessoes-final.sql

echo ""
echo "=================================================="

# 2. Verificar sessões corrigidas e obter o ID correto
echo "📅 2. Verificando sessões corrigidas..."
RESPONSE=$(curl -s -X GET http://localhost:8080/api/sessoes/evento/1 \
  -H "Content-Type: application/json")

echo "$RESPONSE"
echo "Status: $(curl -s -o /dev/null -w "%{http_code}" -X GET http://localhost:8080/api/sessoes/evento/1)"

# Extrair o ID da primeira sessão (Hamlet - Manhã)
SESSAO_ID=$(echo "$RESPONSE" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
echo "🎯 ID da sessão encontrado: $SESSAO_ID"

echo ""
echo "=================================================="

# 3. Testar compra de ingresso com o ID correto
echo "🛒 3. Testando compra de ingresso..."
echo "   - Usuário: ID 2"
echo "   - Sessão: ID $SESSAO_ID (Hamlet - Manhã)"
echo "   - Área: ID 1 (Plateia A)"
echo "   - Poltrona: 1"
echo "   - Valor: R$ 40,00"

curl -X POST http://localhost:8080/api/ingressos \
  -H "Content-Type: application/json" \
  -d "{
    \"usuarioId\": 2,
    \"sessaoId\": $SESSAO_ID,
    \"areaId\": 1,
    \"numeroPoltrona\": 1,
    \"valor\": 40.00
  }" \
  -w "\n\nStatus: %{http_code}\nTempo: %{time_total}s\n"

echo ""
echo "=================================================="

# 4. Verificar ingressos após compra
echo "🎫 4. Verificando ingressos após compra..."
curl -X GET http://localhost:8080/api/ingressos \
  -H "Content-Type: application/json" \
  -w "\n\nStatus: %{http_code}\n"

echo ""
echo "=================================================="

# 5. Verificar poltronas disponíveis
echo "🪑 5. Verificando poltronas disponíveis..."
curl -X POST "http://localhost:8080/api/ingressos/verificar-disponibilidade?sessaoId=$SESSAO_ID&areaId=1" \
  -H "Content-Type: application/json" \
  -w "\n\nStatus: %{http_code}\n"

echo ""
echo "✅ Teste concluído!"
echo "🔗 Swagger UI: http://localhost:8080/api/swagger-ui.html"
