#!/bin/bash

echo "🧪 Testando login do usuário cadastrado..."
echo "=========================================="

# Teste de login com CPF
echo "📝 Teste 1: Login com CPF..."
curl -X POST http://localhost:8080/api/usuarios/login \
  -H "Content-Type: application/json" \
  -d '{
    "identificador": "266.424.790-50",
    "senha": "root123"
  }' \
  -w "\n\nStatus: %{http_code}\nTempo: %{time_total}s\n"

echo ""
echo "=========================================="

# Teste de login com email
echo "📝 Teste 2: Login com email..."
curl -X POST http://localhost:8080/api/usuarios/login \
  -H "Content-Type: application/json" \
  -d '{
    "identificador": "usertest@gmail.com",
    "senha": "root123"
  }' \
  -w "\n\nStatus: %{http_code}\nTempo: %{time_total}s\n"

echo ""
echo "=========================================="

# Teste de login com senha incorreta
echo "📝 Teste 3: Login com senha incorreta..."
curl -X POST http://localhost:8080/api/usuarios/login \
  -H "Content-Type: application/json" \
  -d '{
    "identificador": "266.424.790-50",
    "senha": "senhaerrada"
  }' \
  -w "\n\nStatus: %{http_code}\nTempo: %{time_total}s\n"

echo ""
echo "✅ Testes de login concluídos!"
echo ""
echo "💡 Se o login funcionar, significa que a senha foi salva corretamente!"
