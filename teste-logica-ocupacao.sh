#!/bin/bash

echo "🎭 Teste da Lógica de Ocupação de Poltronas"
echo "============================================="

# Função para fazer compra
fazer_compra() {
  local sessao_id=$1
  local area_id=$2
  local poltrona=$3
  local descricao=$4

  echo ""
  echo "🛒 Testando: $descricao"
  echo "   Sessão: $sessao_id, Área: $area_id, Poltrona: $poltrona"

  response=$(curl -s -X POST http://localhost:8080/api/ingressos \
    -H "Content-Type: application/json" \
    -d "{
            \"usuarioId\": 2,
            \"sessaoId\": $sessao_id,
            \"areaId\": $area_id,
            \"numeroPoltrona\": $poltrona,
            \"valor\": 40.00
        }")

  http_code=$(curl -s -o /dev/null -w "%{http_code}" -X POST http://localhost:8080/api/ingressos \
    -H "Content-Type: application/json" \
    -d "{
            \"usuarioId\": 2,
            \"sessaoId\": $sessao_id,
            \"areaId\": $area_id,
            \"numeroPoltrona\": $poltrona,
            \"valor\": 40.00
        }")

  if [ "$http_code" = "200" ]; then
    echo "   ✅ SUCESSO: Compra realizada!"
    echo "   📄 Response: $response"
  else
    echo "   ❌ ERRO: HTTP $http_code"
    echo "   📄 Response: $response"
  fi
}

echo ""
echo "📋 Cenários de Teste:"
echo "====================="

# Teste 1: Mesma poltrona, sessões diferentes (deve funcionar)
echo ""
echo "🔍 TESTE 1: Mesma poltrona, sessões diferentes"
echo "   Deve permitir compra em cada sessão"

fazer_compra 7 1 1 "Poltrona 1 - Hamlet Manhã (08:00)"
fazer_compra 8 1 1 "Poltrona 1 - Hamlet Tarde (14:30)"
fazer_compra 9 1 1 "Poltrona 1 - Hamlet Noite (19:30)"

# Teste 2: Mesma poltrona, áreas diferentes (deve funcionar)
echo ""
echo "🔍 TESTE 2: Mesma poltrona, áreas diferentes"
echo "   Deve permitir compra em cada área"

fazer_compra 7 1 2 "Poltrona 2 - Hamlet Manhã - Plateia A"
fazer_compra 7 2 2 "Poltrona 2 - Hamlet Manhã - Plateia B"

# Teste 3: Tentativa de comprar a mesma poltrona na mesma sessão/área (deve falhar)
echo ""
echo "🔍 TESTE 3: Mesma poltrona, mesma sessão/área"
echo "   Deve falhar na segunda tentativa"

fazer_compra 7 1 3 "Poltrona 3 - Hamlet Manhã - Plateia A (1ª tentativa)"
fazer_compra 7 1 3 "Poltrona 3 - Hamlet Manhã - Plateia A (2ª tentativa - DEVE FALHAR)"

echo ""
echo "✅ Teste concluído!"
echo "📊 Verifique os resultados acima para confirmar se a lógica está correta."
