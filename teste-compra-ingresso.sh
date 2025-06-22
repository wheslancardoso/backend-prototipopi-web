#!/bin/bash

echo "🎭 Testando Fluxo de Compra de Ingresso - Hamlet"
echo "================================================"

# 1. Verificar eventos disponíveis
echo "📋 1. Listando eventos disponíveis..."
curl -X GET http://localhost:8080/api/eventos \
  -H "Content-Type: application/json" \
  -w "\n\nStatus: %{http_code}\n"

echo ""
echo "================================================"

# 2. Verificar sessões disponíveis
echo "📅 2. Listando sessões disponíveis..."
curl -X GET http://localhost:8080/api/sessoes \
  -H "Content-Type: application/json" \
  -w "\n\nStatus: %{http_code}\n"

echo ""
echo "================================================"

# 3. Verificar sessões do evento Hamlet
echo "🎭 3. Listando sessões do evento Hamlet..."
curl -X GET http://localhost:8080/api/sessoes/evento/1 \
  -H "Content-Type: application/json" \
  -w "\n\nStatus: %{http_code}\n"

echo ""
echo "================================================"

# 4. Verificar áreas disponíveis
echo "🏛️ 4. Listando áreas disponíveis..."
curl -X GET http://localhost:8080/api/areas \
  -H "Content-Type: application/json" \
  -w "\n\nStatus: %{http_code}\n"

echo ""
echo "================================================"

# 5. Verificar ingressos existentes
echo "🎫 5. Listando ingressos existentes..."
curl -X GET http://localhost:8080/api/ingressos \
  -H "Content-Type: application/json" \
  -w "\n\nStatus: %{http_code}\n"

echo ""
echo "================================================"

echo "✅ Verificação inicial concluída!"
echo ""
echo "💡 Agora vamos testar a compra de ingresso..."
echo ""

# 6. Comprar ingresso (assumindo que temos os dados)
echo "🛒 6. Comprando ingresso para Hamlet..."
echo "   - Evento: Hamlet (ID: 1)"
echo "   - Data: 2025-06-22"
echo "   - Horário: Manhã"
echo "   - Área: Plateia A"
echo "   - Poltrona: 1"
echo "   - Usuário: ID 2"

curl -X POST http://localhost:8080/api/ingressos \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 2,
    "sessaoId": 1,
    "areaId": 1,
    "numeroPoltrona": 1,
    "valor": 40.00
  }' \
  -w "\n\nStatus: %{http_code}\nTempo: %{time_total}s\n"

echo ""
echo "================================================"

# 7. Verificar ingressos após a compra
echo "🎫 7. Verificando ingressos após a compra..."
curl -X GET http://localhost:8080/api/ingressos \
  -H "Content-Type: application/json" \
  -w "\n\nStatus: %{http_code}\n"

echo ""
echo "✅ Teste de compra concluído!"
echo ""
echo "🔗 Swagger UI: http://localhost:8080/api/swagger-ui.html"
