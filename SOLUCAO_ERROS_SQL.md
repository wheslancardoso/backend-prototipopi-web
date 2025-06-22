# 🔧 Solução de Erros SQL - Sessões

## ❌ **Erros Encontrados:**

### **1. Field 'horario' doesn't have a default value**

-   **Causa**: Campo `horario` não tem valor padrão
-   **Solução**: Inserir valor explícito para `horario`

### **2. Field 'ativo' doesn't have a default value**

-   **Causa**: Campo `ativo` não tem valor padrão
-   **Solução**: Inserir valor explícito para `ativo`

### **3. Data truncated for column 'data_sessao'**

-   **Causa**: Formato de data incorreto
-   **Solução**: Usar formato `YYYY-MM-DD` para DATE

### **4. Duplicate entry for key 'PRIMARY'**

-   **Causa**: IDs já existem na tabela
-   **Solução**: Remover registros existentes primeiro

## ✅ **Script Corrigido:**

```sql
-- 1. Verificar sessões atuais
SELECT 'SESSOES ATUAIS:' as info;
SELECT id, nome, tipo_sessao, data_sessao, horario, ativo, evento_id FROM sessoes;

-- 2. Remover sessões existentes do Hamlet
DELETE FROM sessoes WHERE evento_id = 1;

-- 3. Inserir sessões corretas
INSERT INTO sessoes (nome, tipo_sessao, data_sessao, horario, ativo, evento_id) VALUES
('Hamlet - Manhã', 'MANHA', '2025-06-22', '08:00:00', true, 1),
('Hamlet - Tarde', 'TARDE', '2025-06-22', '14:30:00', true, 1),
('Hamlet - Noite', 'NOITE', '2025-06-22', '19:30:00', true, 1);
```

## 🚀 **Como Executar:**

### **1. Execute o script de correção:**

```bash
mysql -u root -p teatro_db < corrigir-sessoes-final.sql
```

### **2. Verifique se funcionou:**

```bash
mysql -u root -p teatro_db -e "SELECT id, nome, ativo, horario FROM sessoes WHERE evento_id = 1;"
```

### **3. Execute o teste:**

```bash
chmod +x teste-compra-atualizado.sh
./teste-compra-atualizado.sh
```

## 🔍 **Verificações Importantes:**

### **Estrutura da Tabela:**

```sql
DESCRIBE sessoes;
```

### **Dados Após Correção:**

```sql
SELECT id, nome, tipo_sessao, data_sessao, horario, ativo, evento_id
FROM sessoes
WHERE evento_id = 1;
```

### **Sessões Ativas:**

```sql
SELECT id, nome, ativo, horario
FROM sessoes
WHERE evento_id = 1 AND ativo = true;
```

## 🎯 **Resultado Esperado:**

Após a correção, você deve ver:

-   ✅ 3 sessões do Hamlet (IDs: 1, 2, 3)
-   ✅ `ativo = true` para todas
-   ✅ `horario` correto: `08:00:00`, `14:30:00`, `19:30:00`
-   ✅ `data_sessao = 2025-06-22`

## 🚨 **Se ainda houver problemas:**

### **Verificar estrutura da tabela:**

```sql
SHOW CREATE TABLE sessoes;
```

### **Verificar constraints:**

```sql
SELECT * FROM information_schema.table_constraints
WHERE table_name = 'sessoes';
```

### **Limpar completamente e recriar:**

```sql
-- CUIDADO: Remove TODAS as sessões
DELETE FROM sessoes WHERE evento_id = 1;

-- Reinserir
INSERT INTO sessoes (nome, tipo_sessao, data_sessao, horario, ativo, evento_id) VALUES
('Hamlet - Manhã', 'MANHA', '2025-06-22', '08:00:00', true, 1);
```
