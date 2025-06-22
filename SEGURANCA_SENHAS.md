# 🔒 Segurança de Senhas - Sistema de Teatro

## ✅ **Comportamento Correto: Senha `null` no Response**

### **Por que a senha aparece como `null`?**

```java
// No UsuarioController.java - linha 244
private UsuarioDTO converterParaDTO(Usuario usuario) {
    return new UsuarioDTO(
        usuario.getId(),
        usuario.getNome(),
        usuario.getCpf(),
        usuario.getEndereco(),
        usuario.getTelefone(),
        usuario.getEmail(),
        usuario.getTipoUsuario().name(),
        null // 🔒 NÃO incluir senha no DTO por segurança
    );
}
```

### **Princípios de Segurança:**

1. **🔐 Nunca retornar senhas em APIs**
2. **🛡️ Senhas devem ser hasheadas no banco**
3. **🚫 Não expor dados sensíveis em responses**
4. **✅ Usar DTOs para controlar o que é retornado**

## 🧪 **Como Verificar se a Senha foi Salva Corretamente**

### **1. Teste de Login (Recomendado)**

```bash
# Teste com CPF
curl -X POST http://localhost:8080/api/usuarios/login \
  -H "Content-Type: application/json" \
  -d '{
    "identificador": "266.424.790-50",
    "senha": "root123"
  }'

# Teste com email
curl -X POST http://localhost:8080/api/usuarios/login \
  -H "Content-Type: application/json" \
  -d '{
    "identificador": "usertest@gmail.com",
    "senha": "root123"
  }'
```

### **2. Verificar no Banco de Dados**

```sql
-- Verificar se a senha foi salva (deve estar hasheada)
SELECT id, nome, email, senha FROM usuarios WHERE id = 2;

-- A senha deve aparecer como um hash, não como "root123"
-- Exemplo: $2a$10$abc123... (hash BCrypt)
```

### **3. Teste de Senha Incorreta**

```bash
curl -X POST http://localhost:8080/api/usuarios/login \
  -H "Content-Type: application/json" \
  -d '{
    "identificador": "266.424.790-50",
    "senha": "senhaerrada"
  }'
# Deve retornar 401 (Unauthorized)
```

## 📊 **Respostas Esperadas**

### **✅ Login Bem-sucedido (HTTP 200):**

```json
{
    "token": "jwt-token-placeholder",
    "usuario": {
        "id": 2,
        "nome": "user",
        "cpf": "266.424.790-50",
        "email": "usertest@gmail.com",
        "tipoUsuario": "COMUM",
        "senha": null // 🔒 Sempre null por segurança
    },
    "tipoUsuario": "COMUM"
}
```

### **❌ Login Falhou (HTTP 401):**

```json
{
    "timestamp": "2025-06-21T22:30:00",
    "status": 401,
    "error": "Unauthorized",
    "message": "Credenciais inválidas"
}
```

## 🔍 **Verificação no Banco**

### **Senha Corretamente Salva:**

```sql
-- Deve mostrar algo como:
-- senha: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
SELECT id, nome, email, LEFT(senha, 10) as senha_hash FROM usuarios WHERE id = 2;
```

### **Senha Incorretamente Salva:**

```sql
-- Se aparecer "root123" em texto plano, há problema de segurança
SELECT id, nome, email, senha FROM usuarios WHERE id = 2;
```

## 🎯 **Teste Rápido**

Execute este comando para testar o login:

```bash
chmod +x teste-login.sh && ./teste-login.sh
```

## 🎉 **Conclusão**

-   ✅ **Senha `null` no response = CORRETO**
-   ✅ **Senha hasheada no banco = SEGURO**
-   ✅ **Login funcionando = SENHA SALVA CORRETAMENTE**
-   ✅ **API seguindo boas práticas de segurança**

---

**💡 Dica**: Em APIs reais, nunca retorne senhas, tokens de acesso ou dados sensíveis nos responses. Use DTOs para controlar exatamente o que é exposto!
