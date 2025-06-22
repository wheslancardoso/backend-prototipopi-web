#!/bin/bash

# Script de Testes da API do Sistema de Teatro
# Execute: chmod +x testes-api.sh && ./testes-api.sh

echo "🎭 Iniciando Testes da API do Sistema de Teatro"
echo "================================================"

# Aguardar aplicação inicializar
echo "⏳ Aguardando aplicação inicializar..."
sleep 15

# Teste 1: Conectividade Básica
echo ""
echo "1️⃣ Teste de Conectividade"
echo "-------------------------"
echo "Testando se a API está respondendo..."

if curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/usuarios | grep -q "200\|401\|403"; then
  echo "✅ API está respondendo!"
else
  echo "❌ API não está respondendo"
  exit 1
fi

# Teste 2: Swagger UI
echo ""
echo "2️⃣ Teste do Swagger UI"
echo "----------------------"
if curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/swagger-ui/index.html | grep -q "200"; then
  echo "✅ Swagger UI está funcionando!"
  echo "📚 Acesse: http://localhost:8080/api/swagger-ui/index.html"
else
  echo "❌ Swagger UI não está funcionando"
fi

# Teste 3: Cadastro de Usuário
echo ""
echo "3️⃣ Teste de Cadastro de Usuário"
echo "--------------------------------"
echo "Criando usuário de teste..."

RESPONSE=$(curl -s -X POST http://localhost:8080/api/usuarios/cadastro \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Usuário Teste",
    "cpf": "111.222.333-44",
    "email": "teste@email.com",
    "senha": "123456",
    "endereco": "Rua Teste, 123",
    "telefone": "(11) 88888-8888"
  }')

if echo "$RESPONSE" | grep -q "id\|success"; then
  echo "✅ Usuário criado com sucesso!"
  echo "📄 Resposta: $RESPONSE"
else
  echo "❌ Erro ao criar usuário"
  echo "📄 Resposta: $RESPONSE"
fi

# Teste 4: Login
echo ""
echo "4️⃣ Teste de Login"
echo "-----------------"
echo "Testando login..."

LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/usuarios/login \
  -H "Content-Type: application/json" \
  -d '{
    "identificador": "teste@email.com",
    "senha": "123456"
  }')

if echo "$LOGIN_RESPONSE" | grep -q "token\|success"; then
  echo "✅ Login realizado com sucesso!"
  echo "📄 Resposta: $LOGIN_RESPONSE"
else
  echo "❌ Erro no login"
  echo "📄 Resposta: $LOGIN_RESPONSE"
fi

# Teste 5: Listar Eventos
echo ""
echo "5️⃣ Teste de Listagem de Eventos"
echo "-------------------------------"
echo "Listando eventos..."

EVENTOS_RESPONSE=$(curl -s -X GET http://localhost:8080/api/eventos)

if echo "$EVENTOS_RESPONSE" | grep -q "id\|nome"; then
  echo "✅ Eventos listados com sucesso!"
  echo "📄 Resposta: $EVENTOS_RESPONSE"
else
  echo "❌ Erro ao listar eventos"
  echo "📄 Resposta: $EVENTOS_RESPONSE"
fi

# Teste 6: Listar Sessões
echo ""
echo "6️⃣ Teste de Listagem de Sessões"
echo "-------------------------------"
echo "Listando sessões..."

SESSOES_RESPONSE=$(curl -s -X GET http://localhost:8080/api/sessoes)

if echo "$SESSOES_RESPONSE" | grep -q "id\|nome"; then
  echo "✅ Sessões listadas com sucesso!"
  echo "📄 Resposta: $SESSOES_RESPONSE"
else
  echo "❌ Erro ao listar sessões"
  echo "📄 Resposta: $SESSOES_RESPONSE"
fi

echo ""
echo "🎉 Testes Concluídos!"
echo "====================="
echo "📚 Swagger UI: http://localhost:8080/api/swagger-ui/index.html"
echo "🔗 API Base: http://localhost:8080/api"
echo ""
echo "💡 Dicas:"
echo "- Use o Swagger UI para testes interativos"
echo "- Verifique os logs da aplicação para mais detalhes"
echo "- Teste diferentes cenários (erros, validações, etc.)"
