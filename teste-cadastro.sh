#!/bin/bash

echo "🧪 Testando endpoint de cadastro de usuários..."
echo "================================================"

# Teste 1: Cadastro de usuário comum
echo "📝 Teste 1: Cadastrando usuário comum..."
curl -X POST http://localhost:8080/api/usuarios/cadastro \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "cpf": "123.456.789-00",
    "endereco": "Rua das Flores, 123",
    "telefone": "(11) 99999-9999",
    "email": "joao@email.com",
    "senha": "123456",
    "tipoUsuario": "COMUM"
  }' \
  -w "\n\nStatus: %{http_code}\nTempo: %{time_total}s\n"

echo ""
echo "================================================"

# Teste 2: Cadastro de usuário admin
echo "📝 Teste 2: Cadastrando usuário admin..."
curl -X POST http://localhost:8080/api/usuarios/cadastro \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Admin Sistema",
    "cpf": "987.654.321-00",
    "endereco": "Av. Principal, 456",
    "telefone": "(11) 88888-8888",
    "email": "admin@teatro.com",
    "senha": "admin123",
    "tipoUsuario": "ADMIN"
  }' \
  -w "\n\nStatus: %{http_code}\nTempo: %{time_total}s\n"

echo ""
echo "================================================"

# Teste 3: Listar usuários (para verificar se foram criados)
echo "📋 Teste 3: Listando usuários cadastrados..."
curl -X GET http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -w "\n\nStatus: %{http_code}\nTempo: %{time_total}s\n"

echo ""
echo "✅ Testes concluídos!"
echo ""
echo "🔗 Swagger UI: http://localhost:8080/api/swagger-ui.html"
echo "📚 Documentação da API disponível no Swagger"
