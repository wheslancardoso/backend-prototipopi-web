#!/bin/bash

echo "🔄 Reiniciando aplicação Spring Boot..."

# Parar aplicação se estiver rodando
echo "⏹️  Parando aplicação anterior..."
pkill -f "spring-boot:run" 2>/dev/null || echo "Nenhuma aplicação anterior encontrada"

# Aguardar um pouco
sleep 3

# Compilar e executar
echo "🚀 Iniciando aplicação..."
mvn clean compile
mvn spring-boot:run
