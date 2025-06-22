# 🔧 Corrigir Erro do Campo Status

## 🚨 Problema Identificado

O erro `Data truncated for column 'status'` ocorre porque:

1. **Entidade JPA**: A classe `Ingresso` tem um campo `status` do tipo `StatusIngresso` enum
2. **Tabela do Banco**: A tabela `ingressos` não tem o campo `status`
3. **Resultado**: O Hibernate tenta inserir um valor em uma coluna que não existe

## 🛠️ Solução

### Passo 1: Executar o script SQL

```bash
mysql -u root -p teatro_db < adicionar-status-ingressos.sql
```

### Passo 2: Verificar se funcionou

```bash
mysql -u root -p teatro_db -e "DESCRIBE ingressos;"
```

**Resultado esperado**:

```
+------------------+------------------------------------------------+------+-----+-------------------+----------------+
| Field            | Type                                           | Null | Key | Default           | Extra          |
+------------------+------------------------------------------------+------+-----+-------------------+----------------+
| id               | bigint                                         | NO   | PRI | NULL              | auto_increment |
| usuario_id       | bigint                                         | NO   | MUL | NULL              |                |
| sessao_id        | bigint                                         | NO   | MUL | NULL              |                |
| area_id          | bigint                                         | NO   | MUL | NULL              |                |
| numero_poltrona  | int                                            | NO   |     | NULL              |                |
| valor            | decimal(10,2)                                  | NO   |     | NULL              |                |
| data_compra      | timestamp                                      | YES  |     | CURRENT_TIMESTAMP |                |
| codigo           | varchar(50)                                    | NO   | UNI | NULL              |                |
| status           | enum('VALIDO','UTILIZADO','CANCELADO','REEMBOLSADO') | NO |     | VALIDO            |                |
| data_atualizacao | timestamp                                      | YES  |     | NULL              | on update CURRENT_TIMESTAMP |
| observacoes      | varchar(500)                                   | YES  |     | NULL              |                |
+------------------+------------------------------------------------+------+-----+-------------------+----------------+
```

## 🧪 Teste Após Correção

### 1. Reiniciar a aplicação

```bash
# Parar a aplicação (Ctrl+C)
# Iniciar novamente
mvn spring-boot:run
```

### 2. Testar no Swagger UI

```json
{
    "usuarioId": 2,
    "sessaoId": 7,
    "areaId": 1,
    "numeroPoltrona": 1,
    "valor": 40.0
}
```

### 3. Resposta esperada (HTTP 201)

```json
{
    "id": 1,
    "usuarioId": 2,
    "sessaoId": 7,
    "areaId": 1,
    "numeroPoltrona": 1,
    "valor": 40.0,
    "dataCompra": "2025-06-21T23:02:06.123Z",
    "codigo": "ING-A1B2C3D4",
    "status": "VALIDO",
    "eventoNome": "Hamlet",
    "areaNome": "Plateia A",
    "usuarioNome": "João Silva"
}
```

## 📋 Script SQL Completo

Se o arquivo `adicionar-status-ingressos.sql` não existir, execute este SQL diretamente:

```sql
USE teatro_db;

-- Adicionar o campo status à tabela ingressos
ALTER TABLE ingressos
ADD COLUMN status ENUM('VALIDO', 'UTILIZADO', 'CANCELADO', 'REEMBOLSADO')
NOT NULL DEFAULT 'VALIDO'
AFTER codigo;

-- Adicionar campos de auditoria
ALTER TABLE ingressos
ADD COLUMN data_atualizacao TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
ADD COLUMN observacoes VARCHAR(500) NULL;

-- Verificar a estrutura
DESCRIBE ingressos;
```

## 🎯 Por que isso aconteceu?

1. **Desenvolvimento incremental**: A entidade foi criada com campos que não existiam no banco
2. **Migração manual**: Não usamos ferramentas como Flyway ou Liquibase
3. **Sincronização**: O banco não estava sincronizado com o modelo JPA

## 🚀 Próximos Passos

Após corrigir:

1. ✅ Testar compra de ingresso
2. ✅ Verificar poltronas disponíveis
3. ✅ Testar validações
4. 🎭 **Partir para o frontend!**

## 🔗 Links Úteis

-   **Swagger UI**: http://localhost:8080/api/swagger-ui.html
-   **Health Check**: http://localhost:8080/api/actuator/health
