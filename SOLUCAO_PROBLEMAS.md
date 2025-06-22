# 🔧 Solução de Problemas - Endpoint de Cadastro

## ❌ Problema: Erro 404 no endpoint `/api/usuarios/cadastro`

### 🔍 **Possíveis Causas e Soluções:**

#### **1. Aplicação não está rodando**

```bash
# Verificar se a aplicação está rodando
ps aux | grep java | grep teatro

# Se não estiver rodando, inicie:
mvn spring-boot:run
```

#### **2. Banco de dados não configurado**

```bash
# Executar o script SQL
mysql -u root -p < database_moderno.sql

# Verificar se o banco foi criado
mysql -u root -p -e "USE teatro_db; SHOW TABLES;"
```

#### **3. Erro de compilação**

```bash
# Limpar e recompilar
mvn clean compile

# Verificar se há erros
mvn test
```

#### **4. Problema de configuração do Spring Security**

O Spring Security pode estar bloqueando o endpoint. Verifique o `SecurityConfig.java`.

#### **5. Porta ocupada**

```bash
# Verificar se a porta 8080 está livre
lsof -i :8080

# Se estiver ocupada, mate o processo
kill -9 <PID>
```

---

## 🧪 **Como Testar:**

### **Opção 1: Script Automatizado**

```bash
chmod +x teste-cadastro.sh
./teste-cadastro.sh
```

### **Opção 2: Teste Manual**

```bash
# 1. Verificar se a API está respondendo
curl -X GET http://localhost:8080/api/actuator/health

# 2. Testar o endpoint de cadastro
curl -X POST \
  'http://localhost:8080/api/usuarios/cadastro' \
  -H 'Content-Type: application/json' \
  -d '{
  "nome": "lan",
  "cpf": "266.424.790-50",
  "endereco": "goiania",
  "telefone": "62991911084",
  "email": "usertest@gmail.com",
  "tipoUsuario": "COMUM",
  "senha": "usertest"
}'
```

### **Opção 3: Swagger UI**

1. Acesse: http://localhost:8080/api/swagger-ui/index.html
2. Procure pelo endpoint `POST /api/usuarios/cadastro`
3. Clique em "Try it out"
4. Preencha os dados e execute

---

## 📋 **Checklist de Verificação:**

-   [ ] ✅ Aplicação Spring Boot está rodando
-   [ ] ✅ Banco de dados MySQL está ativo
-   [ ] ✅ Script SQL foi executado com sucesso
-   [ ] ✅ Projeto compila sem erros
-   [ ] ✅ Endpoint está mapeado corretamente
-   [ ] ✅ Spring Security não está bloqueando
-   [ ] ✅ Porta 8080 está livre

---

## 🚀 **Comandos de Inicialização:**

```bash
# 1. Iniciar MySQL (se necessário)
sudo systemctl start mysql

# 2. Executar script SQL
mysql -u root -p < database_moderno.sql

# 3. Iniciar aplicação
mvn spring-boot:run

# 4. Aguardar inicialização (30 segundos)
sleep 30

# 5. Testar endpoint
./teste-cadastro.sh
```

---

## 📞 **Logs Úteis:**

### **Verificar logs da aplicação:**

```bash
# Se estiver rodando em background
tail -f logs/spring.log

# Ou verificar logs do Maven
mvn spring-boot:run 2>&1 | tee app.log
```

### **Verificar logs do MySQL:**

```bash
sudo tail -f /var/log/mysql/error.log
```

---

## 🎯 **Resposta Esperada:**

**Sucesso (HTTP 201):**

```json
{
    "id": 2,
    "nome": "lan",
    "cpf": "266.424.790-50",
    "email": "usertest@gmail.com",
    "tipoUsuario": "COMUM",
    "endereco": "goiania",
    "telefone": "62991911084"
}
```

**Usuário já existe (HTTP 409):**

```json
{
    "timestamp": "2025-06-21T22:13:32",
    "status": 409,
    "error": "Conflict",
    "message": "CPF ou email já cadastrado"
}
```

---

## 🔧 **Se o problema persistir:**

1. **Verifique os logs completos** da aplicação
2. **Confirme a configuração** do banco de dados
3. **Teste outros endpoints** para isolar o problema
4. **Verifique a versão** do Java e Maven
5. **Consulte a documentação** do Spring Boot

---

## 📚 **Recursos Adicionais:**

-   **Swagger UI**: http://localhost:8080/api/swagger-ui/index.html
-   **Health Check**: http://localhost:8080/api/actuator/health
-   **Documentação**: README.md
-   **Scripts de teste**: teste-cadastro.sh, testes-api.sh
